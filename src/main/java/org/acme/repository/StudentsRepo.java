package org.acme.repository;

import java.util.List;

import org.acme.domain.Students;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StudentsRepo implements PanacheRepository<Students> {
    public Students findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public List<Students> getAll() {
        return this.listAll();
    }

    public Students findById(Long id) {
        Students student = find("id", id).firstResult();
        if (student != null) {
            return student; 
        }
        return null;
    }
}
