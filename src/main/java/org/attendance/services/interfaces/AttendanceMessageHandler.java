package org.attendance.services.interfaces;

public interface AttendanceMessageHandler {
    void getMessageFromAttendanceHandler(String message, String topicToSendMessageTo);
}
