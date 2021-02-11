package controller;

import io.quarkus.grpc.runtime.annotations.GrpcService;
import proto.PingGrpc;
import proto.PingRequest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ping")
public class PingController {

    @Inject
    @GrpcService("ping")
    PingGrpc.PingBlockingStub client;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return client.ping(PingRequest.newBuilder().setMessage("ping").build()).getMessage();
    }
}
