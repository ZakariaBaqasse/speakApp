package org.chatApp.Client;

import org.chatApp.Server.UserData;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class Client {
    private String username;
    private String password;
    private DatagramSocket socket;
    private static final int SERVER_PORT=2222;
    private static final InetAddress serverAddress;

    static {
        try {
            serverAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] buffer;

    public Client(String username, String password) {
        this.username = username;
        this.password = password;
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public String login(String username, String password){
        String request = "login,"+username+","+password+", EOF";
        String serverResponse;
        buffer = request.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer,0,buffer.length,serverAddress,SERVER_PORT);
        try {
            this.socket.send(packet);
            byte[] serverBuffer = new byte[1024];
            DatagramPacket loginPacket = new DatagramPacket(serverBuffer,0,serverBuffer.length);
            this.socket.receive(loginPacket);
            serverResponse = new String(loginPacket.getData()).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return serverResponse;
    }

    public String registerUser(String username, String password){
        String request = "register,"+username+","+password;
        String serverResponse;
        byte[] requestBuffer = request.getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestBuffer,0,requestBuffer.length,serverAddress,SERVER_PORT);
        try {
            this.socket.send(requestPacket);
            byte [] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer,0,responseBuffer.length);
            this.socket.receive(responsePacket);
            serverResponse = new String(responsePacket.getData()).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return serverResponse;
    }

    public void receiveMessages(){
        byte[] tmp = new byte[1024];
        DatagramPacket packet = new DatagramPacket(tmp,0,tmp.length);
        try {
            this.socket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String message = new String(packet.getData());
        System.out.println("You got a message: "+message.trim());
    }

    public void sendMessage(String message){
        String receiver = username.equals("zakaria") ? "baqasse":"zakaria";
        String request = "chat,"+receiver+","+message+", ";
        byte[] tmp = request.getBytes();
        DatagramPacket packet = new DatagramPacket(tmp,0,tmp.length,serverAddress,SERVER_PORT);
        try {
            this.socket.send(packet);
            System.out.println("request sent: "+request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
