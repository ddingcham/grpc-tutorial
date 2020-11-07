package com.ddingcham.grpc;

import com.ddingcham.grpc.client.HelloGrpcClient;
import com.ddingcham.grpc.hello.HelloDdingchamGrpc;
import com.ddingcham.grpc.server.HelloGrpcServer;
import com.ddingcham.grpc.server.HelloGrpcServerService;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

class HelloGrpcTest {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TestGrpcChannel testGrpcChannel;
    private HelloGrpcServer server = new HelloGrpcServer(9303, new HelloGrpcServerService());

    @BeforeEach
    void setUp() {
        testGrpcChannel = new TestGrpcChannel("localhost", 9303);
        executorService.execute(() -> {
            try {
                server.start();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                fail();
            }
        });
        Awaitility.await()
                  .until(() -> !server.isTerminated());
    }

    //    TODO : currently just run server (make assertion after adding client)
    @Test
    void simple_asyncStub() throws InterruptedException {
        HelloGrpcClient client = new HelloGrpcClient(testGrpcChannel.asyncStub);
        client.sendAsyncUnary("simple_asyncStub");
        Thread.sleep(3_000L);
    }

    @AfterEach
    void shutDown() throws InterruptedException {
        server.shutdown();
        executorService.shutdown();
        testGrpcChannel.shutdown();
    }

    static class TestGrpcChannel {
        private final ManagedChannel channel;
        private final HelloDdingchamGrpc.HelloDdingchamStub asyncStub;

        public TestGrpcChannel(String name, int port) {
            this.channel = ManagedChannelBuilder.forAddress(name, port)
                                                .usePlaintext()
                                                .build();
            this.asyncStub = HelloDdingchamGrpc.newStub(channel);
        }

        public void shutdown() throws InterruptedException {
            channel.shutdown()
                   .awaitTermination(2, TimeUnit.SECONDS);
        }
    }
}
