package com.ddingcham.grpc.server;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class HelloGrpcServer {
    private final Logger logger = Logger.getLogger(HelloGrpcServer.class.getName());

    private final int port;
    private final Server server;

    public HelloGrpcServer(int port, BindableService service) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                                   .addService(service)
                                   .build();
    }

    public void start() throws IOException, InterruptedException {
        server.start();
        logger.info("start to listen. port : " + port);
        server.awaitTermination();
    }

    public void shutdown() {
        logger.warning("starting shutdown server ....");
        server.shutdown();
        logger.warning("completed shutdown server");
    }

    public boolean isTerminated() {
        return server.isTerminated();
    }
}
