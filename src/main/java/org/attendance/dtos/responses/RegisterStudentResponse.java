package org.attendance.dtos.responses;

import lombok.Data;

@Data
public class RegisterStudentResponse {
    private String fullName;
    private String matricNumber;
    private String registrationDate;
    private String message;
    private String email;
}
