package org.attendance.services.implementations;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.attendance.dtos.requests.AttendanceMessage;
import org.attendance.dtos.responses.MessageToBeReturned;
import org.attendance.services.interfaces.AttendanceMessageHandler;
import org.attendance.services.interfaces.AttendanceMessageService;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.attendance.utils.mapper.mapMessageAndConvertToJsonString;
import static org.attendance.utils.mapper.options;

@Service
public class AttendanceMessageHandlerImpl implements AttendanceMessageHandler {
    private String broker = "ssl://514ebbfc53bd4d40b820d881483e33b6.s1.eu.hivemq.cloud:8883";
    private String clientId = "yabatech_attendance_system";
    private String topic = "attendance";
    private int subQos = 1;

    private final List<MessageToBeReturned> messageToBeReturned = new ArrayList<>();

    @Autowired
    private AttendanceMessageService attendanceMessageService;

    @PostConstruct
    public void initializeTheClient() {
        try(MqttClient client = new MqttClient(broker, clientId)) {
            client.connect(options());
            getIncomingMessage(client);
        }catch(MqttException e) {
            e.getMessage();
            System.out.println("this is from the connection: " + e.getMessage());
            System.out.println("Connection lost: " + e.getReasonCode());
            System.out.println("Cause: " + e.getCause());
        }
    }

    private void getIncomingMessage(MqttClient client) throws MqttException {
        Gson gson = new Gson();
        if (client.isConnected()) {
            client.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    AttendanceMessage incomingMessage = gson.fromJson(new String(message.getPayload()), AttendanceMessage.class);
                    if(incomingMessage.getTime() !=null && incomingMessage.getDate() != null && incomingMessage.getCardId() != null) {
                        attendanceMessageService.addMessage(incomingMessage);
                    }
                    publishMessageToAttendanceSystem(incomingMessage.getTopic(), client);
                }
                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost: " + cause.getCause());
                    System.out.println("ddddddd: " + cause.getMessage());
                }
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete: " + token.isComplete());
                }
            });
            client.subscribe(topic, subQos);
        }
    }

    private void publishMessageToAttendanceSystem(String subTopic, MqttClient client) throws MqttException {
        AtomicReference<String> message = new AtomicReference<>();

        messageToBeReturned.forEach((attendanceReturnMessages) -> {
            if(attendanceReturnMessages.getTopicToPublishTo().equals(subTopic)) message.set(attendanceReturnMessages.getMessage());
        });

        if(message.get() != null && client.isConnected()) {
            MqttMessage newMessage = new MqttMessage(mapMessageAndConvertToJsonString(message).getBytes());
            newMessage.setQos(1);
            client.publish(subTopic, newMessage);
            messageToBeReturned.removeIf(messageToBeReturned -> messageToBeReturned.getTopicToPublishTo().equals(subTopic));
        }
    }

    @Override
    public void getMessageFromAttendanceHandler(String message, String topicToPublishTo) {
        MessageToBeReturned messageToBeReturned = new MessageToBeReturned();
        messageToBeReturned.setMessage(message);
        messageToBeReturned.setTopicToPublishTo(topicToPublishTo);
        this.messageToBeReturned.add(messageToBeReturned);
    }
}