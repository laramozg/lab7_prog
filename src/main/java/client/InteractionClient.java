package client;

import utility.Answer;
import utility.Request;
import utility.UserData;
import utility.element.Worker;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class InteractionClient {
    DatagramChannel channel;
    UserData userData;
    InetSocketAddress address;
    private static final int BUFFER_SIZE = 4096;

    public InteractionClient(String address, int port) {
        try {
            channel = DatagramChannel.open();
            this.address = new InetSocketAddress(address, port);
            channel.connect(this.address);
            channel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void sendRequest(Request request) throws IOException {
        request.setUserData(userData);
        try {
            Worker worker = request.getCommand().getElement();
            worker.setCreator(userData.getLogin());
        } catch (NullPointerException ignored) {
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(request);
        byte[] serializedRequest = byteStream.toByteArray();
        ByteBuffer message = ByteBuffer.wrap(serializedRequest);
        channel.write(message);
    }


    public Answer getAnswer() throws IOException {
        Answer answer = null;
        try {
            ByteArrayOutputStream objectCollector = new ByteArrayOutputStream();
            ByteBuffer transportBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (true) {
                SocketAddress receiveFlag = channel.receive(transportBuffer);
                if (receiveFlag != null) {
                    break;
                }
            }
            objectCollector.write(transportBuffer.array());
            transportBuffer.clear();
            channel.receive(transportBuffer);
            byte[] serializedAnswer = objectCollector.toByteArray();
            ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(serializedAnswer));
            answer = (Answer) input.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return answer;
    }
}
