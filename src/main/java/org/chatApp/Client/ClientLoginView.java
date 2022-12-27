package org.chatApp.Client;

import javax.swing.*;
import java.awt.*;

public class ClientLoginView extends JFrame {
  private Client client;
  private JTextField usernameField;
  private JPasswordField passwordField;

  private JButton loginButton;
  public ClientLoginView(){
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500,500);
    setLayout(new FlowLayout());
    usernameField = new JTextField("Username Goes here...",20);
    passwordField = new JPasswordField("Password goes here...",20);
    loginButton = new JButton("Login");
    add(usernameField);
    add(passwordField);
    add(loginButton);
    loginButton.addActionListener((ae)->{
      String username = usernameField.getText();
      String password = passwordField.getText();
      this.client  = new Client(username,password);
      String loginState = this.client.login(usernameField.getText(),passwordField.getText());
      if(loginState.equals("Login successful")){
          JOptionPane.showMessageDialog(this,loginState);
          new ClientChatGUI(this.client);
          dispose();
      }else{
          JOptionPane.showMessageDialog(this,loginState,"Login Failed",JOptionPane.ERROR_MESSAGE);
      }
    });
  }
}
