package dev.djigit.chessonevsone.sockets;

import dev.djigit.chessonevsone.game.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameCreatorSocket {

    private static GameCreatorSocket CREATOR_SOCKET_INSTANCE = null;
    private ServerSocket socket;

    private Socket clientSocket;

    private GameCreatorSocket() {}

    public static GameCreatorSocket getInstance() {
        if (CREATOR_SOCKET_INSTANCE == null) {
            CREATOR_SOCKET_INSTANCE = new GameCreatorSocket();
        }
        return CREATOR_SOCKET_INSTANCE;
    }

    public void startServer(Player.Color creatorColor) {
        try {
            try {
                this.socket = new ServerSocket(5678);
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

            ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());

            Messages colorRequest = (Messages) reader.readObject();
            if(colorRequest.equals(Messages.COLOR_REQUEST))
                System.out.println("Server: Color request received.");

            System.out.println("Server: Send the color for the client.");
            writer.writeObject(getColorResponse(creatorColor));
            writer.flush();

            System.out.println("Server: Waiting for a client's confirmation message response.");

            Messages confirmResponse = (Messages) reader.readObject();
            if (confirmResponse.equals(Messages.COLOR_RECEIVE))
                System.out.println("Server: Client received the color.");

        } catch (IOException | ClassNotFoundException e) {
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
        try {
            if (!socket.isClosed())
                socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot close server socket.", e);
        }
    }


}
