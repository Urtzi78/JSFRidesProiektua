package eredua.bean;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import businessLogic.*;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class CreateRideBean {
	BLFacade facadeBL;
	private String dCity;
	private String aCity;
	private int nSeats;
	private int price;
	private Date data;

	public CreateRideBean() {
		facadeBL = FacadeBean.getBusinessLogic();
	}

	public void onDateSelect(SelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aukeratutako data: " + event.getObject()));
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

	public int getnSeats() {
		return nSeats;
	}

	public void setnSeats(int nSeats) {
		this.nSeats = nSeats;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String createRide() {
		try {
			if (dCity == null || dCity.trim().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage("departCity",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Irteera hiria ezin da hutsik egon.", null));
				return null;
			}

			if (aCity == null || aCity.trim().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage("arrivalCity",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Helmuga hiria ezin da hutsik egon.", null));
				return null;
			}

			if (dCity.equalsIgnoreCase(aCity)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Irteera hiria eta helmuga hiria ezin dira berdinak izan.", null));
				return null;
			}

			if (data == null || new Date().compareTo(data) > 0) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bidaia gaur ondoren sortu behar da.", null));
				return null;
			}

			facadeBL.createRide(dCity, aCity, data, nSeats, price);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Bidaia sortu da!", null));
			return "ok";

		} catch (RideMustBeLaterThanTodayException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return null;
		} catch (RideAlreadyExistException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bidaia hau dagoeneko sortu duzu.", null));
			return null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Errorea bidaia sortzean.", null));
			return null;
		}
	}
}
