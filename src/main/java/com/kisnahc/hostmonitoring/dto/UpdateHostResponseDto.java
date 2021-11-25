package com.kisnahc.hostmonitoring.dto;

import com.kisnahc.hostmonitoring.domain.Host;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateHostResponseDto {

    private Long id;

    private String updatedHostName;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public UpdateHostResponseDto(Host host) {
        this.id = host.getId();
        this.updatedHostName = host.getName();
        this.createdDate = host.getCreatedDate();
        this.modifiedDate = host.getModifiedDate();
    }
}
