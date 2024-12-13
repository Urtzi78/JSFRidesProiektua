package eredua.bean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;
import domain.Ride;
import domain.SessionManager;

public class PrezioBean {
	BLFacade facadeBL;
	private int prezio;
	private List<Ride> rides;

	public PrezioBean() {
		facadeBL = FacadeBean.getBusinessLogic();
	}

	public int getPrezio() {
		return prezio;
	}

	public void setPrezio(int prezio) {
		this.prezio = prezio;
	}

	public String bilatu() {
		setRides(facadeBL.getRidesByPrice(prezio));
		return "bilatu";
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

}
