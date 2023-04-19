package dev.djigit.chessonevsone.sockets;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public abstract class PlayerSocket {
    final Logger LOG = Logger.getLogger(PlayerSocket.class.getName());
    final LinkedBlockingQueue<ImmutablePair<MessageType, String>> messagesQueue = new LinkedBlockingQueue<>();
    volatile boolean connectionAlive = false;
    ObjectInputStream objectReader;
    ObjectOutputStream objectWriter;
    ExecutorService executorService;

    public LinkedBlockingQueue<ImmutablePair<MessageType, String>> getMessagesQueue() {
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
                MessageType msg = null;
                try {
                    msg = (MessageType) objectReader.readObject();
                    if (msg.equals(MessageType.OPP_MOVE)) {
                        String move = (String) objectReader.readObject();
                        messagesQueue.put(ImmutablePair.of(msg, move));
                    } else {
                        messagesQueue.put(ImmutablePair.of(msg, null));
                    }
                } catch (InterruptedException e) {
                    LOG.warning("Can't put the message into queue. Message: " + msg);
                }
                catch (SocketTimeoutException ignored) {
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
