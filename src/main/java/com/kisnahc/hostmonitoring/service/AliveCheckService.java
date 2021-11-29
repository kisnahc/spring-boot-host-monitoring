package com.kisnahc.hostmonitoring.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;

@Service
public class AliveCheckService {

    public boolean isAlive(String hostName) {

        InetAddress inetAddress = null;

        try {
            inetAddress = InetAddress.getByName(hostName);
            inetAddress.isReachable(3000);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
