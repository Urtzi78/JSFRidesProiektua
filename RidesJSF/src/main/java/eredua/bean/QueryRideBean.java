package eredua.bean;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import businessLogic.*;
import domain.*;

public class QueryRideBean {
	private BLFacade facadeBL;
	private String dCity;
	private String aCity;
	private Date data;
	private List<String> departCities;
	private List<String> arrivalCities;
	private List<Ride> bidaiaEskuragarriak;
	private Ride selectedRide;

	public QueryRideBean() {
		facadeBL = FacadeBean.getBusinessLogic();
	}

	public String getdCity() {
		return dCity;
	}

	public void setdCity(String dCity) {
		this.dCity = dCity;
	}

	public String getaCity() {
		return aCity;
	}

	public void setaCity(String aCity) {
		this.aCity = aCity;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<String> getDepartCities() {
		return facadeBL.getDepartCities();
	}

	public List<String> getArrivalCities() {
		return facadeBL.getDestinationCities(dCity);
	}

	public List<Ride> getBidaiaEskuragarriak() {
		return bidaiaEskuragarriak;
	}

	public Ride getSelectedRide() {
		return selectedRide;
	}

	public void setSelectedRide(Ride selectedRide) {
		this.selectedRide = selectedRide;
	}

	public void updateArrivalCities(AjaxBehaviorEvent event) {
		if (dCity != null && !dCity.isEmpty()) {
			arrivalCities = facadeBL.getDestinationCities(dCity);
		} else {
			arrivalCities = null;
		}
	}

	public String bilatuBidaiak() {
		try {
			bidaiaEskuragarriak = facadeBL.getRides(dCity, aCity, data);
			if (bidaiaEskuragarriak == null || bidaiaEskuragarriak.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Ez dago bidaiarik.", "Saiatu berriz."));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errorea bidaiak bilatzean.", e.getMessage()));
		}
		return null;
	}

	public String queryRide() {
		if (selectedRide == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Ez da bidaia aukeratu.", null));
			return null;
		}

		Driver currentDriver = facadeBL.getDriverByEmail(SessionManager.getLoggedUser());

		if (selectedRide.getnPlaces() <= 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Ez dago tokirik bidaiarentzat.", null));
			return null;
		}

		if (selectedRide.getDriver().getEmail().equals(currentDriver.getEmail())) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Ezin duzu zurea den bidaia bat erreserbatu.", null));
			return null;
		}

		Booking booking = new Booking(selectedRide, currentDriver);
		boolean success = facadeBL.reserveRide(booking);

		if (success) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Bidaia erreserbatuta!", null));
			return "success";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errorea bidaia erreserbatzean.", null));
			return null;
		}
	}

}
