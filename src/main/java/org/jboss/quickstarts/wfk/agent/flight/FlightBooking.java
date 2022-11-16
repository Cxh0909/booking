package org.jboss.quickstarts.wfk.agent.flight;

/**
 * @author yu zhang
 */
public class FlightBooking {
    private FlightCustomer customer;

    private Flight flight;

    private String date;

    public FlightCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(FlightCustomer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
