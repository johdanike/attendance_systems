package org.attendance.services.implementations;

import org.attendance.data.repositories.AttendanceRepository;
import org.attendance.data.repositories.StaffRepository;
import org.attendance.data.repositories.StudentRepository;
import org.attendance.dtos.requests.*;
import org.attendance.dtos.responses.*;
import org.attendance.services.interfaces.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
public class StaffServiceImplTest {
    @Autowired
    private StaffServiceImpl staffService;
    private RegisterStaffRequest registerStaffRequest;
    private RegisterStaffRequest registerStaffRequest1;
    private StaffLoginRequest staffLoginRequest;
    private RegisterStudentRequest registerStudentRequest1;
    private StafflogoutRequest logoutRequest;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private AttendanceRepository attendance_repository;

    @BeforeEach
    public void setUp() {
        staffRepository.deleteAll();
        studentRepository.deleteAll();
        attendance_repository.deleteAll();

        registerStaffRequest = new RegisterStaffRequest();
        registerStaffRequest.setFirstname("John");
        registerStaffRequest.setLastname("Daniel");
        registerStaffRequest.setPassword("password");
        registerStaffRequest.setUsername("username");

        registerStaffRequest1 = new RegisterStaffRequest();
        registerStaffRequest1.setUsername(" ");
        registerStaffRequest1.setPassword(" ");
        registerStaffRequest1.setFirstname(" ");
        registerStaffRequest1.setLastname(" ");

        staffLoginRequest = new StaffLoginRequest();
        staffLoginRequest.setUsername("username");
        staffLoginRequest.setPassword("password");

        registerStudentRequest1 = new RegisterStudentRequest();
        registerStudentRequest1.setDepartment("Mechatronics");
        registerStudentRequest1.setFirstName("John-Daniel");
        registerStudentRequest1.setLastName("Ikechukwu");
        registerStudentRequest1.setCardId("DERRTD");
        registerStudentRequest1.setMatricNumber("F/HD/23/3430022");

        logoutRequest = new StafflogoutRequest();
        logoutRequest.setUsername("username");
    }

    @Test
    public void newStaffCanBeRegistered_test(){
        RegisterStaffResponse registerStaffResponse = staffService.registerStaff(registerStaffRequest);
        assertEquals("Staff registered successfully!!!", registerStaffResponse.getMessage());
    }

