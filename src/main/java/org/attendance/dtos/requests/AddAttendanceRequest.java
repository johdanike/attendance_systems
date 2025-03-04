package org.attendance.dtos.requests;

import lombok.Data;

@Data
public class AddAttendanceRequest {
    private String studentId;
    private String studentName;
    private String attendanceDate;
    private String attendanceTime;
}
