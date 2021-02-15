package com.github.jrdani.server;

import com.github.jrdani.service.*;
import io.grpc.*;

import java.io.*;

public class GrpcServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(6565)
                .addService(new GameService())
                .build();
        server.start();
        server.awaitTermination();
    }
}
