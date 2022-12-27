package org.chatApp.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionTracker {
    private List<UserData>users;
    public SessionTracker(){
        users = new ArrayList<UserData>();
    }

    public void addUser(UserData user){
        users.add(user);
    }

    public UserData getUser(String username){
        Optional<UserData> optional = users.stream().filter((user)->user.getUsername().equals(username)).findFirst();
        return optional.get();
    }

    public List<UserData> getUsers() {
        return users;
    }
}