package eredua.bean;

import java.util.List;
import domain.Booking;
import domain.Driver;
import domain.SessionManager;
import businessLogic.BLFacade;

public class BookingsBean {
	private BLFacade facadeBL;
	private List<Booking> bookings;

	public BookingsBean() {
		facadeBL = FacadeBean.getBusinessLogic();
		loadBookings();
	}

	public List<Booking> getBookings() {
		loadBookings();
		return bookings;
	}

	private void loadBookings() {
		String loggedInEmail = SessionManager.getLoggedUser();
		if (loggedInEmail != null) {
			Driver currentDriver = facadeBL.getDriverByEmail(loggedInEmail);
			bookings = facadeBL.getBookingsByDriver(currentDriver);
		}
	}
}
