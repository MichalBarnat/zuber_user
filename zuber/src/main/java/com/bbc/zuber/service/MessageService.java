package com.bbc.zuber.service;

import com.bbc.zuber.exception.RideInfoNotFoundException;
import com.bbc.zuber.exception.UserUuidNotFoundException;
import com.bbc.zuber.feign.ServerFeignClient;
import com.bbc.zuber.kafka.KafkaProducerService;
import com.bbc.zuber.model.message.command.CreateMessageCommand;
import com.bbc.zuber.model.message.dto.MessageDto;
import com.bbc.zuber.model.message.response.MessageResponse;
import com.bbc.zuber.model.rideAssignment.RideAssignment;
import com.bbc.zuber.model.rideinfo.RideInfo;
import com.bbc.zuber.model.user.User;
import com.bbc.zuber.repository.RideInfoRepository;
import com.bbc.zuber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

import static com.bbc.zuber.model.rideAssignment.enums.RideAssignmentStatus.ACCEPTED;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final RideInfoRepository rideInfoRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService producerService;
    private final ServerFeignClient serverFeignClient;

    public MessageResponse sendMessage(CreateMessageCommand command) {
        User user = userRepository.findByUuid(command.getSenderUuid())
                .orElseThrow(() -> new UserUuidNotFoundException(command.getSenderUuid()));

        RideInfo rideInfo = rideInfoRepository.findByUserUuid(user.getUuid())
                .orElseThrow(() -> new RideInfoNotFoundException("You are not currently part of any ride."));

        RideAssignment rideAssignment = serverFeignClient.findByUuid(rideInfo.getRideAssignmentUuid());

        if (rideAssignment.getStatus() != ACCEPTED) {
            return MessageResponse.builder()
                    .message("Your ride is not accepted. Cannot send message.")
                    .build();
        }

//        if (rideAssignment.getStatus() == COMPLETED) {
//            return MessageResponse.builder()
//                    .message("Your ride has been completed. Cannot send messages.")
//                    .build();
//        }

        producerService.sendMessage(command);

        return MessageResponse.builder()
                .message("Message sent successfully.")
                .build();
    }

    public LinkedList<MessageDto> getMessages(long rideInfoId) {
        return serverFeignClient.findMessagesByRideInfoId(rideInfoId);
    }
}
