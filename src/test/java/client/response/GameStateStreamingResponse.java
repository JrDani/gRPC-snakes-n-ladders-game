package client.response;

import com.github.jrdani.models.*;
import com.google.common.util.concurrent.*;
import io.grpc.stub.*;

import java.util.*;
import java.util.concurrent.*;

public class GameStateStreamingResponse implements StreamObserver<GameState> {

    CountDownLatch latch;
    StreamObserver<Dice> diceStreamObserver;

    public GameStateStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(GameState gameState) {
        List<Player> list = gameState.getPlayerList();
        list.forEach(p -> System.out.println(p.getName() + ":" + p.getPosition()));
        boolean isGameOver = list.stream()
                .anyMatch(p -> p.getPosition() == 100);
        if (isGameOver) {
            System.out.println("Game Over!");
            this.diceStreamObserver.onCompleted();
        } else {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            this.roll();
        }
        System.out.println("--------------------");
    }

    @Override
    public void onError(Throwable throwable) {
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        this.latch.countDown();
    }

    // start point
    public void roll() {
        int diceValue = ThreadLocalRandom.current().nextInt(1, 7);
        Dice dice = Dice.newBuilder().setValue(diceValue).build();
        this.diceStreamObserver.onNext(dice);
    }

    public void setDiceStreamObserver(StreamObserver<Dice> diceStreamObserver) {
        this.diceStreamObserver = diceStreamObserver;
    }
}
