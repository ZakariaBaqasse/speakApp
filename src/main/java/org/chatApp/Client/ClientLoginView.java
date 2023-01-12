package org.chatApp.Client;

import javax.swing.*;
import java.awt.*;

public class ClientLoginView extends JFrame {
  private Client client;
  private JTextField usernameField;
  private JPasswordField passwordField;

  private JLabel usernameLabel;
  private JLabel passwordLabel;
  private JLabel titleLabel;
  private JCheckBox showPassword;
  private JButton loginButton;
  private JButton backButton;
  public ClientLoginView(){
    initialFrameSetup();

    usernameField = new JTextField();
    passwordField = new JPasswordField();
    initiateTextFields(usernameField,300,200,300,40);
    initiateTextFields(passwordField,300,250,300,40);

    titleLabel = new JLabel("Login !");
    usernameLabel = new JLabel("Username");
    passwordLabel = new JLabel("Password");

    initiateLabels(usernameLabel,200,200,100,40,16,Font.PLAIN);
    initiateLabels(passwordLabel,200,250,100,40,16,Font.PLAIN);
    initiateLabels(titleLabel,370,100,300,40,36,Font.BOLD);

    showPassword = new JCheckBox("Show Password");
    initiateCheckbox(showPassword);
    showPassword.addActionListener((ae)->checkBoxListener());

    loginButton = new JButton("Login");
    initiateButton(loginButton,300,340);

    backButton = new JButton("Back");
    initiateButton(backButton,10,10);

    loginButton.addActionListener((ae)->loginListener());
    backButton.addActionListener((ae)->{
      new ClientWelcomeView();
      dispose();
    });
  }

  private void loginListener(){
    String username = usernameField.getText();
    String password = passwordField.getText();
    if(username.equals("")|| password.equals("")){
      JOptionPane.showMessageDialog(this,"Username and password are required Fields","Login Failed",JOptionPane.ERROR_MESSAGE);

    }else{
    this.client  = new Client(username,password);
    String loginState = this.client.login(usernameField.getText(),passwordField.getText());
    if(loginState.equals("Login successful")){
      JOptionPane.showMessageDialog(this,loginState);
      ClientChatGUI gui = new ClientChatGUI(this.client);
      dispose();
    }else{
      JOptionPane.showMessageDialog(this,loginState,"Login Failed",JOptionPane.ERROR_MESSAGE);
    }
    }
  }

  private void checkBoxListener(){
    if(showPassword.isSelected()){
      passwordField.setEchoChar((char) 0);
    }else{
      passwordField.setEchoChar('*');
    }
  }

  private void initiateTextFields(JComponent component,int x,int y,int width,int height){
    component.setPreferredSize(new Dimension(100,20));
    component.setBounds(x,y,width,height);
    this.add(component);
  }

  private void initiateLabels(JLabel label,int x,int y,int width,int height,int fontSize,int fontStyle){
    label.setBounds(x,y,width,height);
    label.setFont(new Font("Roboto",fontStyle,fontSize));
    label.setForeground(Color.decode("#175676"));
    this.add(label);
  }

  private void initiateButton(JButton button,int x,int y){
    button.setBounds(x,y,100,40);
    button.setBackground(Color.decode("#CCE6F4"));
    button.setForeground(Color.decode("#175676"));
    button.setFont(new Font("Roboto",Font.PLAIN,16));
    this.add(button);
  }

  private void initiateCheckbox(JCheckBox checkBox){
    checkBox.setBounds(300,305,300,20);
    checkBox.setForeground(Color.decode("#175676"));
    checkBox.setFont(new Font("Roboto",Font.PLAIN,16));
    this.add(checkBox);
  }

  private void initialFrameSetup(){
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800,600);
    setLayout(null);
    setLocationRelativeTo(null);
  }
}
