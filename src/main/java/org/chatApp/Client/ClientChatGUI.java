package org.chatApp.Client;

import javax.swing.*;
import java.awt.*;

public class ClientChatGUI extends JFrame {
    private JButton sendButton;
    private JTextField messageField;
    private Client client;
    public ClientChatGUI(Client client){
        this.client = client;
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLayout(new FlowLayout());
        sendButton = new JButton("Send");
        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(200,30));
        add(messageField);
        add(sendButton);
        sendButton.addActionListener((ae)->{
            this.client.sendMessage(messageField.getText());
        });
        displayMessages();
    }
    public void displayMessages(){
        Runnable reception = ()->{
            while (true){
                this.client.receiveMessages();
            }
        };
        Thread receptionThread = new Thread(reception);
        receptionThread.start();
    }
}
