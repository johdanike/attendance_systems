package org.attendance.services.implementations;

import org.attendance.data.models.Staff;
import org.attendance.data.repositories.StaffRepository;
import org.attendance.data.repositories.StudentRepository;
import org.attendance.dtos.requests.*;
import org.attendance.dtos.responses.*;
import org.attendance.services.interfaces.AttendanceService;
import org.attendance.services.interfaces.StaffService;
import org.attendance.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;
    private String currentStaff;
    @Autowired
    private StudentRepository student_repository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private AttendanceService attendanceService;
    private String staffUsername;

    @Override
    public RegisterStaffResponse registerStaff(RegisterStaffRequest registerStaffRequest) {
        doesUserAlreadyExist(registerStaffRequest.getUsername());
        nullOrWhiteSpaceChecker(registerStaffRequest);


        Staff staff = getStaffDetailsForNewRegistration(registerStaffRequest);

        RegisterStaffResponse registerStaffResponse = new RegisterStaffResponse();
        registerStaffResponse.setFullName(staff.getFirstname() + " " + staff.getLastname());
        registerStaffResponse.setUsername(staff.getUsername());
        registerStaffResponse.setMessage("Staff registered successfully!!!");
        staffUsername = registerStaffResponse.getUsername();

        return registerStaffResponse;
    }

    private void doesUserAlreadyExist(String username) {
        if (staffRepository.findByUsername(username) != null ){
            throw new IllegalArgumentException("Username already exists");
        }
    }

    private static void nullOrWhiteSpaceChecker(RegisterStaffRequest registerStaffRequest) {
        if (containsWhiteSpace(registerStaffRequest.getUsername()) || containsWhiteSpace(registerStaffRequest.getFirstname())
                || containsWhiteSpace(registerStaffRequest.getLastname()) || containsWhiteSpace(registerStaffRequest.getPassword())){
            throw new IllegalArgumentException("Username and password are required!");
        }
        if (registerStaffRequest.getPassword().isEmpty() || registerStaffRequest.getLastname().isEmpty() ||
                registerStaffRequest.getUsername().isEmpty() || registerStaffRequest.getFirstname().isEmpty()){
            throw new IllegalArgumentException("Username or password cannot be empty");
        }
    }

    private static boolean containsWhiteSpace(String username) {
        Pattern pattern = Pattern.compile("(.*?)\\s(.*?)");
        Matcher matcher = pattern.matcher(username);
        return matcher.find();
    }

    private Staff getStaffDetailsForNewRegistration(RegisterStaffRequest registerStaffRequest) {
        Staff staff = new Staff();
        staff.setFirstname(registerStaffRequest.getFirstname());
        staff.setLastname(registerStaffRequest.getLastname());
        staff.setUsername(registerStaffRequest.getUsername());
        staff.setPassword(registerStaffRequest.getPassword());
        staff.setIsLoggedIn(false);

        isRegisteredStaffEntityMoreThan1();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        staff.setDateOfCreation(LocalDate.parse(LocalDate.now().format(formatter), formatter));
        staffRepository.save(staff);
        return staff;
    }

    private void isRegisteredStaffEntityMoreThan1() {
        if (staffRepository.count() == 1){
            throw new IllegalArgumentException("Staff already exists");
        }
    }

    @Override
    public StaffLoginResponse loginStaff(StaffLoginRequest staffLoginRequest) {
        Staff foundStaff = getStaff();
        if (foundStaff == null)
            throw new IllegalArgumentException("Staff not found");
        if (!foundStaff.getPassword().equals(staffLoginRequest.getPassword()) ||
        !foundStaff.getUsername().equals(staffLoginRequest.getUsername()))
            throw new IllegalArgumentException("Wrong username or password");
        checkLoginDetails(staffLoginRequest);
        foundStaff.setIsLoggedIn(true);
        staffRepository.save(foundStaff);
        return loginStaffIfCredentialsAreCorrect(staffLoginRequest, foundStaff);
    }

    private StaffLoginResponse loginStaffIfCredentialsAreCorrect(StaffLoginRequest staffLoginRequest, Staff foundStaff) {
        if (foundStaff.getPassword().equals(staffLoginRequest.getPassword()) &&
                foundStaff.getUsername().equals(staffLoginRequest.getUsername())) {
            foundStaff.setIsLoggedIn(true);
            StaffLoginResponse staffLoginResponse = new StaffLoginResponse();
            staffLoginResponse.setUsername(foundStaff.getUsername());
            staffLoginResponse.setMessage("Logged in successfully");
            return staffLoginResponse;
        }
        return null;
    }

    private void checkLoginDetails(StaffLoginRequest staffLoginRequest) {
        if (containsWhiteSpace(staffLoginRequest.getUsername()) || containsWhiteSpace(staffLoginRequest.getPassword())
        || staffLoginRequest.getUsername().trim().equals("") || staffLoginRequest.getPassword().trim().equals("")){
            throw new IllegalArgumentException("Username or password cannot be empty");
        }
    }

    private Staff getStaff() {
        String username = staffUsername;
        return staffRepository.findByUsername(username);
    }

    @Override
    public StaffLogoutResponse logoutStaff(StafflogoutRequest stafflogoutRequest) {
        Staff staff = getStaff();
        staff.setIsLoggedIn(false);

        StaffLogoutResponse staffLogoutResponse = new StaffLogoutResponse();
        staffLogoutResponse.setMessage("Logged out successfully");
        staffRepository.save(staff);
        return staffLogoutResponse;
    }

    @Override
    public RegisterStudentResponse registerStudent(RegisterStudentRequest registerStudentRequest) {
        Staff foundStaff = getStaff();
        checkIfStaffIsLoggedIn();
        foundStaff.setIsLoggedIn(true);
        return studentService.registerStudent(registerStudentRequest);
    }

    private void checkIfStaffIsLoggedIn(){
        Staff foundStaff = getStaff();
        System.out.println(foundStaff);
        if (!foundStaff.getIsLoggedIn())
            throw new IllegalArgumentException("Staff is not logged in...");
    }

    @Override
    public List<AttendanceHistoryResponse> getAttendanceHistory(AttendanceHistoryRequest getAttendanceHistoryRequest) {
        return attendanceService.getStudentAttendanceHistory(getAttendanceHistoryRequest);
    }

    @Override
    public GetTotalAttendanceOfStudentResponse getPercentageAttendanceOfStudent(TotalNumberOfAttendanceRequest numberOfAttendanceRequest) {

        Double percentageValue = calculateTotalNumberOfAttendancePercentage(numberOfAttendanceRequest);
        GetTotalAttendanceOfStudentResponse totalAttendanceOfStudentResponseInPercentage = new GetTotalAttendanceOfStudentResponse();
        totalAttendanceOfStudentResponseInPercentage.setTotalAttendancePercentage(percentageValue);
        totalAttendanceOfStudentResponseInPercentage.setMessage("Total number of attendance in percentage for Student: "
            + numberOfAttendanceRequest.getStudentId().toUpperCase() + " is: " +percentageValue + "%.");
        return totalAttendanceOfStudentResponseInPercentage;
    }

    private Double calculateTotalNumberOfAttendancePercentage(TotalNumberOfAttendanceRequest numberOfAttendanceRequest) {
        numberOfAttendanceRequest.setStudentId(numberOfAttendanceRequest.getStudentId());
        numberOfAttendanceRequest.setStartDate(numberOfAttendanceRequest.getStartDate());
        numberOfAttendanceRequest.setEndDate(numberOfAttendanceRequest.getEndDate());

        TotalNumberOfAttendanceResponse totalNumberOfAttendanceResponse =
                attendanceService.studentAttendanceCount(numberOfAttendanceRequest);

        double totalNumberOfAttendance = totalNumberOfAttendanceResponse.getTotalNumberOfAttendance();
        return totalNumberOfAttendance / 100;
    }

}
