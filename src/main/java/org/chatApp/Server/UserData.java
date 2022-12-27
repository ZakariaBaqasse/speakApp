package org.chatApp.Server;

import java.net.InetAddress;

public class UserData {
    private String username;
    private String password;
    private int port;
    private InetAddress address;

    public UserData(String username, String password, int port, InetAddress address) {
        this.username = username;
        this.password = password;
        this.port = port;
        this.address = address;
    }

    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }


}
