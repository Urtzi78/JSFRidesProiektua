package businessLogic;

import java.util.Date;
import java.util.List;

import dataAccess.*;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.SessionManager;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation implements BLFacade {
	HibernateDataAccess dbManager;

	public BLFacadeImplementation() {
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager = new HibernateDataAccess();
		// dbManager.close();
	}

	public BLFacadeImplementation(HibernateDataAccess da) {
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		dbManager = da;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getDepartCities() {
		dbManager.openSession();
		List<String> departLocations = dbManager.getDepartCities();
		dbManager.closeSession();
		return departLocations;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getDestinationCities(String from) {
		dbManager.openSession();
		List<String> targetCities = dbManager.getArrivalCities(from);
		dbManager.closeSession();
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
		dbManager.openSession();
		String driverEmail = SessionManager.getLoggedUser();
		if (driverEmail == null) {
			throw new IllegalStateException("Ez dago saioa hasita");
		}
		Ride ride = dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
		dbManager.closeSession();
		return ride;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		dbManager.openSession();
		List<Ride> rides = dbManager.getRides(from, to, date);
		dbManager.closeSession();
		return rides;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		dbManager.openSession();
		List<Date> dates = dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.closeSession();
		return dates;
	}

	/**
	 * {@inheritDoc}
	 */
	public void initializeBD() {
		dbManager.initializeDB();
	}

	@Override
	public boolean loginEgin(String email, String password) {
		dbManager.openSession();
		try {
			Driver driver = dbManager.getDriver(email, password);
			if (driver != null) {
				SessionManager.login(email);
				return true;
			}
			return false;
		} finally {
			dbManager.closeSession();
		}
	}

	@Override
	public boolean sortuDriver(String name, String email, String password) {
		dbManager.openSession();
		boolean success = dbManager.createDriver(name, email, password);
		dbManager.closeSession();
		return success;
	}

	@Override
	public boolean reserveRide(Booking booking) {
		dbManager.openSession();
		try {
			Ride ride = booking.getRide();
			if (ride.getnPlaces() <= 0) {
				return false;
			}
			ride.setnPlaces(ride.getnPlaces() - 1);
			dbManager.updateRide(ride);
			dbManager.saveBooking(booking);
			return true;
		} finally {
			dbManager.closeSession();
		}
	}

	@Override
	public List<Booking> getBookingsByDriver(Driver currentDriver) {
		dbManager.openSession();
		try {
			return dbManager.getBookingsByDriver(currentDriver);
		} finally {
			dbManager.closeSession();
		}
	}

	@Override
	public List<Ride> getRidesByDriver(Driver currentDriver) {
		dbManager.openSession();
		try {
			return dbManager.getRidesByDriver(currentDriver);
		} finally {
			dbManager.closeSession();
		}
	}

	@Override
	public Driver getDriverByEmail(String loggedInEmail) {
		dbManager.openSession();
		try {
			return dbManager.getDriverByEmail(loggedInEmail);
		} finally {
			dbManager.closeSession();
		}
	}

	@Override
	public List<Ride> getRidesByPrice(int prezio) {
		dbManager.openSession();
		try {
			return dbManager.getRidesByPrice((float)prezio);
		} finally {
			dbManager.closeSession();
		}
	}

}
