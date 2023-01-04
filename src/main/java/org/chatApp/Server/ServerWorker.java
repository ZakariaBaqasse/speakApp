package org.chatApp.Server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.sql.SQLException;
import java.util.List;

public class ServerWorker extends Thread{
    private DatagramPacket packet;
    private DatagramSocket socket;
    private byte[] tmp;
    private SessionTracker session;
    private ServerDB database;
    public ServerWorker(DatagramPacket packet,DatagramSocket socket,SessionTracker tracker,ServerDB database) {
        this.socket = socket;
        this.packet = packet;
        this.database = database;
        tmp = new byte[1024];
        session = tracker;
    }

    public synchronized void userLogin(String username, String password, int port, InetAddress address) {
        boolean valideCredentials;
        String loginResponse;
        try {
             valideCredentials = database.verifyCredentials(username,password,true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(valideCredentials){
            UserData data = new UserData(username,password,port,address);
            session.addUser(data);
            loginResponse = "Login successful";
            notifyFriends(username,"newLoggedFriend");
        }else{
            loginResponse = "Bad credentials ! Could not connect";
        }
        byte[] loginBuffer = loginResponse.getBytes();
        DatagramPacket loginPacket = new DatagramPacket(loginBuffer,0,loginBuffer.length,address,port);
        try {
            this.socket.send(loginPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void handleLogout(String username) {
        session.removeUserSession(username);
        notifyFriends(username,"logoutFriend");
    }

    private void notifyFriends(String username,String state){
        UserData[] friends = this.session.getOnlineFriends(username);
        if(friends!=null){
        String notification = state+","+username;
        for(UserData user:friends){
            byte[] buffer = notification.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer,0,buffer.length,user.getAddress(),user.getPort());
            try {
                this.socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        }
    }

    public void userRegister(String username,String password,int port,InetAddress address){
        boolean userAdded = database.saveUser(username,password);
        String registerResponse = userAdded ? "Account created successfuly !":"Username already exist please try another one !";
        byte[] buffer = registerResponse.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer,0,buffer.length,address,port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void receiveAndTransfer(String message,UserData receiver,String sender) {
        String reply = "newMessage,"+sender+","+message;
        tmp = reply.getBytes();
        DatagramPacket replyPacket = new DatagramPacket(tmp,0,tmp.length,receiver.getAddress(), receiver.getPort());
        try {
            this.socket.send(replyPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendOnlineFriends(UserData[] users){
        StringBuilder response = new StringBuilder("");
        if(users == null){
            response = new StringBuilder("noUsers,");
        }else{
        for (UserData user:users){
            response.append(user.getUsername()).append(",");
        }
        byte[] responseBuffer = response.toString().getBytes();
        DatagramPacket responsePacket = new DatagramPacket(responseBuffer,0,responseBuffer.length,this.packet.getAddress(),this.packet.getPort());
        try {
            this.socket.send(responsePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }
    }
    public void run(){
        String clientRequest = new String(this.packet.getData()).trim();
        String[] data = clientRequest.split(",");
        String command = data[0];
        switch (command){
            case "login": {
                System.out.println("Login request received");
                userLogin(data[1], data[2], this.packet.getPort(), this.packet.getAddress());
                break;
            }
            case "chat":{
                System.out.println("Chat request received");
                UserData receiver = session.getUser(data[2]);
                receiveAndTransfer(data[3],receiver,data[1]);
                break;
            }
            case "register":{
                System.out.println("Register request received");
                userRegister(data[1],data[2],this.packet.getPort(),this.packet.getAddress());
                break;
            }
            case "getOnFriends":{
                System.out.println("Getting online Friends");
                UserData[] friends = this.session.getOnlineFriends(data[1]);
                sendOnlineFriends(friends);
                break;
            }
            case "logout":{
                handleLogout(data[1]);
                break;
            }
            default:{
                System.out.println("Command not found");
            }
        }
    }


}

