package org.jboss.quickstarts.wfk.agent;

import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.jboss.quickstarts.wfk.booking.Booking;

/**
 * @author yu zhang
 */
public class TravelAgentRepository {
    @Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;

    TravelAgent create(TravelAgent travelAgent) throws Exception {
        log.info("TravelAgentRepository.create() - Creating " + travelAgent.toString());

        // Write the travelAgent to the database.
        em.persist(travelAgent);

        return travelAgent;
    }

    List<TravelAgent> findAllOrderedById() throws Exception {
        log.info("TravelAgentRepository.findAllOrderedById()");
        TypedQuery<TravelAgent> query = em.createNamedQuery(Booking.FIND_ALL, TravelAgent.class);
        return query.getResultList();
    }

    public TravelAgent findByCustomerId(Long customerId) throws Exception {
        TypedQuery<TravelAgent> query = em.createNamedQuery(Booking.FIND_ALL, TravelAgent.class).setParameter(
                "customerId", customerId);
        return query.getSingleResult();
    }
}
