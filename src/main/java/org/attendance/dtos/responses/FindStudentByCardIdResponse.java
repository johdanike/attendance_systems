package org.attendance.dtos.responses;

import lombok.Data;

@Data
public class FindStudentByCardIdResponse {
    private String fullName;
    private String matricNumber;
    private String department;
}
