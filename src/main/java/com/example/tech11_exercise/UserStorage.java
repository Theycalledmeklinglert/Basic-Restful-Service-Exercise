package com.example.tech11_exercise;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserStorage {

    private static UserStorage instance;
    private Map<String, User> users = new HashMap<>();

    private UserStorage() {}

    public static UserStorage getInstance() {
        if (instance == null) {
            instance = new UserStorage();
        }
        return instance;
    }

    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }

    public User getUser(String email) {
        return users.get(email);
    }

    public void updateUser(User user) {
        users.put(user.getEmail(), user);
    }

    // this should be protected by crosschecking the password of the user or by checking if the requester has admin rights
    // however as the service does not need to be protected/authenticated currently this is enough for now
    public void deleteUser(User user) {
        users.remove(user.getEmail());
    }

    //for testing only
    public Collection<User> getAllUsers() {
        return users.values();
    }

}
