package org.chatApp.Client;

import javax.swing.*;
import java.awt.*;

public class ClientRegisterView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    public ClientRegisterView(){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLayout(new FlowLayout());
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        registerButton = new JButton("Register");
        add(usernameField);
        add(passwordField);
        add(registerButton);
        registerButton.addActionListener((ae)->{
            Client client = new Client(usernameField.getText(),passwordField.getText());
            String registerState = client.registerUser(usernameField.getText(),passwordField.getText());
            if(registerState.equals("Account created successfuly !")){
                JOptionPane.showMessageDialog(this,registerState+" Now Login using your credentials");
                new ClientLoginView();
                dispose();
            }else{
                JOptionPane.showMessageDialog(this,registerState,"Register Failed",JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}
