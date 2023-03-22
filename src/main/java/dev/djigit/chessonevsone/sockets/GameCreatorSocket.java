package dev.djigit.chessonevsone.sockets;

import dev.djigit.chessonevsone.game.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

public class GameCreatorSocket {

    private static GameCreatorSocket CREATOR_SOCKET_INSTANCE = null;
    private ServerSocket socket;
    private Socket clientSocket;
    private ObjectInputStream objectReader;
    private ObjectOutputStream objectWriter;
    private ConcurrentLinkedQueue<Messages> messagesQueue;
    private volatile boolean connectionAlive = false;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<?> readMessagesFuture;

    // Use Singleton to avoid the socket is closing the connection because
    // AbstractPlainSocketImpl:finalize method is executed when the garbage collector
    // executes stop-the-world. So, we need it to keep the connection open.
    // Memory must be free and connections are closed explicitly.
    private GameCreatorSocket() {}

    public static GameCreatorSocket getInstance() {
        if (CREATOR_SOCKET_INSTANCE == null) {
            CREATOR_SOCKET_INSTANCE = new GameCreatorSocket();
        }
        CREATOR_SOCKET_INSTANCE.cleanUp();
        return CREATOR_SOCKET_INSTANCE;
    }

    private void cleanUp() {
        close();
        objectReader = null;
        objectWriter = null;
        if (messagesQueue != null) messagesQueue.clear();
    }

    public void startServer(Player.Color creatorColor) {
        try {
            try {
                socket = new ServerSocket(5678);
                connectionAlive = true;
            } catch (IOException e) {
                throw new CantCreateServerException(e);
            }

            System.out.println("Server: waiting for a client...");
            clientSocket = socket.accept();
            System.out.println("Server: client connected. Let's choose the color.");
            exchangeColorWithClient(creatorColor);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while waiting for a client connection.", e);
        }
    }

    private void exchangeColorWithClient(Player.Color creatorColor) {
        try {
            System.out.println("Server: Waiting for a client's color request...");

            clientSocket.setSoTimeout(100_000);
            objectReader = new ObjectInputStream(clientSocket.getInputStream());
            objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());

            Messages colorRequest = (Messages) objectReader.readObject();
            if(colorRequest.equals(Messages.COLOR_REQUEST))
                System.out.println("Server: Color request received.");

            System.out.println("Server: Send the color for the client.");
            objectWriter.writeObject(getColorResponse(creatorColor));
            objectWriter.flush();

            System.out.println("Server: Waiting for a client's confirmation message response.");

            Messages confirmResponse = (Messages) objectReader.readObject();
            if (confirmResponse.equals(Messages.COLOR_RECEIVE))
                System.out.println("Server: Client received the color.");

             readMessagesFuture = executorService.submit(getMessageQueueRunnable());

        }
        catch (SocketTimeoutException stx) {
            // todo: handle this probably as forcing reconnection with a client
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Messages getColorResponse(Player.Color createColor) {
        if(createColor.isWhite())
            return Messages.BLACK_COLOR_RESPONSE;
        else
            return Messages.WHITE_COLOR_RESPONSE;
    }

    public void close() {
        while (connectionAlive) {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                    clientSocket = null;
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    socket = null;
                }
                readMessagesFuture.cancel(true);
                executorService.shutdown();
                connectionAlive = false;
            } catch (IOException e) {
                throw new RuntimeException("Cannot close server socket.", e);
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

                }
                catch (IOException | ClassNotFoundException e) {
                    System.out.println("The creator socket stopped receiving messages from the client because of an error");
                    throw new RuntimeException(e);
                }
            }
        };
    }


}
