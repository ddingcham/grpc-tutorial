package com.ddingcham.grpc.client;

import java.util.logging.Logger;

import com.ddingcham.grpc.hello.HelloDdingchamGrpc;
import com.ddingcham.grpc.hello.HelloRequest;
import com.ddingcham.grpc.hello.HelloResponse;

import io.grpc.stub.StreamObserver;

public class HelloGrpcClient {

    private final Logger logger = Logger.getLogger(HelloGrpcClient.class.getName());
    private final HelloDdingchamGrpc.HelloDdingchamStub asyncStub;

    public HelloGrpcClient(HelloDdingchamGrpc.HelloDdingchamStub asyncStub) {
        this.asyncStub = asyncStub;
    }

    public void sendAsyncUnary(String clientName) {
        HelloRequest request = HelloRequest.newBuilder()
                                           .setClientName(clientName)
                                           .build();
        logger.info("sendAsyncUnary. request : " + request);

        asyncStub.unaryHello(request,
                             new StreamObserver<HelloResponse>() {
                                 @Override
                                 public void onNext(HelloResponse value) {
                                     logger.info("from Async Unary : " + value.getWelcomeMessage());
                                 }

                                 @Override
                                 public void onError(Throwable t) {
                                     logger.warning("onError");
                                     t.printStackTrace();
                                 }

                                 @Override
                                 public void onCompleted() {
                                     logger.info("completed");
                                 }
                             });

        logger.info("this log should be logged before server response");
    }
}
