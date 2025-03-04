package org.attendance.services.implementations;


import org.attendance.data.repositories.StudentRepository;
import org.attendance.dtos.requests.FindStudentByCardIdRequest;
import org.attendance.dtos.requests.RegisterStudentRequest;
import org.attendance.dtos.responses.FindStudentByCardIdResponse;
import org.attendance.dtos.responses.RegisterStudentResponse;
import org.attendance.exceptions.StudentException;
import org.attendance.services.interfaces.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class StudentServiceImplTest {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    public void testThatCheckTheNumberOfRegisteredStudents() {
        assertThat(studentRepository.count(), is(0L));
    }

    @Test
    public void testToRegisterAStudent_RepositoryCountIsOne() {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setFirstName("Miracle");
        request.setLastName("Olasoyin");
        request.setCardId("DERRTA");
        request.setDepartment("Mechatronics");
        request.setMatricNumber("F/HD/23/3430022");
        studentService.registerStudent(request);
        assertThat(studentRepository.count(), is(1L));
    }

    @Test
    public void registerAStudentWithIncompleteField() throws Exception {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setFirstName("Miracle");
        request.setLastName("");
        request.setCardId("DERRTA");
        request.setDepartment("Mechatronics");
        request.setMatricNumber("F/HD/23/3430021");
        assertThrows(StudentException.class, ()-> studentService.registerStudent(request));
    }

    @Test
    public void testThatMatricNumberIsTheRightPattern() throws Exception {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setFirstName("Miracle");
        request.setLastName("Olasoyin");
        request.setCardId("DERRTD");
        request.setDepartment("Mechatronics");
        request.setMatricNumber("F/NC/23/3430021");
        assertThrows(StudentException.class, ()-> studentService.registerStudent(request));
    }
    @Test
    public void testToRegisterAStudentThatExistAlready() {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setFirstName("Miracle");
        request.setLastName("Olasoyin");
        request.setCardId("DERRTD");
        request.setDepartment("Mechatronics");
        request.setMatricNumber("F/HD/23/3430022");
        studentService.registerStudent(request);

        RegisterStudentRequest requestTwo = new RegisterStudentRequest();
        requestTwo.setFirstName("Miracle");
        requestTwo.setLastName("Opemipo");
        requestTwo.setCardId("DERRTA");
        requestTwo.setDepartment("Mechatronics");
        requestTwo.setMatricNumber("F/HD/23/3430022");
        assertThrows(StudentException.class, ()-> studentService.registerStudent(requestTwo));
    }

    @Test
    @DisplayName("check to validate the register student response dto")
    public void testToRegisterAStudentAndCheckName() {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setFirstName("Ebuka");
        request.setLastName("Chukwu");
        request.setCardId("DERRTU");
        request.setDepartment("Mechatronics");
        request.setMatricNumber("F/ND/23/3430024");
        RegisterStudentResponse registerResponse = studentService.registerStudent(request);
        assertThat(registerResponse.getFullName(), is("Chukwu Ebuka"));
    }

    @Test
    public void testThatCardIdCannotBeRegisteredTwice() throws Exception {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setFirstName("Ebuka");
        request.setLastName("Chukwu");
        request.setCardId("DERRTD");
        request.setDepartment("Mechatronics");
        request.setMatricNumber("F/ND/23/3430026");
        studentService.registerStudent(request);

        RegisterStudentRequest requestTwo = new RegisterStudentRequest();
        requestTwo.setFirstName("Praise");
        requestTwo.setLastName("Chukwu");
        requestTwo.setCardId("DERRTD");
        requestTwo.setDepartment("Mechatronics");
        requestTwo.setMatricNumber("F/ND/23/3430030");
        assertThrows(StudentException.class, ()-> studentService.registerStudent(request));
    }
    @Test
    public void testToFindStudentByCardId() {
        RegisterStudentRequest registerStudentRequest = new RegisterStudentRequest();
        registerStudentRequest.setFirstName("Ebuka");
        registerStudentRequest.setLastName("Chukwu");
        registerStudentRequest.setCardId("DERRTU");
        registerStudentRequest.setDepartment("Mechatronics");
        registerStudentRequest.setMatricNumber("F/ND/23/3430026");
        studentService.registerStudent(registerStudentRequest);

        FindStudentByCardIdRequest request = new FindStudentByCardIdRequest();
        request.setCardId("DERRTU");
        FindStudentByCardIdResponse response = studentService.findStudentByCardId(request);
        assertThat(response.getFullName(), is("Chukwu Ebuka"));
        assertThat(response.getMatricNumber(), is("F/ND/23/3430026"));
        assertThat(response.getDepartment(), is("Mechatronics"));
    }
}