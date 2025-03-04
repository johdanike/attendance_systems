package org.attendance.services.implementations;
import org.attendance.dtos.requests.AttendanceMessage;
import org.attendance.dtos.responses.AddAttendanceResponse;
import org.attendance.dtos.responses.FindStudentByCardIdResponse;
import org.attendance.services.interfaces.AttendanceMessageHandler;
import org.attendance.services.interfaces.AttendanceMessageService;
import org.attendance.services.interfaces.AttendanceService;
import org.attendance.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.attendance.utils.mapper.mapAttendanceMessage;
import static org.attendance.utils.mapper.mapFindCardMessage;


@Service
public class AttendanceMessageServiceImpl implements AttendanceMessageService {
    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AttendanceMessageHandler attendanceMessageHandler;

    @Override
    public void addMessage(AttendanceMessage attendanceMessage) {
        try {
            FindStudentByCardIdResponse findStudentByCardIdResponse = studentService.findStudentByCardId(mapAttendanceMessage(attendanceMessage));
            AddAttendanceResponse addAttendanceResponse = attendanceService.addAttendance(mapFindCardMessage(findStudentByCardIdResponse, attendanceMessage));
            attendanceMessageHandler.getMessageFromAttendanceHandler(addAttendanceResponse.getMessage(), attendanceMessage.getTopic());
        }catch(Exception e) {
            attendanceMessageHandler.getMessageFromAttendanceHandler(e.getMessage(), attendanceMessage.getTopic());
        }
    }
}
