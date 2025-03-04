package org.attendance.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Student {
    private String firstName;
    private String lastName;
    @Id
    private String matricNumber;
    private String cardId;
    private String department;
    private String email;
    private LocalDate dateOfRegistration = LocalDate.now();
}
