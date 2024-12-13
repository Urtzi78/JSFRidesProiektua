package domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int bookNumber;
	@ManyToOne
	private Ride ride;
	@ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private Driver user;

	public Booking() {
		super();
	}
	
	public Booking(Ride ride, Driver user) {
		this.ride = ride;
		this.user = user;
	}

	public int getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(int bookNumber) {
		this.bookNumber = bookNumber;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public Driver getUser() {
		return user;
	}

	public void setUser(Driver user) {
		this.user = user;
	}

	public double prezioaKalkulatu() {
		return this.ride.getPrice();
	}

}