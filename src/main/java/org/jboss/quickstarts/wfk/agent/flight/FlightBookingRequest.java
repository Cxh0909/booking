package org.jboss.quickstarts.wfk.agent.flight;

/**
 * @author yu zhang
 */
public class FlightBookingRequest {
    private FlightCustomer customer;

    private Flight flight;

    private String future;

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

    public String getFuture() {
        return future;
    }

    public void setFuture(String future) {
        this.future = future;
    }
}
