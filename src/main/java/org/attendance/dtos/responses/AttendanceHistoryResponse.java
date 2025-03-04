package org.attendance.dtos.responses;

import lombok.Data;

@Data
public class AttendanceHistoryResponse {
    private String studentId;
    private String studentName;
//    private String department;
    private String attendanceDate;
    private String attendanceTime;
}
