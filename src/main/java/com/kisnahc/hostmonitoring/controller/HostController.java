package com.kisnahc.hostmonitoring.controller;

import com.kisnahc.hostmonitoring.domain.Host;
import com.kisnahc.hostmonitoring.dto.HostResponseDto;
import com.kisnahc.hostmonitoring.dto.HostStatusResponseDto;
import com.kisnahc.hostmonitoring.dto.UpdateHostResponseDto;
import com.kisnahc.hostmonitoring.dto.UpdateRequestDto;
import com.kisnahc.hostmonitoring.service.HostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class HostController {

    private final HostService hostService;

    @PostMapping("/host")
    public HostResponseDto saveHost(@Valid @RequestBody Host host) throws IOException {
        Host saveHost = hostService.saveHost(host);
        return new HostResponseDto(saveHost);
    }

    @GetMapping("/host/{hostId}")
    public HostResponseDto findHost(@PathVariable Long hostId) {
        Host findHost = hostService.findByHostId(hostId);
        return new HostResponseDto(findHost);
    }

    @GetMapping("/host")
    public Result findHostList() {
        List<Host> hostList = hostService.findAllByHost();

        // 엔티티 -> DTO 변환.
        List<HostResponseDto> collect = hostList.stream()
                .map(host -> new HostResponseDto(host))
                .collect(Collectors.toList());

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
    public HostStatusResponseDto hostStatus(@PathVariable Long hostId) throws IOException {
        hostService.hostStatus(hostId);
        Host updateStatusHost = hostService.findByHostId(hostId);
        return new HostStatusResponseDto(updateStatusHost);
    }

    /**
     * Host status 전체 조회.
     */
    @GetMapping("/host/status")
    public Result hostStatusList() throws IOException {
        List<Host> hostList = hostService.findAllByHost();

        // TODO 성능 개선.
        for (Host host : hostList) {
            hostService.hostStatus(host.getId());
        }

        List<HostStatusResponseDto> collect = hostList.stream()
                .map(HostStatusResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @AllArgsConstructor
    @Data
    private static class Result<T> {
        private int count;
        private T hostList;
    }
}
