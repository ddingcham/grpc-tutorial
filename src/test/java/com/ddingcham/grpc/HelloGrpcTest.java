package com.ddingcham.grpc;

import com.ddingcham.grpc.server.HelloGrpcServer;
import com.ddingcham.grpc.server.HelloGrpcServerService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.fail;

class HelloGrpcTest {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private HelloGrpcServer cut;

    @BeforeEach
    void setUp() {
        cut = new HelloGrpcServer(9303, new HelloGrpcServerService());
        executorService.submit(() -> {
            try {
                cut.start();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                fail();
            }
        });
    }

//    TODO : currently just run server (make assertion after adding client)
    @Test
    void run_server() throws InterruptedException {
        System.out.println("start");
        Thread.sleep(2_000L);
        System.out.println("stop");
    }

    @AfterEach
    void shutDown() {
        cut.shutdown();
        executorService.shutdown();
    }
}
