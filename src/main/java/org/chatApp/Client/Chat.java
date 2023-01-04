package org.chatApp.Client;

import javax.swing.*;
import java.awt.*;

public class Chat {
    private Client client;
    private String receiver;
    private JList messages;
    private DefaultListModel<String> messagesModel = new DefaultListModel<>();
    private boolean isPanelOpen;
    private JPanel viewPanel;

    public Client getClient() {
        return client;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean isPanelOpen() {
        return isPanelOpen;
    }
    public Chat(Client client, String receiver){
        viewPanel = new JPanel(null);
        this.client=client;
        this.receiver = receiver;
        messages = new JList<>(messagesModel);
        isPanelOpen = false;
    }

    public JPanel invokePanel(){
        setupMessageList();
        JScrollPane scroll = new JScrollPane(messages);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(0,0,600,500);
        viewPanel.add(scroll);
        viewPanel.setBounds(200,0,600,600);
        viewPanel.setBackground(Color.decode("#CCE6F4"));
        viewPanel.setFont(new Font("Roboto",Font.PLAIN,16));
        viewPanel.setForeground(Color.decode("#175676"));
        setupMessagePanel();
        isPanelOpen = true;
        return viewPanel;
    }

    private void setupMessagePanel(){
        JPanel messagePanel = new JPanel(new FlowLayout());
        JButton sendButton = new JButton("send");
        sendButton.setPreferredSize(new Dimension(70,50));
        sendButton.setBackground(Color.decode("#CCE6F4"));
        JTextField messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(450,50));
        messageField.setFont(new Font("Roboto",Font.PLAIN,14));
        sendButton.addActionListener((ae)->{
            messagesModel.add(messagesModel.size(),"You: "+messageField.getText());
            this.client.sendMessage(messageField.getText(),this.receiver);
            messageField.setText("");
        });
        messagePanel.add(messageField);
        messagePanel.add(sendButton);
        messagePanel.setBackground(Color.decode("#CCE6F4"));
        messagePanel.setBounds(0,500,550,50);
        viewPanel.add(messagePanel);
    }

    private void setupMessageList(){
        messages.setFixedCellHeight(50);
        messages.setFixedCellWidth(600);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) messages.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        messages.setCellRenderer(renderer);
        messages.setFont(new Font("Roboto",Font.PLAIN,16));
        messages.setForeground(Color.decode("#175676"));
    }

    public void addMessage(String message){
        messagesModel.add(messagesModel.getSize(),message);
    }
}
