package dev.djigit.chessonevsone.sockets;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class PlayerSocket {
    final ConcurrentLinkedQueue<ImmutablePair<Messages, String>> messagesQueue = new ConcurrentLinkedQueue<>();
    volatile boolean connectionAlive = false;

    public ConcurrentLinkedQueue<ImmutablePair<Messages, String>> getMessagesQueue() {
        return messagesQueue;
    }

    public boolean isConnectionAlive() {
        return connectionAlive;
    }

    abstract public void close();
}
