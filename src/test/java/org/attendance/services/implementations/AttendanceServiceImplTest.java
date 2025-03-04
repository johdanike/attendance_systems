package org.attendance.services.implementations;

import org.attendance.data.repositories.AttendanceRepository;
import org.attendance.data.repositories.StudentRepository;
import org.attendance.dtos.requests.AddAttendanceRequest;
import org.attendance.dtos.requests.AttendanceHistoryRequest;
import org.attendance.dtos.requests.RegisterStudentRequest;
import org.attendance.dtos.requests.TotalNumberOfAttendanceRequest;
import org.attendance.dtos.responses.AddAttendanceResponse;
import org.attendance.dtos.responses.AttendanceHistoryResponse;
import org.attendance.exceptions.AttendanceException;
import org.attendance.exceptions.StudentException;
import org.attendance.services.interfaces.AttendanceService;
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
public class AttendanceServiceImplTest {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        attendanceRepository.deleteAll();
        studentRepository.deleteAll();
    }

    private RegisterStudentRequest studentData() {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setFirstName("Miracle");
        request.setLastName("Olasoyin");
        request.setCardId("DERRTD");
        request.setDepartment("Mechatronics");
        request.setMatricNumber("F/HD/20/3430019");
        return request;
    }

    @Test
    public void testToCheckTotalNumberOfAttendance() {
        assertThat(attendanceRepository.count(), is(0L));
    }

    @Test
    public void testToAddAttendanceNumber_Count_Is_One() {
        studentService.registerStudent(studentData());
        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/HD/20/3430019");
        addAttendanceRequest.setStudentName("Miracle");
        addAttendanceRequest.setAttendanceDate("23-04-2024");
        addAttendanceRequest.setAttendanceTime("02:22:00");
        attendanceService.addAttendance(addAttendanceRequest);
        assertThat(attendanceRepository.count(), is(1L));
    }

    @Test
    public void testThatAllFieldsAreFilled() throws AttendanceException {
        studentService.registerStudent(studentData());
        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/HD/20/3430019");
        addAttendanceRequest.setStudentName("");
        addAttendanceRequest.setAttendanceDate("23-04-2024");
        addAttendanceRequest.setAttendanceTime("02:22:00");
        assertThrows(AttendanceException.class, ()-> attendanceService.addAttendance(addAttendanceRequest));
    }

    @Test
    public void testThatTheMatricNumberIsInTheRightFormat() throws AttendanceException {
        studentService.registerStudent(studentData());
        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/MD/20/3430019");
        addAttendanceRequest.setStudentName("Miracle");
        addAttendanceRequest.setAttendanceDate("23-04-2024");
        addAttendanceRequest.setAttendanceTime("02:22:00");
        assertThrows(AttendanceException.class, ()-> attendanceService.addAttendance(addAttendanceRequest));
    }
    @Test
    public void testThatAttendanceCanOnlyBeTakenOncePerDay() throws AttendanceException {
        studentService.registerStudent(studentData());
        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/HD/20/3430019");
        addAttendanceRequest.setStudentName("Miracle");
        addAttendanceRequest.setAttendanceDate("18-04-2024");
        addAttendanceRequest.setAttendanceTime("02:22:00");
        attendanceService.addAttendance(addAttendanceRequest);

        AddAttendanceRequest addAttendanceRequestTwo = new AddAttendanceRequest();
        addAttendanceRequestTwo.setStudentId("F/HD/20/3430019");
        addAttendanceRequestTwo.setStudentName("Miracle");
        addAttendanceRequestTwo.setAttendanceDate("18-04-2024");
        addAttendanceRequestTwo.setAttendanceTime("02:22:00");
        assertThrows(AttendanceException.class, ()-> attendanceService.addAttendance(addAttendanceRequestTwo));
    }

    @Test
    public void testToCheckAddAttendanceResponseIsPopulatedProperly() {
        studentService.registerStudent(studentData());
        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/HD/20/3430019");
        addAttendanceRequest.setStudentName("Miracle");
        addAttendanceRequest.setAttendanceDate("12-04-2024");
        addAttendanceRequest.setAttendanceTime("02:22:00");
        AddAttendanceResponse response = attendanceService.addAttendance(addAttendanceRequest);
        assertThat(response.getMessage(), is("attendance recorded successfully"));
    }

    @Test
    @DisplayName("miracle's attendance was recorded in the db, we are getting his attendance history")
    public void testToGetAttendanceHistory() {
        studentService.registerStudent(studentData());
        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/HD/20/3430019");
        addAttendanceRequest.setStudentName("Miracle");
        addAttendanceRequest.setAttendanceDate("14-04-2024");
        addAttendanceRequest.setAttendanceTime("02:22:00");
        attendanceService.addAttendance(addAttendanceRequest);

        AddAttendanceRequest addAttendanceRequestTwo = new AddAttendanceRequest();
        addAttendanceRequestTwo.setStudentId("F/HD/20/3430019");
        addAttendanceRequestTwo.setStudentName("Miracle");
        addAttendanceRequestTwo.setAttendanceDate("15-04-2024");
        addAttendanceRequestTwo.setAttendanceTime("02:22:00");
        attendanceService.addAttendance(addAttendanceRequestTwo);

        AddAttendanceRequest addAttendanceRequestThree = new AddAttendanceRequest();
        addAttendanceRequestThree.setStudentId("F/HD/20/3430019");
        addAttendanceRequestThree.setStudentName("Miracle");
        addAttendanceRequestThree.setAttendanceDate("16-04-2024");
        addAttendanceRequestThree.setAttendanceTime("02:22:00");
        attendanceService.addAttendance(addAttendanceRequestThree);

        AttendanceHistoryRequest attendanceHistoryRequest = new AttendanceHistoryRequest();
        attendanceHistoryRequest.setStudentId("F/HD/20/3430019");
        attendanceHistoryRequest.setStartDate("14-04-2024");
        attendanceHistoryRequest.setEndDate("16-04-2024");
        assertThat(attendanceService.getStudentAttendanceHistory(attendanceHistoryRequest).size(), is(3));
        for(AttendanceHistoryResponse attendanceHistoryResponse: attendanceService.getStudentAttendanceHistory(attendanceHistoryRequest)) {
            System.out.println(attendanceHistoryResponse);
        }

    }

    @Test
    @DisplayName("test to get the number of times miracle attended a class within a period")
    public void testToGetTheNumberOfAttendanceByAStudent() {
        studentService.registerStudent(studentData());
        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/HD/20/3430019");
        addAttendanceRequest.setStudentName("Miracle");
        addAttendanceRequest.setAttendanceDate("14-04-2024");
        addAttendanceRequest.setAttendanceTime("02:22:00");
        attendanceService.addAttendance(addAttendanceRequest);

        AddAttendanceRequest addAttendanceRequestTwo = new AddAttendanceRequest();
        addAttendanceRequestTwo.setStudentId("F/HD/20/3430019");
        addAttendanceRequestTwo.setStudentName("Miracle");
        addAttendanceRequestTwo.setAttendanceDate("15-04-2024");
        addAttendanceRequestTwo.setAttendanceTime("02:22:00");
        attendanceService.addAttendance(addAttendanceRequestTwo);

        AddAttendanceRequest addAttendanceRequestThree = new AddAttendanceRequest();
        addAttendanceRequestThree.setStudentId("F/HD/20/3430019");
        addAttendanceRequestThree.setStudentName("Miracle");
        addAttendanceRequestThree.setAttendanceDate("16-04-2024");
        addAttendanceRequestThree.setAttendanceTime("02:22:00");
        attendanceService.addAttendance(addAttendanceRequestThree);

        TotalNumberOfAttendanceRequest totalNumberOfAttendanceRequest = new TotalNumberOfAttendanceRequest();
        totalNumberOfAttendanceRequest.setStudentId("F/HD/20/3430019");
        totalNumberOfAttendanceRequest.setStartDate("14-04-2024");
        totalNumberOfAttendanceRequest.setEndDate("16-04-2024");
        assertThat(attendanceService.studentAttendanceCount(totalNumberOfAttendanceRequest).getTotalNumberOfAttendance(), is(3));
    }

    @Test
    public void testThatStudentExistBeforeAttendanceCanBeTaken() {
        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/ND/20/3430019");
        addAttendanceRequest.setStudentName("Victor");
        addAttendanceRequest.setAttendanceDate("12-04-2024");
        addAttendanceRequest.setAttendanceTime("02:22:00");
        assertThrows(StudentException.class, ()-> attendanceService.addAttendance(addAttendanceRequest));
    }

}