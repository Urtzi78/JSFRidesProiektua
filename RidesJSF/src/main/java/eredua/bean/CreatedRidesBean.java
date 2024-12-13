package eredua.bean;

import java.util.List;
import domain.Driver;
import domain.Ride;
import domain.SessionManager;
import businessLogic.BLFacade;

public class CreatedRidesBean {
	private BLFacade facadeBL;
	private List<Ride> rides;

	public CreatedRidesBean() {
		facadeBL = FacadeBean.getBusinessLogic();
		loadCreatedRides();
	}

	public List<Ride> getRides() {
		loadCreatedRides();
		return rides;
	}

	private void loadCreatedRides() {
		Driver currentDriver = facadeBL.getDriverByEmail(SessionManager.getLoggedUser());
		if (currentDriver != null) {
			rides = facadeBL.getRidesByDriver(currentDriver);
		}
	}
}
