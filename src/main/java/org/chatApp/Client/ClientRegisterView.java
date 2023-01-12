package org.chatApp.Client;

import javax.swing.*;
import java.awt.*;

public class ClientRegisterView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel titleLabel;
    private JCheckBox showPassword;
    private JButton backButton;
    public ClientRegisterView(){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLayout(null);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Register !");
        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");

        initiateLabels(usernameLabel,200,200,100,40,16,Font.PLAIN);
        initiateLabels(passwordLabel,200,250,100,40,16,Font.PLAIN);
        initiateLabels(titleLabel,370,100,300,40,36,Font.BOLD);


        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        initiateTextFields(usernameField,300,200,300,40);
        initiateTextFields(passwordField,300,250,300,40);

        showPassword = new JCheckBox("Show Password");
        initiateCheckbox(showPassword);
        showPassword.addActionListener((ae)->checkBoxListener());

        registerButton = new JButton("Register");
        initiateButton(registerButton,300,340);

        backButton = new JButton("Back");
        initiateButton(backButton,10,10);

        registerButton.addActionListener((ae)->registerListener());
        backButton.addActionListener((ae)->{
            new ClientWelcomeView();
            dispose();
        });
    }

    private void registerListener(){
        if(usernameField.getText().equals("")||passwordField.getText().equals("")){
            JOptionPane.showMessageDialog(this,"Username and Password are required to complete registration","Register Failed",JOptionPane.ERROR_MESSAGE);
        }else{
            Client client = new Client(usernameField.getText(),passwordField.getText());
            String registerState = client.registerUser(usernameField.getText(),passwordField.getText());
            if(registerState.equals("Account created successfuly !")){
                JOptionPane.showMessageDialog(this,registerState+" Now Login using your credentials");
                new ClientLoginView();
                dispose();
            }else{
                JOptionPane.showMessageDialog(this,registerState,"Register Failed",JOptionPane.ERROR_MESSAGE);
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

}
