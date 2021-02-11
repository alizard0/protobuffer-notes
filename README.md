# protobuffer-notes
> protocol buffers are a language for serializing structure data.

```protobuffer
syntax = "proto2";
package notes;

option java_package = "com.alizardo.protobuffer";
option java_outer_classname = "BookProto";

message Book {
    required string isbn = 1;
    required string name = 2;
    required int32 pages = 3;
    
    Enum BookType {
        ROMANCE = 0;
        DRAMA = 1;
        CRIME = 2;
    }

    optional BookType type = 4;
    
    message BookAuthor {
        required string name = 1;
    }

    repeated BookAuthor author = 5;
}
```

That's an example of book structure using proto-buffers, `book.proto`. This file contains several keywords, namely:
1. `syntax`, for proto buffer version
1. `java_package`, for specifying the java package where the pojo will be generated
1. `java_outer_classname`, for specifying the wrap class name
1. `optional`, for optional fields
1. `repeated`, for repeated fields, proto buffer will preserve the order of repeat values.
1. `required`, for required fields. If field isn't set, it throws `RuntimeException`. Please note, **Required Is Forever**
> You should be very careful about marking fields as required. If at some point you wish to stop writing or sending a required field, it will be problematic to change the field to an optional field – old readers will consider messages without this field to be incomplete and may reject or drop them unintentionally

In terms of data types, it supports `bool`, `int32`, `float`, `double` and `string`. 
Moreover, you can create your own classes and assign them to fields.

Lastly, the *markers* (=1, =2) represent tags used in binary encoding.
> Tag numbers 1-15 require one less byte to encode than higher numbers, so as an optimization you can decide to use those tags for the commonly used or repeated elements, leaving tags 16 and higher for less-commonly used optional elements. Each element in a repeated field requires re-encoding the tag number, so repeated fields are particularly good candidates for this optimization.  

## RPC
> In distributed computing, a remote procedure call (RPC) is when a computer program causes a procedure (subroutine) to execute in a different address space (commonly on another computer on a shared network), which is coded as if it were a normal (local) procedure call, without the programmer explicitly coding the details for the remote interaction. 

### Java RMI
> Invoking a method on a remote object is known as remote method invocation (RMI) or remote invocation, and is the object-oriented programming analog of a remote procedure call (RPC). 

1. **Stub**, server interface used by the client.
Stub, for the interface. 
> The stub acts as a gateway for client side objects and all outgoing requests to server side objects that are routed through it. The stub wraps client object functionality and by adding the network logic ensures the reliable communication channel between client and server.
2. **Skeleton**, server-side implementation of the interface.
> A skeleton acts as gateway for server side objects and all incoming clients requests are routed through it. 

**Workflow**
> When a caller wants to perform remote call on the called object, it delegates requests to its stub which initiates communication with the remote skeleton. Consequently, the stub passes caller arguments over the network to the server skeleton. The skeleton then passes received data to the called object, waits for a response and returns the result to the client stub. Note that there is no direct communication between the caller and the called object. 

### GRPC
Google upgraded version of RPC. It uses HTTP/2 for transport, protocol buffers for data encoding and provides a bunch of new features such as: authentication, bidirectional streaming, timeouts, flow control ...

## Example on Quarkus
1. Requires dependency,
```xml
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-grpc</artifactId>
</dependency>
```

2. Requires maven plugin, in order to generate classes from `.proto` files
```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-maven-plugin</artifactId>
            <version>${quarkus-plugin.version}</version>
            <extensions>true</extensions>
            <executions>
                <execution>
                    <goals>
                        <goal>build</goal>
                        <goal>generate-code</goal>
                        <goal>generate-code-tests</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

3. Write your `ping.proto` file
```proto
syntax = "proto3";
option java_multiple_files = true;
option java_package = "com.alizardo.proto";
option java_outer_classname = "PingServiceProto";

package ping;

service Ping {
    rpc Ping returns (Pong) {}
}

message PingRequest {}

message Pong {      
    string message = 1;
}
```

4. Generate classes using `mvn compile`

5. On `application.properties`, you need to specify where we can find the service,
```properties
quarkus.grpc.clients.ping.host=localhost
```
5. Your service,
```java
@Path("/ping")
public class PingResource {

    @Inject
    @GrpcService("ping")                     
    PingRpc.PingBlockingStub client;   

    @GET
    @Path("/{name}")
    public String hello(@PathParam("name") String name) {
        return client.sayHello(HelloRequest.newBuilder().setName(name).build()).getMessage();  
    }
}
```

## Links
1. https://developers.google.com/protocol-buffers
1. https://en.wikipedia.org/wiki/Remote_procedure_call
1. https://en.wikipedia.org/wiki/Distributed_object_communication
1. https://en.wikipedia.org/wiki/GRPC