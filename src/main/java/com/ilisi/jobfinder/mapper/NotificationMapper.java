package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.Notification.NotificationDTO;
import com.ilisi.jobfinder.model.Notification;

public class NotificationMapper {
    public static NotificationDTO toNotificationDTO(Notification entity){
        return NotificationDTO.builder()
                .id(entity.getId())
                .title(entity.getTitre())
                .body(entity.getContenu())
                .seen(entity.isVue())
                .sentDate(entity.getDateEnvoi())
                .data(entity.getData())
                .build();
    }
}
