package com.kisnahc.hostmonitoring.controller;

import com.kisnahc.hostmonitoring.domain.Host;
import com.kisnahc.hostmonitoring.dto.*;
import com.kisnahc.hostmonitoring.service.HostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class HostController {

    private final HostService hostService;

    @PostMapping("/host")
    public HostResponseDto saveHost(@Valid @RequestBody SaveHostRequestDto requestDto) throws IOException {
        Host saveHost = hostService.saveHost(requestDto.getHostName());
        return new HostResponseDto(saveHost);
    }

    @GetMapping("/host/{hostId}")
    public HostResponseDto findHost(@PathVariable Long hostId) {
        Host findHost = hostService.findByHostId(hostId);
        return new HostResponseDto(findHost);
    }

    @GetMapping("/host")
    public Result findHostList() {
        List<HostResponseDto> collect = hostService.getHosts();
        return new Result(collect.size(), collect);
    }

    @PutMapping("/host/{hostId}")
    public UpdateHostResponseDto updateHost(@PathVariable Long hostId,
                                            @Valid @RequestBody UpdateRequestDto requestDto) {
        hostService.updateHost(hostId, requestDto.getHostName());
        Host updateHost = hostService.findByHostId(hostId);
        return new UpdateHostResponseDto(updateHost);
    }

    @DeleteMapping("/host/{hostId}")
    public void deleteHost(@PathVariable Long hostId) {
        hostService.deleteHost(hostId);
    }

    /**
     * Host status 단건 조회.
     */
    @GetMapping("/host/status/{hostId}")
    public HostStatusResponseDto hostStatus(@PathVariable Long hostId) {
        hostService.findHostStatus(hostId);
        Host updateHostStatus = hostService.findByHostId(hostId);
        return new HostStatusResponseDto(updateHostStatus);
    }

    /**
     * Host status 전체 조회.
     */
    @GetMapping("/host/status")
    public Result hostStatusList() {
        List<HostStatusResponseDto> collect = hostService.getHostsStatus();
        return new Result(collect.size(), collect);
    }

    @AllArgsConstructor
    @Data
    private static class Result<T> {
        private int count;
        private T hostList;
    }
}
