package org.chatApp.Client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

//class responsable sur l'affichage de l'interface pour ajouter des amis
public class ClientAddFriendsView extends JFrame {
    private Client client;
    private JList usersList;
    private DefaultListModel<String> usersModel = new DefaultListModel<>();
    private JButton addFriends;
    public ClientAddFriendsView(Client client,String[] users) {
        this.client = client;
        initialFrameSetup();
        usersList = new JList<>(usersModel);
        usersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setupListModel(users);
        setupLabel();
        setupList();
        setupButton();
    }
    private void initialFrameSetup(){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,600);
        setLayout(null);
        setLocationRelativeTo(null);
    }
    private void setupLabel(){
        JPanel upperPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Our app Users");
        JButton backButton = new JButton("Back");
        buttonStyle(backButton,new Dimension(100,50));
        label.setFont(new Font("Roboto",Font.BOLD,20));
        label.setForeground(Color.decode("#175676"));
        backButton.addActionListener((ae)->{
            new ClientChatGUI(this.client);
            dispose();
        });
        upperPanel.add(backButton);
        upperPanel.add(label);
        upperPanel.setBounds(0,0,300,70);
        add(upperPanel);
    }
    private void setupList(){
        setupListStyle();
        JPanel usersPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(usersPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        usersPanel.add(usersList);
        scrollPane.setBounds(0,70,200,550);
        add(scrollPane);
    }
    private void setupListStyle(){
        usersList.setFixedCellWidth(200);
        usersList.setFixedCellHeight(50);
        usersList.setBackground(Color.decode("#CCE6F4"));
        usersList.setFont(new Font("Roboto",Font.PLAIN,16));
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) usersList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        usersList.setCellRenderer(renderer);
    }
    private void setupButton(){
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addFriends = new JButton("Add");
        buttonStyle(addFriends,new Dimension(100,50));
        buttonPanel.setBounds(200,70,100,550);
        addFriends.addActionListener((ae)->{
            List selectedUsers = usersList.getSelectedValuesList();
            if(selectedUsers.size()==0){
                JOptionPane.showMessageDialog(this,"You must at least select one user to add!","Login Failed",JOptionPane.ERROR_MESSAGE);
            }else{
                this.client.addFriends(selectedUsers);
                JOptionPane.showMessageDialog(this,"Friends Added Successfully !");
                dispose();
            }
        });
        buttonPanel.add(addFriends);
        add(buttonPanel);
    }

    private void buttonStyle(JButton button,Dimension dimension){
        button.setFont(new Font("Roboto",Font.PLAIN,16));
        button.setBackground(Color.decode("#CCE6F4"));
        button.setForeground(Color.decode("#175676"));
        button.setPreferredSize(dimension);
    }
    private void setupListModel(String[] users){
        for (String user:users){
            usersModel.add(usersModel.getSize(),user);
        }
    }
}
