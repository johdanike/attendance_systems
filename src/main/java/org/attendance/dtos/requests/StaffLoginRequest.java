package org.attendance.dtos.requests;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StaffLoginRequest {
    private String username;
    private String password;
}
