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
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data aukeratua: " + event.getObject()));
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
			facadeBL.createRide(dCity, aCity, data, nSeats, price, "driver1@gmail.com");
			return "ok";
		} catch (RideMustBeLaterThanTodayException e) {
			e.printStackTrace();
			return "error";
		} catch (RideAlreadyExistException e) {
			e.printStackTrace();
			return "error";
		}
	}

}