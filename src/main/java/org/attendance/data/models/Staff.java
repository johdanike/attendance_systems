package org.attendance.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
public class Staff {
    private String firstname;
    private String lastname;
    @Id
    private String username;
    private String password;
    private LocalDate dateOfCreation;
    private Boolean isLoggedIn;


    public String toString() {
        return "STAFF [firstname=" + firstname + ", " +
                "lastname=" + lastname +
                ", username=" + username +
                ", password=" + password +
                ", dateOfCreation=" + dateOfCreation +
                ", isLoggedIn=" + isLoggedIn + "]";
    }
}
