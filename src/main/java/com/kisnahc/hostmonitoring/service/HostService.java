package com.kisnahc.hostmonitoring.service;

import com.kisnahc.hostmonitoring.domain.Host;
import com.kisnahc.hostmonitoring.domain.HostStatus;
import com.kisnahc.hostmonitoring.dto.HostResponseDto;
import com.kisnahc.hostmonitoring.dto.HostStatusResponseDto;
import com.kisnahc.hostmonitoring.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HostService {

    private final HostRepository hostRepository;
    private final AliveCheckService aliveCheckService;
    private final EntityManager entityManager;
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

//        // 호스트 Alive Check.
//        //  TODO 데이터베이스에 host_status 넣을지 말지 고민.
//        if (inetAddress.isReachable(3000)) {
//            getHost.setStatus(HostStatus.ALIVE);
//        } else {
//            getHost.setStatus(HostStatus.DEAD);
//        }

        // 호스트 등록 100개 제한.
        // TODO 메서드 뽑기.
        if (entityManager.createQuery("select h from Host as h").getResultList().size() == 100) {
            throw new ArrayIndexOutOfBoundsException("호스트 등록은 100개까지 저장할 수 있습니다.");
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
    public List<HostResponseDto> gerHosts() {
        List<Host> hostList = findAllByHost();

        // 엔티티 -> DTO 변환.
        List<HostResponseDto> collect = hostList.stream()
                .map(host -> new HostResponseDto(host))
                .collect(Collectors.toList());
        return collect;
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
    public void hostStatus(Long hostId) {
        Host findHost = hostRepository.findById(hostId).get();

        boolean isAlive = aliveCheckService.isALive(findHost.getName());

        if (isAlive) {
            findHost.setStatus(HostStatus.ALIVE);
            findHost.setLastAliveDate(LocalDateTime.now());
        } else {
            findHost.setStatus(HostStatus.DEAD);
        }
    }

    /**
     * Host status 전체 조회 메서드.
     */
    public List<HostStatusResponseDto> getHostsStatus() {
        List<Host> hostList = findAllByHost();

        // TODO 성능 개선.
        for (Host host : hostList) {
            hostStatus(host.getId());
        }

//        hostService.hostStatus(hostList);

        List<HostStatusResponseDto> collect = hostList.stream()
                .map(HostStatusResponseDto::new)
                .collect(Collectors.toList());
        return collect;
    }

}
