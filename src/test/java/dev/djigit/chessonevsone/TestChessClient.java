package dev.djigit.chessonevsone;

import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.sockets.Messages;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TestChessClient {
    private static final int PORT = 5678;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static Socket clientSocket;

    private static void connect() throws IOException, ClassNotFoundException {
        System.out.println("Client: Request a color from the server.");
        out.writeObject(Messages.COLOR_REQUEST);
        out.flush();

        System.out.println("Client: Wait for the color response...");
        Messages colorResponse = (Messages) in.readObject();
        System.out.println("We are: " + colorResponse);

        System.out.println("Client: Send 'color receive' response to server.");
        out.writeObject(Messages.COLOR_RECEIVE);
        out.flush();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        clientSocket = new Socket((String) null, PORT);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());

        connect();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter coords: ");
            String line = sc.nextLine();
            String[] coords = line.split(" ");
            CellModel.Coords from = CellModel.Coords.valueOf(coords[0]);
            CellModel.Coords to = CellModel.Coords.valueOf(coords[1]);

            out.writeObject(Messages.OPP_MOVE);
            out.writeObject(line);

            System.out.printf("You entered from: %s%n", from);
            System.out.printf("You entered to: %s%n", to);
        }

    }
}
