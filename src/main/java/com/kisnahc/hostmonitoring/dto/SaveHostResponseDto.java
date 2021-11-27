package com.kisnahc.hostmonitoring.dto;

import com.kisnahc.hostmonitoring.domain.Host;
import lombok.Data;

@Data
public class SaveHostResponseDto {

    private Long hostId;

    private String hostName;

    private String hostAddress;

    public SaveHostResponseDto(Host host) {
        this.hostId = host.getId();
        this.hostName = host.getName();
        this.hostAddress = host.getAddress();
    }
}
