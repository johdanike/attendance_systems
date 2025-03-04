package org.attendance.dtos.responses;

import lombok.Data;

@Data
public class MessageToBeReturned {
    private String message;
    private String topicToPublishTo;
}
