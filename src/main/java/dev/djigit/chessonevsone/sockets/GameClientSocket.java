package dev.djigit.chessonevsone.sockets;

import dev.djigit.chessonevsone.game.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class GameClientSocket {
    private static GameClientSocket CLIENT_SOCKET_INSTANCE = null;
    private Socket socket;
    private ObjectOutputStream objectWriter;
    private ObjectInputStream objectReader;
    private Consumer<Player.Color> setColorConsumer;
    private ConcurrentLinkedQueue<Messages> messagesQueue;
    private volatile boolean connectionAlive = false;
    private ExecutorService executorService;
    private Future<?> readMessagesFuture;

    // Use Singleton to avoid the socket is closing the connection because
    // AbstractPlainSocketImpl:finalize method is executed when the garbage collector
    // executes stop-the-world. So, we need it to keep the connection open.
    // Memory must be free and connections are closed explicitly.
    private GameClientSocket() {}

    public static GameClientSocket getInstance() {
        if (CLIENT_SOCKET_INSTANCE == null) {
            CLIENT_SOCKET_INSTANCE = new GameClientSocket();
        }
        CLIENT_SOCKET_INSTANCE.cleanUp();
        return CLIENT_SOCKET_INSTANCE;
    }

    public void setSetColorConsumer(Consumer<Player.Color> setColorConsumer) {
        this.setColorConsumer = setColorConsumer;
    }

    private void cleanUp() {
        close();
        objectReader = null;
        objectWriter = null;
        if (messagesQueue != null) messagesQueue.clear();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void connect() {
        try {
            socket = new Socket((String) null, 5678);
            connectionAlive = true;
            System.out.println("Client: connection established.");

            requestColorFromServer();
        } catch (IOException e) {
            throw new RuntimeException("Can't connect to server.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void requestColorFromServer() throws IOException, ClassNotFoundException {
        socket.setSoTimeout(100_000);
        objectWriter = new ObjectOutputStream(socket.getOutputStream());
        objectReader = new ObjectInputStream(socket.getInputStream());

        System.out.println("Client: Request a color from the server.");
        objectWriter.writeObject(Messages.COLOR_REQUEST);
        objectWriter.flush();

        System.out.println("Client: Wait for the color response...");
        Messages colorResponse = (Messages) objectReader.readObject();
        setColorForClient(colorResponse);

        System.out.println("Client: Send 'color receive' response to server.");
        objectWriter.writeObject(Messages.COLOR_RECEIVE);
        objectWriter.flush();

        readMessagesFuture = executorService.submit(getMessageQueueRunnable());
    }

    private void setColorForClient(Messages colorMsg) {
        if (colorMsg.equals(Messages.WHITE_COLOR_RESPONSE))
            setColorConsumer.accept(Player.Color.WHITE);
        else if (colorMsg.equals(Messages.BLACK_COLOR_RESPONSE))
            setColorConsumer.accept(Player.Color.BLACK);
    }

    public void close() {
        while (connectionAlive) {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    socket = null;
                }
                if (readMessagesFuture != null)
                    readMessagesFuture.cancel(true);
                executorService.shutdown();
                executorService.awaitTermination(10, TimeUnit.SECONDS);
                connectionAlive = false;
            } catch (IOException e) {
                throw new RuntimeException("Cannot close a client socket.", e);
            } catch (InterruptedException ex) {
                if (!executorService.isTerminated())
                    executorService.shutdownNow();
            }
        }
    }

    private Runnable getMessageQueueRunnable() {
        return () -> {
            while (connectionAlive) {
                try {
                    Messages msg = (Messages) objectReader.readObject();
                    messagesQueue.add(msg);
                }
                catch (SocketTimeoutException ignored) {
                    // readObject() timeout occurred
                }
                catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
