package service;

import io.grpc.stub.StreamObserver;
import proto.PingGrpc;
import proto.PingRequest;
import proto.Pong;

import javax.inject.Singleton;

//@Singleton
public class PingService extends PingGrpc.PingImplBase {

    @Override
    public void ping(PingRequest request, StreamObserver<Pong> responseObserver) {
        responseObserver.onNext(Pong.newBuilder().setMessage("pong").build());
        responseObserver.onCompleted();
    }
}
