package com.elearning.app.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStudentRepository extends JpaRepository<TaskStudent, Long> {
}
