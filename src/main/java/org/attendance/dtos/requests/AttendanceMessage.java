package org.attendance.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
public class AttendanceMessage {
    private String cardId;
    private String date;
    private String time;
    private String topic;
}
