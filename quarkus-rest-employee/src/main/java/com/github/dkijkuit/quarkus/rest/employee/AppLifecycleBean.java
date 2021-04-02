package com.github.dkijkuit.quarkus.rest.employee;

import com.github.dkijkuit.quarkus.rest.employee.entity.Employee;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;

@ApplicationScoped
public class AppLifecycleBean {
    EntityManager em;

    public AppLifecycleBean(EntityManager em) {
        this.em = em;
    }

    private static final Logger log = Logger.getLogger("ListenerBean");

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
        List.of(new Employee("Jean-Luc Picard", "Captain"),
                new Employee("Worf", "Tactical officer"))
                .stream().peek(employee -> log.info(format("Preloaded %s", employee)))
                .forEach(em::persist);
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("The application is stopping...");
    }

}