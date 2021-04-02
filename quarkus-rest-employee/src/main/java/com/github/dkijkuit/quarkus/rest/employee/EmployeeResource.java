package com.github.dkijkuit.quarkus.rest.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dkijkuit.quarkus.rest.employee.entity.Employee;
import com.github.dkijkuit.quarkus.rest.employee.exception.EmployeeNotFoundException;
import io.quarkus.panache.common.Sort;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Slf4j
@Path("employees")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class EmployeeResource {
    @GET
    @Path("{id}")
    public Employee one(@PathParam Long id) {
        log.info("Loading id {}", id);
        return (Employee) Employee.findByIdOptional(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @GET
    public List<Employee> all() {
        return Employee.listAll(Sort.ascending("id"));
    }

    @POST
    @Transactional
    public Response create(Employee employee) {
        if (employee.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        employee.persist();
        return Response.ok(employee).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Employee update(@PathParam Long id, Employee employee) {
        if (employee.getName() == null) {
            throw new WebApplicationException("Employee Name was not set on request.", 422);
        }

        Employee entity = Employee.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Employee with id of " + id + " does not exist.", 404);
        }

        entity.setName(employee.getName());
        entity.setRole(employee.getRole());

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        Employee entity = Employee.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Employee with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {
        ObjectMapper objectMapper;

        @Inject
        void setObjectMapper(ObjectMapper objectMapper){
            this.objectMapper = objectMapper;
        }

        @Override
        public Response toResponse(Exception exception) {
            log.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}