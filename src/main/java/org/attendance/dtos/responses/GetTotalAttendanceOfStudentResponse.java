package org.attendance.dtos.responses;

import lombok.Data;

@Data
public class GetTotalAttendanceOfStudentResponse {
    private Double totalAttendancePercentage;
    private String message;
}
