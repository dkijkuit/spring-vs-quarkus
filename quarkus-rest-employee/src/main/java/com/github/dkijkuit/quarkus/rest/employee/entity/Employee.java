package com.github.dkijkuit.quarkus.rest.employee.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Cacheable
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Employee extends PanacheEntityBase {
    private @Id @GeneratedValue Long id;
    private String name;
    private String role;

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }
}
