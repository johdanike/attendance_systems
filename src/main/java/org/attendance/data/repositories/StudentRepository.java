package org.attendance.data.repositories;

import org.attendance.data.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository("student_repository")
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByMatricNumber(String matricNumber);
    Optional<Student> findByCardId(String cardId);
}
