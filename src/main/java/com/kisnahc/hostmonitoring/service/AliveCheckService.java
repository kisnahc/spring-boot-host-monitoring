package com.kisnahc.hostmonitoring.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class AliveCheckService {

    @Async
    public boolean isALive(String hostName) {

        InetAddress ia = null;

        try {
            ia = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            return false;
        }

        try {
            return ia.isReachable(3000);
        } catch (IOException e) {
            return false;
        }

    }
}
