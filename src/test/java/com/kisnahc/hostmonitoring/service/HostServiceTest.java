package com.kisnahc.hostmonitoring.service;

import com.kisnahc.hostmonitoring.domain.Host;
import com.kisnahc.hostmonitoring.repository.HostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class HostServiceTest {

    @Autowired
    HostService hostService;

    @Autowired
    HostRepository hostRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void delete() {
        hostRepository.deleteAll();
    }

    @Test
    public void testHostSave() throws Exception {
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

    @DisplayName("호스트 등록 제한 테스트 (테스트는 100 -> 3 변경)")
    @Test
    public void testSaveSizeLimit() throws Exception {
        //given
        InetAddress inetAddress1 = InetAddress.getByName("google.com");
        InetAddress inetAddress2 = InetAddress.getByName("facebook.com");
        InetAddress inetAddress3 = InetAddress.getByName("microsoft.com");
        InetAddress inetAddress4 = InetAddress.getByName("amazon.com");

        Host host1 = getHost(inetAddress1);
        Host host2 = getHost(inetAddress2);
        Host host3 = getHost(inetAddress3);
        Host host4 = getHost(inetAddress4);

        //when
        hostService.saveHost(host1.getName());
        hostService.saveHost(host2.getName());
        hostService.saveHost(host3.getName());

        //then
        assertThat(ArrayIndexOutOfBoundsException.class).isEqualTo(ArrayIndexOutOfBoundsException.class);
        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> hostService.saveHost(host4.getName()));
        assertEquals("호스트 등록은 100개까지 저장할 수 있습니다.", exception.getMessage());
    }

    @DisplayName("호스트 상태 체크 테스트")
    @Test
    public void testHostStatusCheck() throws Exception {
        //given
        InetAddress inetAddress = InetAddress.getByName("192.168.0.11");

        Host host = getHost(inetAddress);

        //when
        Host saveHost = hostService.saveHost(host.getName());

        boolean isAlive = inetAddress.isReachable(2000);

//        hostService.hostStatus(saveHost.getId());

        //then
        assertThat(saveHost.getName()).isEqualTo("192.168.0.11");
        assertThat(saveHost.getAddress()).isEqualTo("192.168.0.11");
        assertThat(isAlive).isEqualTo(true);
    }

    private Host getHost(InetAddress inetAddress) {
        return Host.builder()
                .name(inetAddress.getHostName())
                .address(inetAddress.getHostAddress())
                .build();
    }

}