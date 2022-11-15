package org.jboss.quickstarts.wfk.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.quickstarts.wfk.Commodity.Commodity;
import org.jboss.quickstarts.wfk.customer.Customer;

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
	private Customer customer;

	@ManyToOne
	private Commodity commodity;

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

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	@Override
	public String toString() {
		return "Booking{" +
				"id=" + id +
				", bookingStatus=" + bookingStatus +
				'}';
	}
}
