package org.chatApp.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;

public class ServerMain {
    public static void main(String[] args) {
        int SERVER_PORT = 2222;
        String DB_USER = "root";
        String DB_PASSWORD = "";
        String CONNECTION_STRING = "jdbc:mysql://localhost:3306/speakapp_chat";
        try {
            DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
            System.out.println("Server is waiting on port: "+SERVER_PORT);
            ServerDB database = new ServerDB(DB_USER,DB_PASSWORD,CONNECTION_STRING);
            SessionTracker sessionTracker = new SessionTracker(database);
            while (true){
                byte[] requestBuffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(requestBuffer,0,requestBuffer.length);
                serverSocket.receive(packet);
                ServerWorker worker = new ServerWorker(packet,serverSocket,sessionTracker,database);
                worker.start();
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
