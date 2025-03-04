package org.attendance.services.implementations;

import org.attendance.data.models.Attendance;
import org.attendance.data.repositories.AttendanceRepository;
import org.attendance.dtos.requests.*;
import org.attendance.dtos.responses.AddAttendanceResponse;
import org.attendance.dtos.responses.AttendanceHistoryResponse;
import org.attendance.dtos.responses.TotalNumberOfAttendanceResponse;
import org.attendance.exceptions.AttendanceException;
import org.attendance.services.interfaces.AttendanceService;
import org.attendance.services.interfaces.StudentService;
import org.attendance.utils.mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.attendance.utils.mapper.*;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private StudentService studentService;

    @Override
    public AddAttendanceResponse addAttendance(AddAttendanceRequest addAttendanceRequest) {
        checkAllFields(addAttendanceRequest);
        checkIfStudentExist(addAttendanceRequest.getStudentId());
        checkIfAttendanceHasBeenTakenForThatDay(addAttendanceRequest);
        attendanceRepository.save(map(addAttendanceRequest));
        return new AddAttendanceResponse("attendance recorded successfully");
    }
    private void checkIfAttendanceHasBeenTakenForThatDay(AddAttendanceRequest addAttendanceRequest) {
        List<Attendance> foundAttendanceByStudentId = attendanceRepository.findAttendanceByStudentId(addAttendanceRequest.getStudentId());
        foundAttendanceByStudentId.forEach(attendance -> {
            if(attendance != null) {
                StringBuilder message = new StringBuilder();
                if(dateIsTheSameWithIncomingDateForTheSameStudentId(attendance, addAttendanceRequest))
                    throw new AttendanceException(String.valueOf(message.append(addAttendanceRequest
                            .getStudentName()).append(" signed up attendance already for today ")));
            }
        });
    }

    private boolean dateIsTheSameWithIncomingDateForTheSameStudentId(Attendance attendance, AddAttendanceRequest addAttendanceRequest) {
        return attendance.getAttendanceDate().equals(addAttendanceRequest.getAttendanceDate()) && attendance.getStudentId().equals(addAttendanceRequest.getStudentId());
    }

    private void checkAllFields(AddAttendanceRequest addAttendanceRequest) {
        if(addAttendanceRequest.getStudentName().isBlank() || addAttendanceRequest.getStudentName().isEmpty()) throw new AttendanceException("student name field is empty");
        if(addAttendanceRequest.getStudentId().isBlank() || addAttendanceRequest.getStudentId().isEmpty()) throw new AttendanceException("student id field is empty");
        if(addAttendanceRequest.getAttendanceTime().isBlank() || addAttendanceRequest.getAttendanceTime().isEmpty()) throw new AttendanceException("attendance time field is empty");
        if(addAttendanceRequest.getAttendanceDate().isBlank() || addAttendanceRequest.getAttendanceDate().isEmpty()) throw new AttendanceException("attendance date field is empty");
        if(!matricNumberIsValid(addAttendanceRequest.getStudentId())) throw new AttendanceException("student id field is not valid");
    }

    @Override
    public List<AttendanceHistoryResponse> getStudentAttendanceHistory(AttendanceHistoryRequest attendanceHistoryRequest) {
        checkIfStudentExist(attendanceHistoryRequest.getStudentId());
        List<Attendance> attendances = attendanceRepository.findAttendanceByStudentId(attendanceHistoryRequest.getStudentId());
        String startDate = attendanceHistoryRequest.getStartDate();
        String endDate = attendanceHistoryRequest.getEndDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate formatedStartDate = LocalDate.parse(startDate, formatter);
        LocalDate formatedEndDate = LocalDate.parse(endDate, formatter);
        return attendances.stream()
                .filter(attendance -> {
                    LocalDate foundAttendanceDate = LocalDate.parse(attendance.getAttendanceDate(), formatter);
                    return !foundAttendanceDate.isBefore(formatedStartDate) && !foundAttendanceDate.isAfter(formatedEndDate);
                })
                .map(mapper::attendanceHistoryResponseMapper)
                .collect(Collectors.toList());
    }

    @Override
    public TotalNumberOfAttendanceResponse studentAttendanceCount(TotalNumberOfAttendanceRequest totalNumberOfAttendanceRequest) {
        checkIfStudentExist(totalNumberOfAttendanceRequest.getStudentId());
        TotalNumberOfAttendanceResponse totalNumberOfAttendanceResponse = new TotalNumberOfAttendanceResponse();
        totalNumberOfAttendanceResponse.setTotalNumberOfAttendance(getStudentAttendanceHistory(map(totalNumberOfAttendanceRequest)).size());
        return totalNumberOfAttendanceResponse;
    }

    private void checkIfStudentExist(String studentId) {
        studentService.findStudentByMatricNumber(studentId);
    }
}
