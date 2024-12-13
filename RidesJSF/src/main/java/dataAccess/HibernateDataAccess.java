package dataAccess;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import configuration.UtilDate;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import eredua.HibernateUtil;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

import java.util.*;

public class HibernateDataAccess {

	private Session session;

	public HibernateDataAccess() {
		openSession();
		clearDatabase();
		initializeDB();
		closeSession();
	}

	public void openSession() {
		if (session == null || !session.isOpen()) {
			session = HibernateUtil.getSessionFactory().openSession();
			System.out.println("DataAccess session opened");
		}
	}

	public void closeSession() {
		if (session != null && session.isOpen()) {
			session.close();
		}
		System.out.println("DataAccess closed");
	}

	public void initializeDB() {
		Transaction transaction = session.beginTransaction();

		try {
			Calendar today = Calendar.getInstance();
			int month = today.get(Calendar.MONTH);
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 1;
				year += 1;
			}

			// Create drivers
			Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez");
			driver1.setPassword("1");
			Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga");
			driver2.setPassword("2");
			Driver driver3 = new Driver("driver3@gmail.com", "Test driver");
			driver3.setPassword("3");
			Driver driverA = new Driver("a", "A driver");
			driverA.setPassword("a");

			// Create rides for drivers
			driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
			driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8);
			driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);

			driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
			driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);

			driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);

			driverA.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);

			// Persist drivers to the database
			session.persist(driver1);
			session.persist(driver2);
			session.persist(driver3);
			session.persist(driverA);

			// Commit transaction
			transaction.commit();
			System.out.println("Db initialized");
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	public void clearDatabase() {
		Transaction transaction = session.beginTransaction();

		try {
			session.createQuery("DELETE FROM Booking").executeUpdate();
			session.createQuery("DELETE FROM Ride").executeUpdate();
			session.createQuery("DELETE FROM Driver").executeUpdate();

			transaction.commit();
			System.out.println("Database cleared successfully.");
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
			System.out.println("Error clearing the database.");
		}
	}

	public List<String> getDepartCities() {
		try {
			session.beginTransaction();
			Query query = session.createQuery("SELECT DISTINCT r.nondik FROM Ride r ORDER BY r.nondik");
			List<String> cities = query.list();
			session.getTransaction().commit();
			return cities;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<String> getArrivalCities(String nondik) {
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("SELECT DISTINCT r.nora FROM Ride r WHERE r.nondik=:nondik ORDER BY r.nora");
			query.setParameter("nondik", nondik);
			List<String> arrivingCities = query.list();
			session.getTransaction().commit();
			return arrivingCities;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public Ride createRide(String nondik, String nora, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {

		try {
			if (new Date().compareTo(date) > 0) {
				throw new RideMustBeLaterThanTodayException("The ride must be later than today.");
			}

			session.beginTransaction();
			Driver driver = (Driver) session.get(Driver.class, driverEmail);

			if (driver == null) {
				throw new RideAlreadyExistException("Driver ez da existitzen");
			}

			if (driver.doesRideExists(nondik, nora, date)) {
				session.getTransaction().commit();
				throw new RideAlreadyExistException("Ride hau sortu duzu jada");
			}

			Ride ride = driver.addRide(nondik, nora, date, nPlaces, price);
			session.persist(driver);
			session.getTransaction().commit();
			return ride;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw new RuntimeException("Ride sortzean errorea", e);
		}
	}

	public List<Ride> getRides(String nondik, String nora, Date date) {
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("SELECT r FROM Ride r WHERE r.nondik=:nondik AND r.nora=:nora AND r.date=:date");
			query.setParameter("nondik", nondik);
			query.setParameter("nora", nora);
			query.setParameter("date", date);
			List<Ride> rides = query.list();
			session.getTransaction().commit();
			return rides;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Date> getThisMonthDatesWithRides(String nondik, String nora, Date date) {
		try {
			session.beginTransaction();
			Date firstDayMonthDate = UtilDate.firstDayMonth(date);
			Date lastDayMonthDate = UtilDate.lastDayMonth(date);

			Query query = session.createQuery(
					"SELECT DISTINCT r.date FROM Ride r WHERE r.nondik=:nondik AND r.nora=:nora AND r.date BETWEEN :startDate AND :endDate");
			query.setParameter("nondik", nondik);
			query.setParameter("nora", nora);
			query.setParameter("startDate", firstDayMonthDate);
			query.setParameter("endDate", lastDayMonthDate);

			List<Date> dates = query.list();
			session.getTransaction().commit();
			return dates;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public boolean loginEgin(String email, String password) {
		try {
			session.beginTransaction();

			Driver driver = (Driver) session.get(Driver.class, email);
			session.getTransaction().commit();

			if (driver != null && driver.getPassword().equals(password)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	public boolean createDriver(String name, String email, String password) {
		try {
			session.beginTransaction();

			Driver existingDriver = (Driver) session.get(Driver.class, email);
			if (existingDriver != null) {
				session.getTransaction().commit();
				return false;
			}

			Driver newDriver = new Driver(email, name);
			newDriver.setPassword(password);
			session.persist(newDriver);
			session.getTransaction().commit();

			return true;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	public Driver getDriver(String email, String password) {
		try {
			session.beginTransaction();
			Query query = session.createQuery("FROM Driver WHERE email = :email AND password = :password");
			query.setParameter("email", email);
			query.setParameter("password", password);
			Driver driver = (Driver) query.uniqueResult();
			session.getTransaction().commit();
			return driver;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		}
	}

	public void updateRide(Ride ride) {
		try {
			session.beginTransaction();
			session.update(ride);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			throw new RuntimeException("Bidaia eguneratzean errorea.", e);
		}
	}

	public void saveBooking(Booking booking) {
		try {
			session.beginTransaction();

			Driver driver = booking.getUser();
			if (driver != null) {
				session.save(booking);
				session.getTransaction().commit();
			} else {
				throw new RuntimeException("El conductor no está asociado a la reserva.");
			}
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			throw new RuntimeException("Error al guardar la reserva.", e);
		}
	}

	public List<Booking> getBookingsByDriver(Driver currentDriver) {
		try {
			session.beginTransaction();
			Query query = session.createQuery("FROM Booking WHERE user_email = :driverEmail");
			query.setParameter("driverEmail", currentDriver.getEmail());
			List<Booking> bookings = query.list();
			session.getTransaction().commit();
			return bookings;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Ride> getRidesByDriver(Driver currentDriver) {
		try {
			session.beginTransaction();
			Query query = session.createQuery("FROM Ride WHERE driver = :driver");
			query.setParameter("driver", currentDriver);
			List<Ride> rides = query.list();
			session.getTransaction().commit();
			return rides;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public Driver getDriverByEmail(String loggedInEmail) {
		try {
			session.beginTransaction();
			Query query = session.createQuery("FROM Driver WHERE email = :email");
			query.setParameter("email", loggedInEmail);
			Driver driver = (Driver) query.uniqueResult();
			session.getTransaction().commit();
			return driver;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		}
	}

	
	public List<Ride> getRidesByPrice(float price) {
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("SELECT r FROM Ride r WHERE r.price=:price");
			query.setParameter("price", price);
			List<Ride> rides = query.list();
			session.getTransaction().commit();
			return rides;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
}
