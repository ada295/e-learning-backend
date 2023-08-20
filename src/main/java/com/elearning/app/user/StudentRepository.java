package com.elearning.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findAllByName(String name);
    List<Student> findAllBySurname(String surname);
    List<Student> findAllByNameAndSurname(String name, String surname);
}
