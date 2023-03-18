package dev.djigit.chessonevsone.sockets;

import dev.djigit.chessonevsone.game.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameCreatorSocket {

    private ServerSocket socket;

    public GameCreatorSocket() {
        try {
            this.socket = new ServerSocket(5678);
        } catch (IOException e) {
            throw new RuntimeException("Can't create server socket.", e);
        }
    }

    public void startServer(Player.Color creatorColor) {
        try {
            System.out.println("Server: waiting for a client...");
            Socket clientSocket = socket.accept();
            System.out.println("Server: client connected. Let's choose the color.");
            exchangeColorWithClient(clientSocket, creatorColor);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while waiting for a client connection.", e);
        }
    }

    private void exchangeColorWithClient(Socket clientSocket, Player.Color creatorColor) {
        try {
            System.out.println("Server: Waiting for a client's color request...");

            ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
            Messages colorRequest = (Messages) reader.readObject();
            if(colorRequest.equals(Messages.COLOR_REQUEST))
                System.out.println("Server: Color request received.");

            System.out.println("Server: Send the color for the client.");
            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            writer.writeObject(getColorResponse(creatorColor));

            System.out.println("Server: Waiting for a client's confirmation message response.");

            reader = new ObjectInputStream(clientSocket.getInputStream());
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


}
