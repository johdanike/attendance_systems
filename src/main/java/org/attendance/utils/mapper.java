package org.attendance.utils;

import com.google.gson.Gson;
import org.attendance.data.models.Attendance;
import org.attendance.data.models.Student;
import org.attendance.dtos.requests.*;
import org.attendance.dtos.responses.AttendanceHistoryResponse;
import org.attendance.dtos.responses.FeedbackMessage;
import org.attendance.dtos.responses.FindStudentByCardIdResponse;
import org.attendance.dtos.responses.RegisterStudentResponse;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import javax.net.ssl.SSLSocketFactory;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mapper {
    public static Student map(RegisterStudentRequest registerStudentRequest) {
        Student student = new Student();
        student.setFirstName(registerStudentRequest.getFirstName());
        student.setLastName(registerStudentRequest.getLastName());
        student.setMatricNumber(registerStudentRequest.getMatricNumber());
        student.setCardId(registerStudentRequest.getCardId());
        student.setDepartment(registerStudentRequest.getDepartment());
        student.setEmail(registerStudentRequest.getEmail());
        return student;
    }

    public static boolean matricNumberIsValid (String matricNumber) {
        String regex = "(F|P|)/(ND|HD)/\\d+/\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(matricNumber);
        return matcher.find();
    }

    public static RegisterStudentResponse map(Student student) {
        RegisterStudentResponse registerStudentResponse = new RegisterStudentResponse();
        registerStudentResponse.setFullName(student.getLastName() + " " + student.getFirstName());
        registerStudentResponse.setMatricNumber(student.getMatricNumber());
        registerStudentResponse.setRegistrationDate(String.valueOf(student.getDateOfRegistration()));
        registerStudentResponse.setMessage("Student registered successfully");
        return registerStudentResponse;
    }

    public static FindStudentByCardIdResponse findStudentMapper(Student student) {
        FindStudentByCardIdResponse response = new FindStudentByCardIdResponse();
        response.setFullName(student.getLastName() + " " + student.getFirstName());
        response.setMatricNumber(student.getMatricNumber());
        response.setDepartment(student.getDepartment());
        return response;
    }
    public static Attendance map(AddAttendanceRequest addAttendanceRequest) {
        Attendance attendance = new Attendance();
        attendance.setStudentId(addAttendanceRequest.getStudentId());
        attendance.setStudentName(addAttendanceRequest.getStudentName());
        attendance.setAttendanceDate(addAttendanceRequest.getAttendanceDate());
        attendance.setAttendanceTime(addAttendanceRequest.getAttendanceTime());
        return attendance;
    }

    public static AttendanceHistoryResponse attendanceHistoryResponseMapper(Attendance attendance) {
        AttendanceHistoryResponse attendanceHistoryResponse = new AttendanceHistoryResponse();
        attendanceHistoryResponse.setStudentId(attendance.getStudentId());
        attendanceHistoryResponse.setStudentName(attendance.getStudentName());
        attendanceHistoryResponse.setAttendanceDate(attendance.getAttendanceDate());
        attendanceHistoryResponse.setAttendanceTime(attendance.getAttendanceTime());
        attendanceHistoryResponse.setAttendanceDate(attendance.getAttendanceDate());
        return attendanceHistoryResponse;
    }

    public static AttendanceHistoryRequest map(TotalNumberOfAttendanceRequest totalNumberOfAttendanceRequest) {
        AttendanceHistoryRequest attendanceHistoryRequest = new AttendanceHistoryRequest();
        attendanceHistoryRequest.setStudentId(totalNumberOfAttendanceRequest.getStudentId());
        attendanceHistoryRequest.setStartDate(totalNumberOfAttendanceRequest.getStartDate());
        attendanceHistoryRequest.setEndDate(totalNumberOfAttendanceRequest.getEndDate());
        return attendanceHistoryRequest;
    }
    public static FindStudentByCardIdRequest mapAddAttendanceWithFindStudentRequest(String studentId) {
        FindStudentByCardIdRequest findStudentByCardIdRequest = new FindStudentByCardIdRequest();
        findStudentByCardIdRequest.setCardId(studentId);
        return findStudentByCardIdRequest;
    }

    public static AddAttendanceRequest mapFindCardMessage(FindStudentByCardIdResponse findStudentByCardIdResponse, AttendanceMessage attendanceMessage) {
        AddAttendanceRequest addAttendanceRequest = new AddAttendanceRequest();
        addAttendanceRequest.setStudentName(findStudentByCardIdResponse.getFullName());
        addAttendanceRequest.setStudentId(findStudentByCardIdResponse.getMatricNumber());
        addAttendanceRequest.setAttendanceDate(attendanceMessage.getDate());
        addAttendanceRequest.setAttendanceTime(attendanceMessage.getTime());
        return addAttendanceRequest;
    }

    public static FindStudentByCardIdRequest mapAttendanceMessage(AttendanceMessage attendanceMessage) {
        FindStudentByCardIdRequest findStudentByCardIdRequest = new FindStudentByCardIdRequest();
        findStudentByCardIdRequest.setCardId(attendanceMessage.getCardId());
        return findStudentByCardIdRequest;
    }
    public static MqttConnectOptions options() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(60);
        options.setServerURIs(new String[] { "ssl://514ebbfc53bd4d40b820d881483e33b6.s1.eu.hivemq.cloud:8883" });
        options.setUserName("arewaking");
        options.setPassword("Racco1999".toCharArray());
        options.setSocketFactory(SSLSocketFactory.getDefault());
        return options;
    }

    public static String mapMessageAndConvertToJsonString(AtomicReference<String> message) {
        Gson gson = new Gson();
        FeedbackMessage feedbackMessage = new FeedbackMessage();
        feedbackMessage.setMessage(message.get());
        return gson.toJson(feedbackMessage);
    }
}
