package dev.djigit.chessonevsone.sockets;

import dev.djigit.chessonevsone.game.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class GameClientSocket {
    private static GameClientSocket CLIENT_SOCKET_INSTANCE = null;
    private Socket socket;
    private Consumer<Player.Color> setColorConsumer;

    private GameClientSocket() {}

    public static GameClientSocket getInstance() {
        if (CLIENT_SOCKET_INSTANCE == null) {
            CLIENT_SOCKET_INSTANCE = new GameClientSocket();
        }
        return CLIENT_SOCKET_INSTANCE;
    }

    public void setSetColorConsumer(Consumer<Player.Color> setColorConsumer) {
        this.setColorConsumer = setColorConsumer;
    }

    public void connect() {
        try {
            socket = new Socket((String) null, 5678);
            System.out.println("Client: connection established.");

            requestColorFromServer();
        } catch (IOException e) {
            throw new RuntimeException("Can't connect to server.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void requestColorFromServer() throws IOException, ClassNotFoundException {
        ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());

        System.out.println("Client: Request a color from the server.");
        writer.writeObject(Messages.COLOR_REQUEST);
        writer.flush();

        System.out.println("Client: Wait for the color response...");
        Messages colorResponse = (Messages) reader.readObject();
        setColorForClient(colorResponse);

        System.out.println("Client: Send 'color receive' response to server.");
        writer.writeObject(Messages.COLOR_RECEIVE);
        writer.flush();
    }

    private void setColorForClient(Messages colorMsg) {
        if (colorMsg.equals(Messages.WHITE_COLOR_RESPONSE))
            setColorConsumer.accept(Player.Color.WHITE);
        else if (colorMsg.equals(Messages.BLACK_COLOR_RESPONSE))
            setColorConsumer.accept(Player.Color.BLACK);
    }
}
