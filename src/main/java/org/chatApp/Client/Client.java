package org.chatApp.Client;


import java.io.IOException;

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

    public String getUsername() {
        return username;
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

    public String[] getLoggedInFriends(){
        String request = "getOnFriends,"+this.username;
        String[] onlineFriends;
        byte[] requestBuffer = request.getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestBuffer,0,requestBuffer.length,serverAddress,SERVER_PORT);
        try {
            socket.send(requestPacket);
            byte[] responseBuffer = new byte[3000];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer,responseBuffer.length);
            socket.receive(responsePacket);
            if(new String(responsePacket.getData()).trim().split(",")[0]=="noUsers"){
                onlineFriends = null;
            }else{
            onlineFriends = new String(responsePacket.getData()).trim().split(",");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return onlineFriends;
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

    public void receiveMessages(String sender,String message,ClientChatGUI messageFrame){
        messageFrame.notifyNewMessage(sender,message);
    }

    private void newOnlineFriend(String friend,ClientChatGUI chatGUI) {
        chatGUI.displayNewFriend(friend);
    }

    public void sendMessage(String message,String receiver){
        String request = "chat,"+this.username+","+receiver+","+message+", ";
        byte[] tmp = request.getBytes();
        DatagramPacket packet = new DatagramPacket(tmp,0,tmp.length,serverAddress,SERVER_PORT);
        try {
            this.socket.send(packet);
            System.out.println("request sent: "+request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void receiveResponses(ClientChatGUI chatGUI){
        byte[] tmp = new byte[1024];
        DatagramPacket packet = new DatagramPacket(tmp,0,tmp.length);
        try {
            this.socket.receive(packet);
            String[] response = new String(packet.getData()).trim().split(",");
            String respnseType = response[0];
            switch (respnseType){
                case "newMessage":{
                    receiveMessages(response[1],response[2],chatGUI);
                    break;
                }
                case "newLoggedFriend":{
                    newOnlineFriend(response[1],chatGUI);
                    break;
                }
                case "logoutFriend":{
                    loggedOutFriend(response[1],chatGUI);
                    break;
                }
                default:
                    System.out.println("Unknown request");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loggedOutFriend(String friend, ClientChatGUI chatGUI) {
        chatGUI.removeActiveFriend(friend);
    }

    public void logout(){
        String request = "logout,"+this.username;
        byte[] buffer = request.getBytes();
        DatagramPacket requsetLogout = new DatagramPacket(buffer,0,buffer.length,serverAddress,SERVER_PORT);
        try {
            this.socket.send(requsetLogout);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
