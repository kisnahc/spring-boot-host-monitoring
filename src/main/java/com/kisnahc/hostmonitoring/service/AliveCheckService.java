package com.kisnahc.hostmonitoring.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

@Service
public class AliveCheckService {

    @Async
    public CompletableFuture<Boolean> isALive(String hostName) {

        InetAddress ia = null;

        try {
            ia = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            return CompletableFuture.completedFuture(false);
        }

        try {
            return CompletableFuture.completedFuture(ia.isReachable(3000));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(false);
        }

    }
}
