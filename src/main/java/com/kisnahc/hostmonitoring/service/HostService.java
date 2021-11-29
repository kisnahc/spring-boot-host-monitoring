package com.kisnahc.hostmonitoring.service;

import com.kisnahc.hostmonitoring.domain.Host;
import com.kisnahc.hostmonitoring.domain.HostStatus;
import com.kisnahc.hostmonitoring.dto.HostResponseDto;
import com.kisnahc.hostmonitoring.dto.HostStatusResponseDto;
import com.kisnahc.hostmonitoring.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HostService {

    private final HostRepository hostRepository;
    private final AliveCheckService aliveCheckService;

    /**
     * Host 등록 메서드.
     */
    @Transactional
    public Host saveHost(String hostName) throws IOException {

        InetAddress inetAddress = InetAddress.getByName(hostName);

        Host getHost = Host.builder()
                .name(inetAddress.getHostName())
                .address(inetAddress.getHostAddress())
                .build();

        if (hostRepository.findCount() == 100) {
            throw new RuntimeException("호스트 등록은 100까지 가능합니다.");
        }

        return hostRepository.save(getHost);
    }

    /**
     * HostList 조회 메서드.
     */
    public List<Host> findAllByHost() {
        return hostRepository.findAll();
    }

    /**
     * Host 단건 조회 메서드. (host_id로 조회)
     */
    public Host findByHostId(Long hostId) {
        return hostRepository.findById(hostId).get();
    }

    /**
     * Host 전체 조회 후 DTO 변환 메서드.
     */
    public List<HostResponseDto> getHosts() {
        List<Host> hostList = findAllByHost();

        // 엔티티 -> DTO 변환.
        return hostList.stream()
                .map(host -> new HostResponseDto(host))
                .collect(Collectors.toList());
    }

    /**
     * Host 수정 메서드. (host_id로 조회 후 변경감지.)
     */
    @Transactional
    public void updateHost(Long hostId, String hostName) {
        Host findHost = hostRepository.findById(hostId).get();
        findHost.updateHostName(hostName);
    }

    /**
     * Host 삭제 메서드.
     */
    @Transactional
    public void deleteHost(Long hostId) {
        hostRepository.deleteById(hostId);
    }

    /**
     * Host status 단건 조회 메서드.
     */
    @Transactional
    public void findHostStatus(Long hostId) {
        Host findHost = hostRepository.findById(hostId).get();

        boolean isAlive = aliveCheckService.isAlive(findHost.getName());


        if (isAlive) {
            findHost.setStatus(HostStatus.ALIVE);

            // 마지막 Alive 시간.
            if (findHost.getHostStatus() == HostStatus.ALIVE && findHost.getLastAliveDate() == null) {
                findHost.setLastAliveDate(LocalDateTime.now());
            } else {
                findHost.setLastAliveDate(findHost.getLastAliveDate());
            }

        } else {
            findHost.setStatus(HostStatus.DEAD);
        }
    }

    /**
     * Host status 전체 조회 메서드.
     */
    public List<HostStatusResponseDto> getHostsStatus() {
        List<Host> hostList = findAllByHost();

        hostList.stream()
                .parallel()
                .map(Host::getHostStatus)
                .collect(Collectors.toList());

        return hostList.stream()
                .map(HostStatusResponseDto::new)
                .collect(Collectors.toList());
    }

}
