package service;

import io.smallrye.mutiny.Uni;
import proto.MutinyPingGrpc;
import proto.PingRequest;
import proto.Pong;

import javax.inject.Singleton;

@Singleton
public class ReactivePingService extends MutinyPingGrpc.PingImplBase {
    @Override
    public Uni<Pong> ping(PingRequest request) {
        return Uni.createFrom().item(() ->
                Pong.newBuilder().setMessage("pong").build());
    }
}
