package org.attendance.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.attendance.dtos.requests.*;
import org.attendance.dtos.responses.*;
import org.attendance.services.interfaces.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@AllArgsConstructor
public class StaffController {
    @Autowired
    private StaffService staffService;

    @CrossOrigin(origins = "*")
    @PostMapping("/register/")
    public ResponseEntity<?> registerStaff(@RequestBody RegisterStaffRequest registerStaffRequest) {
        try {
            RegisterStaffResponse registerStaffResponse = staffService.registerStaff(registerStaffRequest);
            return new ResponseEntity<>(registerStaffResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login/")
    public ResponseEntity<?> login(@RequestBody StaffLoginRequest staffLoginRequest) {
        try {
            StaffLoginResponse staffLoginResponse = staffService.loginStaff(staffLoginRequest);
            return new ResponseEntity<>(staffLoginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/logout/")
    public ResponseEntity<?> logout(@RequestBody StafflogoutRequest stafflogoutRequest) {
        try {
            StaffLogoutResponse staffLogoutResponse = staffService.logoutStaff(stafflogoutRequest);
            return new ResponseEntity<>(staffLogoutResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/registerStudent/")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterStudentRequest registerStudentRequest) {
        try {
            RegisterStudentResponse registerStudentResponse = staffService.registerStudent(registerStudentRequest);
            return new ResponseEntity<>(registerStudentResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/getAttendanceHistory/")
    public ResponseEntity<?> getAttendanceHistory(@RequestBody AttendanceHistoryRequest attendanceHistoryRequest){
        try {
            List<AttendanceHistoryResponse> attendanceHistoryResponse = staffService.getAttendanceHistory(attendanceHistoryRequest);
            return new ResponseEntity<>(attendanceHistoryResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/attendancePercentage/")
    public ResponseEntity<?> attendancePercentage(@RequestBody TotalNumberOfAttendanceRequest totalNumberOfAttendanceRequest){
        try {
            GetTotalAttendanceOfStudentResponse getTotalAttendanceOfStudentResponse = staffService.getPercentageAttendanceOfStudent(totalNumberOfAttendanceRequest);
            return new ResponseEntity<>(getTotalAttendanceOfStudentResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
