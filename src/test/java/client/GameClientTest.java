package client;

import client.response.*;
import com.github.jrdani.models.*;
import io.grpc.*;
import io.grpc.stub.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameClientTest {

    private GameServiceGrpc.GameServiceStub stub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        this.stub = GameServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void clientGame() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        GameStateStreamingResponse gameStateStreamingResponse = new GameStateStreamingResponse(latch);
        StreamObserver<Dice> streamObserver = this.stub.roll(gameStateStreamingResponse);
        gameStateStreamingResponse.setDiceStreamObserver(streamObserver);
        gameStateStreamingResponse.roll();
        latch.await();
    }
}
