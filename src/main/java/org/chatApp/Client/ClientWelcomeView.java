package org.chatApp.Client;

import javax.swing.*;
import java.awt.*;

public class ClientWelcomeView extends JFrame {
    private JButton login;
    private JButton register;
    private JLabel welcome;
    public ClientWelcomeView(){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLayout(new FlowLayout());
        login = new JButton("Login");
        register = new JButton("register");
        welcome = new JLabel("Welcome !");
        add(welcome);
        add(login);
        add(register);
        login.addActionListener((ae)->{
            new ClientLoginView();
            dispose();
        });
        register.addActionListener((ae)->{
            new ClientRegisterView();
            dispose();
        });
    }
}
