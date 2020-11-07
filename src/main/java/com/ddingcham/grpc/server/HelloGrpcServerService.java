package com.ddingcham.grpc.server;

import com.ddingcham.grpc.hello.HelloDdingchamGrpc;
import com.ddingcham.grpc.hello.HelloRequest;
import com.ddingcham.grpc.hello.HelloResponse;

import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class HelloGrpcServerService extends HelloDdingchamGrpc.HelloDdingchamImplBase {

    private final Logger logger = Logger.getLogger(HelloGrpcServerService.class.getName());

    /**
     * Sets unimplemented status for method on given response stream for unary call.
     * super.unaryHello(request, responseObserver);
     * see ServerCalls.java
     * <p>
     * methodDescriptor of method for which error will be thrown.
     * responseObserver on which error will be set.
     * public static void asyncUnimplementedUnaryCall(
     * MethodDescriptor<?, ?> methodDescriptor, StreamObserver<?> responseObserver) {
     * checkNotNull(methodDescriptor, "methodDescriptor");
     * checkNotNull(responseObserver, "responseObserver");
     * responseObserver.onError(Status.UNIMPLEMENTED
     * .withDescription(String.format("Method %s is unimplemented",
     * methodDescriptor.getFullMethodName()))
     * .asRuntimeException());
     * }
     */
    @Override
    public void unaryHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        logger.info("request : " + request);
        doBusiness(1_000L);
        responseObserver.onNext(HelloResponse.newBuilder()
                                             .setWelcomeMessage("hello ddingcham")
                                             .build());
        doBusiness(1_000L);
        responseObserver.onCompleted();
    }

    private void doBusiness(long executionMilliseconds) {
        try {
            Thread.sleep(executionMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
