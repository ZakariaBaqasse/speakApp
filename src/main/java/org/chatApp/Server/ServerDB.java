package org.chatApp.Server;

import java.sql.*;

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
}
