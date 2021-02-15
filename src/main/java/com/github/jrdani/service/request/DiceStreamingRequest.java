package com.github.jrdani.service.request;

import com.github.jrdani.models.*;
import com.github.jrdani.service.*;
import io.grpc.stub.*;

import java.util.concurrent.*;

public class DiceStreamingRequest implements StreamObserver<Dice> {

    private StreamObserver<GameState> gameStateStreamObserver;
    private Player client;
    private Player server;

    public DiceStreamingRequest(StreamObserver<GameState> gameStateStreamObserver, Player client, Player server) {
        this.gameStateStreamObserver = gameStateStreamObserver;
        this.client = client;
        this.server = server;
    }

    @Override
    public void onNext(Dice dice) {
        this.client = this.getNewPlayerPosition(this.client, dice.getValue());
        if (this.client.getPosition() != 100) {
            this.server = this.getNewPlayerPosition(this.server, ThreadLocalRandom.current().nextInt(1,7));
        }
        this.gameStateStreamObserver.onNext(this.getGameState());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        this.gameStateStreamObserver.onCompleted();
    }

    private GameState getGameState() {
        return GameState.newBuilder()
                .addPlayer(this.client)
                .addPlayer(this.server)
                .build();
    }

    private Player getNewPlayerPosition(Player player, int diceValue) {
        int position = player.getPosition() + diceValue;
        position = SnakesAndLaddersMap.getPosition(position);
        if (position <= 100) {
            player = player.toBuilder()
                    .setPosition(position)
                    .build();
        }
        return player;
    }
}
