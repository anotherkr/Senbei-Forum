package com.yhz.senbeiforummain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * @author 吉良吉影
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private Long id;

    private Long senderId;

    private Long receiverId;

    private String content;

    private LocalDateTime sendTime;


    public ChatMessage(Long senderId, Long receiverId, String content) {

    }
}
