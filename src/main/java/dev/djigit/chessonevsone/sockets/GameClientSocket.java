package dev.djigit.chessonevsone.sockets;

import dev.djigit.chessonevsone.game.Player;

import java.io.*;
import java.net.Socket;

public class GameClientSocket {
    private Socket socket;
    private Player.Color color;

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
        ObjectOutputStream writer;
        ObjectInputStream reader;

        writer = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Client: Request a color from the server.");
        writer.writeObject(Messages.COLOR_REQUEST);

        reader = new ObjectInputStream(socket.getInputStream());
        System.out.println("Client: Wait for the color response...");
        Messages colorResponse = (Messages) reader.readObject();
        setColor(colorResponse);

        writer = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Client: Send 'color receive' response to server.");
        writer.writeObject(Messages.COLOR_RECEIVE);
    }

    private void setColor(Messages colorMsg) {
        if (colorMsg.equals(Messages.WHITE_COLOR_RESPONSE))
            color = Player.Color.WHITE;
        else if (colorMsg.equals(Messages.BLACK_COLOR_RESPONSE))
            color = Player.Color.BLACK;
    }

    public Player.Color getColor() {
        return color;
    }
}
