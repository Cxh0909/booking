package org.jboss.quickstarts.wfk.Commodity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.quickstarts.wfk.booking.Booking;

@Entity
@NamedQueries({
		@NamedQuery(name = Commodity.FIND_ALL, query = "SELECT c FROM Commodity c ORDER BY c.id ASC"),
		@NamedQuery(name = Commodity.FIND_BY_TYPE, query = "SELECT c FROM Commodity c WHERE commodity_type = :type")
})
@XmlRootElement
@Table(name = "commodity")
public class Commodity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "Commodity.findAll";

	public static final String FIND_BY_TYPE = "Commodity.findByType";

	@Id
	@ApiModelProperty(hidden = true)
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@NotNull
    @Column(name = "commodity_type")
	@Enumerated(EnumType.STRING)
	private CommodityType commodityType;

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@OneToMany(mappedBy = "commodity", cascade = CascadeType.ALL)
	private List<Booking> bookings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CommodityType getCommodityType() {
		return commodityType;
	}

	public void setCommodityType(CommodityType commodityType) {
		this.commodityType = commodityType;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	@Override
	public String toString() {
		return "Commodity{" +
				"id=" + id +
				", commodityType=" + commodityType +
				", bookings=" + bookings +
				'}';
	}
}
