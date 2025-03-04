package org.attendance.services.interfaces;

import org.attendance.dtos.requests.AddAttendanceRequest;
import org.attendance.dtos.requests.AttendanceHistoryRequest;
import org.attendance.dtos.requests.TotalNumberOfAttendanceRequest;
import org.attendance.dtos.responses.AddAttendanceResponse;
import org.attendance.dtos.responses.AttendanceHistoryResponse;
import org.attendance.dtos.responses.TotalNumberOfAttendanceResponse;

import java.util.List;

public interface AttendanceService {
    AddAttendanceResponse addAttendance(AddAttendanceRequest addAttendanceRequest);
    List<AttendanceHistoryResponse> getStudentAttendanceHistory(AttendanceHistoryRequest attendanceHistoryRequest);
    TotalNumberOfAttendanceResponse studentAttendanceCount(TotalNumberOfAttendanceRequest totalNumberOfAttendanceRequest);
}
