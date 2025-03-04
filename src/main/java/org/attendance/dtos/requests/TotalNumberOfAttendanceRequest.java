package org.attendance.dtos.requests;

import lombok.Data;

@Data
public class TotalNumberOfAttendanceRequest {
    private String studentId;
    private String startDate;
    private String endDate;
}
