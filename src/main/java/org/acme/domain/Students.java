package org.acme.domain;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
public class Students extends PanacheEntity {
    @Column(name = "name")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "age",nullable = false)
    private int age;
    
    @Column(name = "email", unique = true)
    @NotBlank(message = "Email is mandatory")   
    private String email;

    @ManyToMany
    @JoinTable(
        name = "students_university",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "university_id")
    )
    private List<University> university;

}
