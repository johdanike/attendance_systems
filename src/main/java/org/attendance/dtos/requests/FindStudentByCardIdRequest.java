package org.attendance.dtos.requests;

import lombok.Data;

@Data
public class FindStudentByCardIdRequest {
    private String cardId;
}
