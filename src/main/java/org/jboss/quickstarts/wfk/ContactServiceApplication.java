/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.quickstarts.wfk;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jboss.quickstarts.wfk.agent.TravelAgentRestService;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingRestService;
import org.jboss.quickstarts.wfk.contact.ContactRestService;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerRestService;
import org.jboss.quickstarts.wfk.guestbooking.GuestBookingRestService;
import org.jboss.quickstarts.wfk.taxi.TaxiRestService;
import org.jboss.quickstarts.wfk.util.JacksonConfig;
import org.jboss.quickstarts.wfk.util.RestServiceExceptionHandler;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import io.swagger.jaxrs.config.BeanConfig;

/**
 * A class extending {@link Application} and annotated with @ApplicationPath is the Java EE 6 "no XML" approach to activating
 * JAX-RS.
 *
 * <p>
 * Resources are served relative to the servlet path specified in the {@link ApplicationPath} annotation.
 * </p>
 */
@ApplicationPath("/api")
public class ContactServiceApplication extends Application {


    public ContactServiceApplication() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("0.1.0");
        beanConfig.setSchemes(new String[]{"https"});
        // We may no longer need to change this
        // beanConfig.setHost("localhost:8080/jboss-contacts-swagger");
        beanConfig.setBasePath("/api");
        beanConfig.setTitle("csc8104 API");
        beanConfig.setDescription("csc8104 api");
        //Add additional RESTService containing packages here, separated by commas:
        // "org.jboss.quickstarts.wfk.contact," +
        // "org.jboss.quickstarts.wfk.other"
		beanConfig.setResourcePackage(
				"org.jboss.quickstarts.wfk.booking,org.jboss.quickstarts.wfk.customer,org.jboss.quickstarts.wfk"
                        + ".taxi,org.jboss.quickstarts.wfk.guestbooking,org.jboss.quickstarts.wfk.agent");
        beanConfig.setScan(true);

        //Do not edit below
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> services = new HashSet<>();

        //Add RESTful resources here as you create them
        services.add(ContactRestService.class);
        services.add(CustomerRestService.class);
		services.add(TaxiRestService.class);
		services.add(BookingRestService.class);
        services.add(GuestBookingRestService.class);
        services.add(TravelAgentRestService.class);

        //Do not edit below
        services.add(RestServiceExceptionHandler.class);
        services.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        services.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        return services;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<>();
        singletons.add(new JacksonConfig());
        return singletons;
    }


}
