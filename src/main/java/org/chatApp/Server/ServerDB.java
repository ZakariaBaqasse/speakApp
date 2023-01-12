package org.chatApp.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ServerDB {
    private String dbUsername;
    private String dbPassword;
    private String connectionString;
    private Connection connection;

    public ServerDB(String dbUsername, String dbPassword, String connectionString) throws SQLException {
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.connectionString = connectionString;
        this.connection = DriverManager.getConnection(this.connectionString,this.dbUsername,this.dbPassword);
    }

    public boolean verifyCredentials(String username,String password,boolean verifyPassword) throws SQLException {
        boolean userExist = true;
        String query = verifyPassword? "select username,password from users where username=? and password = ?":"select username from users where username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        if(verifyPassword){
            statement.setString(1,username);
            statement.setString(2,password);
        }else{
            statement.setString(1,username);
        }
        ResultSet set = statement.executeQuery();
        if(!set.next()){
            userExist = false;
        }
        return userExist;
    }

    public boolean saveUser(String username,String password){
        boolean userSaved = false;
        try {
            boolean usernameDuplicate = verifyCredentials(username,password,false);
            if(!usernameDuplicate){
                String query = "insert into `users` (`username`,`password`) values (?,?)";
                PreparedStatement insertStmt = connection.prepareStatement(query);
                insertStmt.setString(1,username);
                insertStmt.setString(2,password);
                insertStmt.executeUpdate();
                userSaved = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userSaved;
    }

    public List<String> getUserFriends(String username){
        List<String> friends = new ArrayList<>();
        String query = "select * from friendships where user=? or friend=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            statement.setString(2,username);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                if(set.getString("user").equals(username)){
                    friends.add(set.getString("friend"));
                }else{
                    friends.add(set.getString("user"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friends;
    }

    public boolean addFriends(String username,String[] friends){
        boolean addedFriends = true;
        List<String> userFriends = getUserFriends(username);
        String query = "insert into friendships (user,friend) values (?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            Stream.of(friends).filter((friend)->!userFriends.contains(friend))
                               .forEach((friend)->helper(statement,username,friend,addedFriends));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return addedFriends;
    }
    private void helper(PreparedStatement statement,String username,String friend,boolean addedFriends){
        try {
                statement.setString(1,username);
                statement.setString(2,friend);
                if(statement.executeUpdate()==0){
                    addedFriends = false;
                }
        }catch (SQLException exception){
            exception.printStackTrace();
        }
    }
    public String getAllUsers(String username){
        String users = "";
        String query = "select username from users where username != ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                users+=set.getString("username")+"/";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
