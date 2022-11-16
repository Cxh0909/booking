package org.jboss.quickstarts.wfk.taxi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.quickstarts.wfk.booking.Booking;

@Entity
@NamedQueries({
		@NamedQuery(name = Taxi.FIND_ALL, query = "SELECT t FROM Taxi t ORDER BY t.id ASC"),
		@NamedQuery(name = Taxi.FIND_BY_TYPE, query = "SELECT c FROM Taxi c WHERE taxi_status = :status")
})
@XmlRootElement
@Table(name = "taxi")
public class Taxi implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "Taxi.findAll";

	public static final String FIND_BY_TYPE = "Taxi.findByType";

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@NotNull
	@Column(name = "license_plate_number")
	private String licensePlateNumber;

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@OneToMany(mappedBy = "taxi", cascade = CascadeType.ALL)
	private List<Booking> bookings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	@Override
	public String toString() {
		return "Taxi{" +
				"id=" + id +
				", licensePlateNumber=" + licensePlateNumber +
				", bookings=" + bookings +
				'}';
	}
}
