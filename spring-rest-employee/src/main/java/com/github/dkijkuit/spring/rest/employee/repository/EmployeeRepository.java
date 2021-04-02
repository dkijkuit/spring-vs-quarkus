package com.github.dkijkuit.spring.rest.employee.repository;

import com.github.dkijkuit.spring.rest.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}