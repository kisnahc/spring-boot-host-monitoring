package com.kisnahc.hostmonitoring.dto;

import com.kisnahc.hostmonitoring.domain.Host;
import com.kisnahc.hostmonitoring.domain.HostStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HostStatusResponseDto {

    private Long hostId;

    private HostStatus nowHostStatus;

    private String hostName;

    private String hostAddress;

    private LocalDateTime lastAliveDate;

    public HostStatusResponseDto(Host host) {
        this.hostId = host.getId();
        this.nowHostStatus = host.getHostStatus();
        this.hostName = host.getName();
        this.hostAddress = host.getAddress();
        // TODO 마지막 활성 시간 구하기.
//        this.lastAliveDate = host.getLastAliveDate();
    }
}
