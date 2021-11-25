package com.kisnahc.hostmonitoring.service;

import com.kisnahc.hostmonitoring.domain.Host;
import com.kisnahc.hostmonitoring.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HostService {

    private final HostRepository hostRepository;

    /**
     * Host 등록 메서드.
     */
    @Transactional
    public Host saveHost(Host host) throws UnknownHostException {

        InetAddress inetAddress = InetAddress.getByName(host.getName());

        Host getHost = Host.builder()
                .name(inetAddress.getHostName())
                .address(inetAddress.getHostAddress())
                .build();

        return hostRepository.save(getHost);
    }

    /**
     * Host 단건 조회 메서드. (host_id로 조회)
     */
    public Host findByHostId(Long hostId) {
        return hostRepository.findById(hostId).get();
    }

    /**
     * Host 전체 조회 메서드.
     */
    public List<Host> findAllByHost() {
        return hostRepository.findAll();
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

}
