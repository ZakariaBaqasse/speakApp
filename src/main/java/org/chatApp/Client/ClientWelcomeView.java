package org.chatApp.Client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


//class qui affiche l'interface de bienvenue
public class ClientWelcomeView extends JFrame {
    private JButton login;
    private JButton register;
    private JLabel welcome;
    private JLabel logo = new JLabel();
    private JPanel welcomePanel;
    private JPanel buttonsPanel;
    public ClientWelcomeView(){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,600);
        GridBagConstraints panelConstraints = new GridBagConstraints();
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setLocationRelativeTo(null);

        //panels
        welcomePanel = new JPanel(new FlowLayout());
        buttonsPanel = new JPanel(new GridBagLayout());

        logo.setIcon(new ImageIcon(this.getClass().getResource("/logo.png")));

        welcome = new JLabel("Welcome To SpeakApp!");
        welcome.setFont(new Font("Roboto",Font.BOLD,36));
        welcome.setForeground(Color.decode("#175676"));
        welcomePanel.add(welcome);
        welcomePanel.add(logo);

        login = new JButton("Login");
        initiateButton(login);

        register = new JButton("Register");
        initiateButton(register);

        panelConstraints.fill = GridBagConstraints.HORIZONTAL;
        panelConstraints.anchor = GridBagConstraints.LINE_START;
        panelConstraints.weightx = 1.0;
        panelConstraints.gridwidth = 2;
        panelConstraints.insets = new Insets(0,10,0,10);
        buttonsPanel.add(login,panelConstraints);
        panelConstraints.anchor = GridBagConstraints.LINE_END;
        buttonsPanel.add(register,panelConstraints);

        add(welcomePanel,BorderLayout.NORTH);
        add(buttonsPanel,BorderLayout.CENTER);

        login.addActionListener((ae)->{
            new ClientLoginView();
            dispose();
        });
        register.addActionListener((ae)->{
            new ClientRegisterView();
            dispose();
        });

    }

    private void initiateButton(JButton button){
        button.setPreferredSize(new Dimension(50,50));
        button.setBackground(Color.decode("#CCE6F4"));
        button.setForeground(Color.decode("#175676"));
        button.setFont(new Font("Roboto",Font.PLAIN,16));
    }
}

