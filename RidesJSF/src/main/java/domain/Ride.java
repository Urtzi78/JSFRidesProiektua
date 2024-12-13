package domain;

import java.io.*;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Ride implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer rideNumber;
	private String nondik;
	private String nora;
	private int nPlaces;
	private Date date;
	private float price;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "driver_email", referencedColumnName = "email")
	private Driver driver;

	@OneToMany(mappedBy = "ride")
	private List<Booking> bookings;

	public Ride() {
		super();
	}

	public Ride(Integer rideNumber, String from, String to, Date date, int nPlaces, float price, Driver driver) {
		super();
		this.rideNumber = rideNumber;
		this.nondik = from;
		this.nora = to;
		this.nPlaces = nPlaces;
		this.date = date;
		this.price = price;
		this.driver = driver;
	}

	public Ride(String from, String to, Date date, int nPlaces, float price, Driver driver) {
		super();
		this.nondik = from;
		this.nora = to;
		this.nPlaces = nPlaces;
		this.date = date;
		this.price = price;
		this.driver = driver;
	}

	/**
	 * Get the number of the ride
	 * 
	 * @return the ride number
	 */
	public Integer getRideNumber() {
		return rideNumber;
	}

	/**
	 * Set the ride number to a ride
	 * 
	 * @param ride Number to be set
	 */

	public void setRideNumber(Integer rideNumber) {
		this.rideNumber = rideNumber;
	}

	/**
	 * Get the origin of the ride
	 * 
	 * @return the origin location
	 */

	public String getFrom() {
		return nondik;
	}

	/**
	 * Set the origin of the ride
	 * 
	 * @param origin to be set
	 */

	public void setFrom(String origin) {
		this.nondik = origin;
	}

	/**
	 * Get the destination of the ride
	 * 
	 * @return the destination location
	 */

	public String getTo() {
		return nora;
	}

	/**
	 * Set the origin of the ride
	 * 
	 * @param destination to be set
	 */
	public void setTo(String destination) {
		this.nora = destination;
	}

	/**
	 * Get the free places of the ride
	 * 
	 * @return the available places
	 */

	/**
	 * Get the date of the ride
	 * 
	 * @return the ride date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Set the date of the ride
	 * 
	 * @param date to be set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public int getnPlaces() {
		return nPlaces;
	}

	/**
	 * Set the free places of the ride
	 * 
	 * @param nPlaces places to be set
	 */

	public void setnPlaces(int nPlaces) {
		this.nPlaces = nPlaces;
	}

	/**
	 * Get the driver associated to the ride
	 * 
	 * @return the associated driver
	 */
	public Driver getDriver() {
		return driver;
	}

	/**
	 * Set the driver associated to the ride
	 * 
	 * @param driver to associate to the ride
	 */
	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public String toString() {
		return rideNumber + ";" + ";" + nondik + ";" + nora + ";" + date;
	}

}
