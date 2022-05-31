package server;

import utility.Answer;
import utility.Request;


import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class InteractionServer {
    private InetAddress address;
    private int port;
    private static final DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket(new InetSocketAddress(3011));
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private static final int BUFFER_SIZE = 4096;

    public Request getRequest() {
        Request request = null;
        try {
            DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            socket.receive(packet);
            this.address = packet.getAddress();
            this.port = packet.getPort();
            ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            request = (Request) input.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return request;
    }


    public void sendAnswer(Answer answer) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(out);
            output.writeObject(answer);
            byte[] serializedAnswer = out.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(serializedAnswer);
            buffer.get(serializedAnswer);
            DatagramPacket packet = new DatagramPacket(serializedAnswer, serializedAnswer.length, address, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
