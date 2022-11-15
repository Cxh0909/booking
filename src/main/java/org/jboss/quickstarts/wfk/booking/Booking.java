package org.jboss.quickstarts.wfk.booking;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.taxi.Taxi;

@Entity
@NamedQueries({ @NamedQuery(name = Booking.FIND_ALL, query = "SELECT b FROM Booking b ORDER BY b.id ASC"), })
@XmlRootElement
@Table(name = "booking")
public class Booking implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "Booking.findAll";

	@Id
	@ApiModelProperty(hidden = true)
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(name = "booking_status")
	@Enumerated(EnumType.STRING)
	private BookingStatus bookingStatus = BookingStatus.CREATED;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Customer customer;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Taxi taxi;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Taxi getTaxi() {
		return taxi;
	}

	public void setTaxi(Taxi taxi) {
		this.taxi = taxi;
	}

	@Override
	public String toString() {
		return "Booking{" +
				"id=" + id +
				", bookingStatus=" + bookingStatus +
				'}';
	}
}
