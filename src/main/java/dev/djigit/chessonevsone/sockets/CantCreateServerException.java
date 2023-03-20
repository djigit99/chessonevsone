package dev.djigit.chessonevsone.sockets;

public class CantCreateServerException extends RuntimeException {
    public CantCreateServerException(Throwable cause) {
        super("Check if the port 5678 is busy on your machine.", cause);
    }
}
