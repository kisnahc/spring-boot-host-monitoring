package com.kisnahc.hostmonitoring.controller;

import com.kisnahc.hostmonitoring.domain.Host;
import com.kisnahc.hostmonitoring.repository.HostRepository;
import com.kisnahc.hostmonitoring.service.HostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.net.InetAddress;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HostControllerTest {

    @Autowired
    HostService hostService;

    @Autowired
    HostRepository hostRepository;

    @BeforeEach
    public void delete() {
        hostRepository.deleteAll();
    }

    @Test
    public void testHost() throws Exception {
        //given
        InetAddress inetAddress = InetAddress.getByName("www.google.com");
        String hostAddress = inetAddress.getHostAddress();

        Host host = getHost(inetAddress);

        //when
        Host saveHost = hostService.saveHost(host.getName());

        //then
        assertThat(saveHost.getName()).isEqualTo("www.google.com");
        assertThat(saveHost.getAddress()).isEqualTo(hostAddress);
    }

    @Test
    public void findHost() throws Exception {
        //given
        InetAddress inetAddress = InetAddress.getByName("www.google.com");

        Host host = getHost(inetAddress);

        //when
        Host saveHost = hostService.saveHost(host.getName());
        Host findHost = hostRepository.findById(saveHost.getId()).get();

        //then
        assertThat(saveHost.getName()).isEqualTo(findHost.getName());
        assertThat(saveHost.getAddress()).isEqualTo(findHost.getAddress());
    }

    private Host getHost(InetAddress inetAddress) {
        return Host.builder()
                .name(inetAddress.getHostName())
                .address(inetAddress.getHostAddress())
                .build();
    }


}