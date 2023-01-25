package com.elearning.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserTest,Long> {
    List<UserTest> findAllByName(String name);
    List<UserTest> findAllBySurname(String surname);
    List<UserTest> findAllByNameAndSurname(String name, String surname);
}
