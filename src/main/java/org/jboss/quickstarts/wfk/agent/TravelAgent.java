package org.jboss.quickstarts.wfk.agent;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.customer.Customer;

/**
 * @author yu zhang
 */
@Entity
@Table(name = "agent")
@NamedQueries({
        @NamedQuery(name = TravelAgent.FIND_ALL, query = "SELECT ta FROM TravelAgent ta ORDER BY ta.id ASC"),
        @NamedQuery(name = TravelAgent.FIND_BY_CUSTOMER_ID, query = "SELECT ta FROM TravelAgent ta WHERE ta.customerId"
                + " = :customerId"),
})
public class TravelAgent {

    public static final String FIND_ALL = "TravelAgent.findAll";

    public static final String FIND_BY_CUSTOMER_ID = "TravelAgent.findByCustomerId";

    @Id
    @ApiModelProperty(hidden = true)
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "commodity_type")
    private CommodityType commodityType;

    @Column(name = "booking_detail")
    private String bookingDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public String getBookingDetail() {
        return bookingDetail;
    }

    public void setBookingDetail(String bookingDetail) {
        this.bookingDetail = bookingDetail;
    }
}
