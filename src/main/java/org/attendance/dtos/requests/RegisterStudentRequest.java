package org.attendance.dtos.requests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterStudentRequest {
    private String firstName;
    private String lastName;
    private String matricNumber;
    private String cardId;
    private String department;
    private LocalDate dateOfRegistration;
    private String email;
}
