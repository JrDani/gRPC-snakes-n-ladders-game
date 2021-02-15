package com.github.jrdani.service;

import com.github.jrdani.models.*;
import com.github.jrdani.models.GameServiceGrpc.*;
import com.github.jrdani.service.request.*;
import io.grpc.stub.*;

public class GameService extends GameServiceImplBase {

    @Override
    public StreamObserver<Dice> roll(StreamObserver<GameState> responseObserver) {
        Player client = Player.newBuilder().setName("Client").setPosition(0).build();
        Player server = Player.newBuilder().setName("Server").setPosition(0).build();
        return new DiceStreamingRequest(responseObserver, client, server);
    }
}
