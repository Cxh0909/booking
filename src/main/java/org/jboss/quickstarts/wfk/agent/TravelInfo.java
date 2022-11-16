package org.jboss.quickstarts.wfk.agent;

import javax.validation.constraints.NotNull;
import org.jboss.quickstarts.wfk.agent.flight.FlightBooking;
import org.jboss.quickstarts.wfk.agent.hotel.HotelBooking;
import org.jboss.quickstarts.wfk.booking.Booking;


public class TravelInfo {
    @NotNull
    private CommodityType commodityType;

    private FlightBooking flightBooking;

    private HotelBooking hotelBooking;

    private Booking taxiBooking;

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public FlightBooking getFlightBooking() {
        return flightBooking;
    }

    public void setFlightBooking(FlightBooking flightBooking) {
        this.flightBooking = flightBooking;
    }

    public HotelBooking getHotelBooking() {
        return hotelBooking;
    }

    public void setHotelBooking(HotelBooking hotelBooking) {
        this.hotelBooking = hotelBooking;
    }

    public Booking getTaxiBooking() {
        return taxiBooking;
    }

    public void setTaxiBooking(Booking taxiBooking) {
        this.taxiBooking = taxiBooking;
    }

    @Override
    public String toString() {
        return "TravelInfo{" +
                "commodityType=" + commodityType +
                ", flightBooking=" + flightBooking +
                ", hotelBooking=" + hotelBooking +
                ", taxiBooking=" + taxiBooking +
                '}';
    }
}
