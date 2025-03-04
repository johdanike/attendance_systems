package org.attendance.services.interfaces;

import org.attendance.dtos.requests.*;
import org.attendance.dtos.responses.*;

import java.util.List;

public interface StaffService {
    RegisterStaffResponse registerStaff(RegisterStaffRequest registerStaffRequest);
    StaffLoginResponse loginStaff(StaffLoginRequest staffLoginRequest);
    StaffLogoutResponse logoutStaff(StafflogoutRequest stafflogoutRequest);
    RegisterStudentResponse registerStudent(RegisterStudentRequest registerStudentRequest);
    List<AttendanceHistoryResponse> getAttendanceHistory(AttendanceHistoryRequest getAttendanceHistoryRequest);
    GetTotalAttendanceOfStudentResponse getPercentageAttendanceOfStudent(TotalNumberOfAttendanceRequest getTotalAttendanceOfStudentRequest);

}
