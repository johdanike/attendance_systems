package org.attendance.data.repositories;

import org.attendance.data.models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("staff_repository")
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findByUsername(String username);
}
