package com.kisnahc.hostmonitoring.dto;

import com.kisnahc.hostmonitoring.domain.Host;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HostResponseDto {

    private Long hostId;

    private String hostName;

    private String hostAddress;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public HostResponseDto(Host host) {
        this.hostId = host.getId();
        this.hostName = host.getName();
        this.hostAddress = host.getAddress();
        this.createdDate = host.getCreatedDate();
        this.modifiedDate = host.getModifiedDate();
    }
}
