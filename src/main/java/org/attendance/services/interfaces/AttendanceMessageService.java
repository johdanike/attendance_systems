package org.attendance.services.interfaces;

import org.attendance.dtos.requests.AttendanceMessage;

public interface AttendanceMessageService {
    void addMessage(AttendanceMessage attendanceMessage);
}
