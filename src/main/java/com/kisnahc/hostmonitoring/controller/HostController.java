package com.kisnahc.hostmonitoring.controller;

import com.kisnahc.hostmonitoring.domain.Host;
import com.kisnahc.hostmonitoring.dto.HostResponseDto;
import com.kisnahc.hostmonitoring.dto.UpdateHostResponseDto;
import com.kisnahc.hostmonitoring.dto.UpdateRequestDto;
import com.kisnahc.hostmonitoring.service.HostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class HostController {

    private final HostService hostService;

    @PostMapping("/host")
    public HostResponseDto saveHost(@Valid @RequestBody Host host) throws UnknownHostException {
        Host saveHost = hostService.saveHost(host);
        return new HostResponseDto(saveHost);
    }

    @GetMapping("/host/{hostId}")
    public HostResponseDto findHost(@PathVariable Long hostId) {
        Host findHost = hostService.findByHostId(hostId);
        return new HostResponseDto(findHost);
    }

    @GetMapping("/host")
    public Result findHosts() {

        List<Host> hostList = hostService.findAllByHost();

        // 엔티티 -> DTO 변환.
        List<HostResponseDto> collect = hostList.stream()
                .map(host -> new HostResponseDto(host))
                .collect(Collectors.toList());

        return new Result(collect, collect.size());
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

    @AllArgsConstructor
    @Data
    private static class Result<T> {
        private T data;
        private int count;
    }
}
