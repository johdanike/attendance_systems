package org.attendance.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterStaffRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
}