    @Test
    public void staffCannotRegisterWithEmptyOrNullValueCredentials_test() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> staffService.registerStaff(registerStaffRequest1));
        assertEquals("Username and password are required!", exception.getMessage());
    }

    @Test
    public void staffCannotRegisterEmptyStrings_test() {
        registerStaffRequest.setUsername("");
        registerStaffRequest.setPassword("");
        registerStaffRequest.setFirstname("");
        registerStaffRequest.setLastname("");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> staffService.registerStaff(registerStaffRequest));
        assertEquals("Username or password cannot be empty", exception.getMessage());
    }

    @Test
    public void staffCannotRegisterSpaces_test() {
        registerStaffRequest.setUsername(" ");
        registerStaffRequest.setPassword(" ");
        registerStaffRequest.setFirstname(" ");
        registerStaffRequest.setLastname(" ");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> staffService.registerStaff(registerStaffRequest));
        assertEquals("Username and password are required!", exception.getMessage());
    }

    @Test
    public void staffCanLogin_test() {
        staffService.registerStaff(registerStaffRequest);
        StaffLoginResponse staffLoginResponse = staffService.loginStaff(staffLoginRequest);
        assertEquals("Logged in successfully", staffLoginResponse.getMessage());
    }

    @Test
    public void staffCanRegisterStudent_test() {
        staffService.registerStaff(registerStaffRequest);
        staffService.loginStaff(staffLoginRequest);
        RegisterStudentResponse registerStudentResponse = staffService.registerStudent(registerStudentRequest1);
        assertEquals("Student registered successfully", registerStudentResponse.getMessage());
    }

    @Test
    public void staffCanViewAllAttendance_test() {
        staffService.registerStaff(registerStaffRequest);
        staffService.loginStaff(staffLoginRequest);

        registerStudentRequest1 = new RegisterStudentRequest();
        registerStudentRequest1.setDepartment("Mechatronics");
        registerStudentRequest1.setFirstName("Esther");
        registerStudentRequest1.setLastName("Adeola");
        registerStudentRequest1.setCardId("DEAATC");
        registerStudentRequest1.setMatricNumber("F/HD/20/3431019");

        RegisterStudentResponse responseData = staffService.registerStudent(registerStudentRequest1);
        assertEquals("Student registered successfully", responseData.getMessage());

        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/HD/20/3431019");
        addAttendanceRequest.setStudentName("Esther");
        addAttendanceRequest.setAttendanceDate("14-03-2025");
        addAttendanceRequest.setAttendanceTime("2:03:00");
        AddAttendanceResponse response = attendanceService.addAttendance(addAttendanceRequest);
        assertEquals(response.getMessage(), "attendance recorded successfully");

        AddAttendanceRequest addAttendanceRequest1
                = new AddAttendanceRequest();
        addAttendanceRequest1.setStudentId("F/HD/20/3431019");
        addAttendanceRequest1.setStudentName("Esther");
        addAttendanceRequest1.setAttendanceDate("15-03-2025");
        addAttendanceRequest1.setAttendanceTime("1:03:00");
        AddAttendanceResponse response1 = attendanceService.addAttendance(addAttendanceRequest1);
        assertEquals(response1.getMessage(), "attendance recorded successfully");

        AddAttendanceRequest addAttendanceRequest2 = new AddAttendanceRequest();
        addAttendanceRequest2.setStudentId("F/HD/20/3431019");
        addAttendanceRequest2.setStudentName("Esther");
        addAttendanceRequest2.setAttendanceDate("16-03-2025");
        addAttendanceRequest2.setAttendanceTime("3:03:00");
        AddAttendanceResponse response3 = attendanceService.addAttendance(addAttendanceRequest2);
        assertEquals(response3.getMessage(), "attendance recorded successfully");

        AttendanceHistoryRequest attendanceHistoryRequest = new AttendanceHistoryRequest();
        attendanceHistoryRequest.setStudentId(registerStudentRequest1.getMatricNumber());
        attendanceHistoryRequest.setStartDate("14-03-2025");
        attendanceHistoryRequest.setEndDate("16-03-2025");

        List<AttendanceHistoryResponse> attendanceHistoryResponse =
                staffService.getAttendanceHistory(attendanceHistoryRequest);
        System.out.println(attendanceHistoryResponse);
        assertEquals(1, studentRepository.count());
    }

    @Test
    public void whenStudentTapsIn_attendanceIsAdded_test() {
        staffService.registerStaff(registerStaffRequest);
        staffService.loginStaff(staffLoginRequest);

        registerStudentRequest1 = new RegisterStudentRequest();
        registerStudentRequest1.setDepartment("Nanotech");
        registerStudentRequest1.setFirstName("Daniel");
        registerStudentRequest1.setLastName("Adeola");
        registerStudentRequest1.setCardId("DEAQTC");
        registerStudentRequest1.setMatricNumber("F/HD/20/3421019");
        staffService.registerStudent(registerStudentRequest1);

        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/HD/20/3421019");
        addAttendanceRequest.setStudentName("Daniel");
        addAttendanceRequest.setAttendanceDate("14-03-2025");
        addAttendanceRequest.setAttendanceTime("2:03:00");
        AddAttendanceResponse response = attendanceService.addAttendance(addAttendanceRequest);
        assertEquals(response.getMessage(), "attendance recorded successfully");
    }

    @Test
    public void staffWhenLoggedInCanLogout_test(){
        staffService.registerStaff(registerStaffRequest);
        staffService.loginStaff(staffLoginRequest);
        StaffLogoutResponse logoutResponse = staffService.logoutStaff(logoutRequest);
        assertEquals(logoutResponse.getMessage(), "Logged out successfully");
    }

    @Test
    public void staffCanViewTotalNumberOfDays_studentWasInAttendance_test(){
        staffService.registerStaff(registerStaffRequest);
        staffService.loginStaff(staffLoginRequest);

        registerStudentRequest1 = new RegisterStudentRequest();
        registerStudentRequest1.setDepartment("Mechatronics");
        registerStudentRequest1.setFirstName("Esther");
        registerStudentRequest1.setLastName("Adeola");
        registerStudentRequest1.setCardId("DEAATC");
        registerStudentRequest1.setMatricNumber("F/HD/20/3431019");

        RegisterStudentResponse responseData = staffService.registerStudent(registerStudentRequest1);
        assertEquals("Student registered successfully", responseData.getMessage());

        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentId("F/HD/20/3431019");
        addAttendanceRequest.setStudentName("Esther");
        addAttendanceRequest.setAttendanceDate("14-03-2025");
        addAttendanceRequest.setAttendanceTime("2:03:00");
        AddAttendanceResponse response = attendanceService.addAttendance(addAttendanceRequest);
        assertEquals(response.getMessage(), "attendance recorded successfully");

        AddAttendanceRequest addAttendanceRequest1
                = new AddAttendanceRequest();
        addAttendanceRequest1.setStudentId("F/HD/20/3431019");
        addAttendanceRequest1.setStudentName("Esther");
        addAttendanceRequest1.setAttendanceDate("15-03-2025");
        addAttendanceRequest1.setAttendanceTime("1:03:00");
        AddAttendanceResponse response1 = attendanceService.addAttendance(addAttendanceRequest1);
        assertEquals(response1.getMessage(), "attendance recorded successfully");

        AddAttendanceRequest addAttendanceRequest2 = new AddAttendanceRequest();
        addAttendanceRequest2.setStudentId("F/HD/20/3431019");
        addAttendanceRequest2.setStudentName("Esther");
        addAttendanceRequest2.setAttendanceDate("16-03-2025");
        addAttendanceRequest2.setAttendanceTime("3:03:00");
        AddAttendanceResponse response3 = attendanceService.addAttendance(addAttendanceRequest2);
        assertEquals(response3.getMessage(), "attendance recorded successfully");

        AttendanceHistoryRequest attendanceHistoryRequest = new AttendanceHistoryRequest();
        attendanceHistoryRequest.setStudentId(registerStudentRequest1.getMatricNumber());
        attendanceHistoryRequest.setStartDate("14-03-2025");
        attendanceHistoryRequest.setEndDate("16-03-2025");

        List<AttendanceHistoryResponse> attendanceHistoryResponse =
                staffService.getAttendanceHistory(attendanceHistoryRequest);
        System.out.println(attendanceHistoryResponse);
        assertEquals(1, studentRepository.count());

        TotalNumberOfAttendanceRequest totalNumberOfAttendanceRequest = new TotalNumberOfAttendanceRequest();
        totalNumberOfAttendanceRequest.setStudentId(registerStudentRequest1.getMatricNumber());
        totalNumberOfAttendanceRequest.setStartDate("14-03-2025");
        totalNumberOfAttendanceRequest.setEndDate("16-03-2025");
        GetTotalAttendanceOfStudentResponse totalNumberOfAttendanceResponse = staffService.getPercentageAttendanceOfStudent(totalNumberOfAttendanceRequest);
        assertEquals("Total number of attendance in percentage for Student: F/HD/20/3431019 is: 0.03%.", totalNumberOfAttendanceResponse.getMessage());
    }


}