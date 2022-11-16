package org.jboss.quickstarts.wfk.agent;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;


public class TravelInfo {
    @NotNull
    private CommodityType commodityType;

    @NotNull
    private String bookingParams;

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public String getBookingParams() {
        return bookingParams;
    }

    public void setBookingParams(String bookingParams) {
        this.bookingParams = bookingParams;
    }
}
