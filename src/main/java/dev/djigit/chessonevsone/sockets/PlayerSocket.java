package dev.djigit.chessonevsone.sockets;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public abstract class PlayerSocket {
    final Logger LOG = Logger.getLogger(PlayerSocket.class.getName());
    final ConcurrentLinkedQueue<ImmutablePair<MessageType, String>> messagesQueue = new ConcurrentLinkedQueue<>();
    volatile boolean connectionAlive = false;
    ObjectInputStream objectReader;
    ObjectOutputStream objectWriter;
    ExecutorService executorService;

    public ConcurrentLinkedQueue<ImmutablePair<MessageType, String>> getMessagesQueue() {
        return messagesQueue;
    }

    public boolean isConnectionAlive() {
        return connectionAlive;
    }

    abstract public void close();

    public abstract void sendMessage(MessageType msgType, String msg);

    void cleanUp() {
        close();
        objectReader = null;
        objectWriter = null;
        messagesQueue.clear();
        executorService = Executors.newSingleThreadExecutor();
    }

    Runnable getMessageQueueRunnable() {
        return () -> {
            while (connectionAlive) {
                try {
                    MessageType msg = (MessageType) objectReader.readObject();
                    if (msg.equals(MessageType.OPP_MOVE)) {
                        String move = (String) objectReader.readObject();
                        messagesQueue.add(ImmutablePair.of(msg, move));
                    } else {
                        messagesQueue.add(ImmutablePair.of(msg, null));
                    }
                } catch (SocketTimeoutException ignored) {
                    LOG.warning("Socket's read timeout occurred.");
                }
                catch (IOException e) {
                    LOG.warning("Can't read opponent's message.");
                    throw new RuntimeException(e);
                }
                catch (ClassNotFoundException e) {
                    LOG.warning("Can't read opponent's message type.");
                    throw new RuntimeException(e);
                } catch (NullPointerException e) {
                    LOG.warning("NPE occurred while read a message from socket.");
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
