package org.attendance.services.implementations;

import org.attendance.data.models.Student;
import org.attendance.data.repositories.StudentRepository;
import org.attendance.dtos.requests.FindStudentByCardIdRequest;
import org.attendance.dtos.requests.RegisterStudentRequest;
import org.attendance.dtos.responses.FindStudentByCardIdResponse;
import org.attendance.dtos.responses.RegisterStudentResponse;
import org.attendance.exceptions.StudentException;
import org.attendance.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.attendance.utils.mapper.*;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public RegisterStudentResponse registerStudent(RegisterStudentRequest registerStudentRequest) {
        checkIfFieldsIsEmpty(registerStudentRequest);
        checkIfStudentExists(registerStudentRequest);
        checkIfCardIdExist(registerStudentRequest);
        Student student = map(registerStudentRequest);
        studentRepository.save(student);

        return map(student);
    }

    private void checkIfStudentExists(RegisterStudentRequest registerStudentRequest) {
        Optional<Student> student = studentRepository.findByMatricNumber(registerStudentRequest.getMatricNumber());
        if(student.isPresent()) throw new StudentException("Student already exists");
    }

    private void checkIfFieldsIsEmpty(RegisterStudentRequest registerStudentRequest) {
        if (registerStudentRequest.getFirstName().isBlank() || registerStudentRequest.getFirstName().isEmpty()) throw new StudentException("first name cannot be empty");
        if(registerStudentRequest.getLastName().isBlank() || registerStudentRequest.getLastName().isEmpty()) throw new StudentException("last name cannot be empty");
        if(registerStudentRequest.getMatricNumber().isBlank() || registerStudentRequest.getMatricNumber().isEmpty()) throw new StudentException("matricNumber cannot be empty");
        if(registerStudentRequest.getDepartment().isBlank() || registerStudentRequest.getDepartment().isEmpty()) throw new StudentException("department cannot be empty");
        if(registerStudentRequest.getCardId().isBlank() || registerStudentRequest.getCardId().isEmpty()) throw new StudentException("cardId cannot be empty");
        if(!matricNumberIsValid(registerStudentRequest.getMatricNumber())) throw new StudentException("matric number is invalid");
    }
    @Override
    public FindStudentByCardIdResponse findStudentByCardId(FindStudentByCardIdRequest findStudentByCardIdRequest) {
        Optional<Student> foundStudent = studentRepository.findByCardId(findStudentByCardIdRequest.getCardId());
        if(foundStudent.isPresent()) {
            Student student = foundStudent.get();
            return findStudentMapper(student);
        }else {
            throw new StudentException("Student not found: register student");
        }
    }

    @Override
    public void findStudentByMatricNumber(String matricNumber) {
        Optional<Student> foundStudent = studentRepository.findByMatricNumber(matricNumber);
        if(!foundStudent.isPresent()) {
            throw new StudentException("Student not found: register student");
        }
    }

    private void checkIfCardIdExist(RegisterStudentRequest registerStudentRequest) {
        Optional<Student> foundStudent = studentRepository.findByCardId(registerStudentRequest.getCardId());
        if(foundStudent.isPresent()) throw new StudentException("Student card number already exists");
    }
}
