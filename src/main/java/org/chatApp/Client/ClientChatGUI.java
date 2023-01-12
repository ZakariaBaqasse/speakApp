package org.chatApp.Client;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;


//class qui englobe tous le logique de l'interface de chat
public class ClientChatGUI extends JFrame {

    private Client client;
    private JPanel friendsPanel;
    private JList friendsList;
    private DefaultListModel<String> friendsModel = new DefaultListModel<>();
    private LinkedList<Chat> chats;
    private JLabel onlineFriendsLabel;

    private JLabel receiver;
    public ClientChatGUI(Client client){
        this.client = client;
        chats = new LinkedList<>();
        initialFrameSetup();
        setupFriendsList();
        friendsPanelSetup();
        setupMenu();
        friendsList.addListSelectionListener((lse)->{
            if(friendsList.getSelectedValue()!=""){
                Chat userChat = chats.stream().filter((chat)->chat.getReceiver().equals(friendsList.getSelectedValue())).findFirst().get();
                add(userChat.invokePanel());
            }
        });
        displayMessages();
        this.client.getLoggedInFriends();
    }

    private void setupMenu(){
       JMenuBar menuBar = new JMenuBar();
       JMenu menu = new JMenu("More Actions");
       JMenuItem addFriends = new JMenuItem("Add Friends");
       JMenuItem logout = new JMenuItem("Logout");
       logout.addActionListener((ae)-> {
           this.client.logout();
           new ClientWelcomeView();
           dispose();
       });
       addFriends.addActionListener((ae)-> {
           this.client.getUsers();
       });
       menu.add(addFriends);
       menu.add(logout);
       menuBar.add(menu);
       setJMenuBar(menuBar);
       JMenu username = new JMenu(this.client.getUsername());
       menuBar.add(username);
    }
    private void displayMessages(){
        Runnable reception = ()->{
            while (true){
                this.client.receiveResponses(this);
            }
        };
        Thread receptionThread = new Thread(reception);
        receptionThread.start();
    }

    public void notifyNewMessage(String sender, String message) {
        Chat userChat = chats.stream().filter((chat)->chat.getReceiver().equals(sender)).findFirst().get();
        userChat.addMessage(sender+": "+message);
        if(!userChat.isPanelOpen()){
            JOptionPane.showMessageDialog(this,"You got a new message from "+sender);
        }
    }

    public void displayNewFriend(String friend) {
        if(!friendsModel.contains(friend)){
            friendsModel.add(friendsModel.getSize(),friend);
            Chat chat = new Chat(this.client,friend);
            chats.add(chat);
        }
    }

    public void removeActiveFriend(String friend) {
        for (int i = 0;i<friendsModel.getSize();i++){
            if(friendsModel.getElementAt(i).equals(friend)){
                JOptionPane.showMessageDialog(this,friend+" left the chat !");
                Chat userChat = chats.stream().filter((chat)->chat.getReceiver().equals(friend)).findFirst().get();
                if(userChat.isPanelOpen()){
                    friendsList.clearSelection();
                    userChat.closePanel(this);
                }
                friendsModel.remove(i);
            }
        }
    }

    private void initialFrameSetup(){
        this.client = client;
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLayout(null);
        setLocationRelativeTo(null);
    }

    private void friendsPanelSetup(){
        onlineFriendsLabel = new JLabel("Online Friends");
        onlineFriendsLabel.setFont(new Font("Roboto",Font.BOLD,20));
        onlineFriendsLabel.setBackground(Color.decode("#9ee37d"));
        onlineFriendsLabel.setForeground(Color.decode("#175676"));
        onlineFriendsLabel.setBounds(0,0,200,50);
        this.friendsPanel = new JPanel();
        this.friendsPanel.add(onlineFriendsLabel);
        JScrollPane scrollableList = new JScrollPane(this.friendsPanel);
        scrollableList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.friendsPanel.setPreferredSize(new Dimension(200,600));
        this.friendsPanel.add(friendsList);
        scrollableList.setBounds(0,0,200,550);
        add(scrollableList);
    }

    private void setupFriendsList(){
        friendsList = new JList<>(friendsModel);
        friendsList.setFixedCellWidth(200);
        friendsList.setFixedCellHeight(50);
        friendsList.setBackground(Color.decode("#CCE6F4"));
        friendsList.setFont(new Font("Roboto",Font.PLAIN,16));
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) friendsList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        friendsList.setCellRenderer(renderer);
    }

    public void displayOnlineFriend(String[] friendsUsernames){
        System.out.println(Arrays.toString(friendsUsernames));
        if(!friendsUsernames[0].equals("noFriends")){
           for(String username:friendsUsernames){
               friendsModel.add(friendsModel.getSize(),username);
               Chat chat = new Chat(this.client,username);
               chats.add(chat);
           }
        }
    }

}


