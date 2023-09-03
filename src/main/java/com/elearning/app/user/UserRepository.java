package com.elearning.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);

    default List<UserAccount> findAllByRoles(List<UserRole> roles) {
       return findAll().stream().filter(e->e.getRoles().containsAll(roles))
                .collect(Collectors.toList());
    }

    List<UserAccount> findAllByFirstNameAndLastName(String firstName, String lastName);

    List<UserAccount> findByFirstName(String firstName);

    List<UserAccount> findByLastName(String lastName);
}
