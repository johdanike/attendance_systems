package org.attendance.services.interfaces;

import org.attendance.dtos.requests.FindStudentByCardIdRequest;
import org.attendance.dtos.requests.RegisterStudentRequest;
import org.attendance.dtos.responses.FindStudentByCardIdResponse;
import org.attendance.dtos.responses.RegisterStudentResponse;

import java.util.Optional;

public interface StudentService {
    RegisterStudentResponse registerStudent(RegisterStudentRequest registerStudentRequest);
    FindStudentByCardIdResponse findStudentByCardId(FindStudentByCardIdRequest findStudentByCardIdRequest);
    void  findStudentByMatricNumber(String matricNumber);
}
