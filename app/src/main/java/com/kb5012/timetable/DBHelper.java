package com.kb5012.timetable;

import java.util.ArrayList;

/**
 * Created by Ronald on 14-12-2015.
 */
public class DBHelper {
    // alleen gebruikt voor demo
    private ArrayList<Group> groups = new ArrayList<Group>();
    private ArrayList<User> users = new ArrayList<>();

    //ToDo database connection maken..
    public DBHelper() {
    }

    public static User userInlog(String username, String password) {
        // demo
        if (username.equals("username") && password.equals("password")) {
            User user = new User();
            user.setFirstName("test");
            user.setLastName("testen");
            return user;
        }
        // TODO hier user ophalen met wachtwoord en username van db als er geen is word er niks teruggegeven.
        return null;
    }

    public static User findUserById(int id) {
        //TODO hier de user uithalen
        //demo test
        User user = new User();
        user.setFirstName("test");
        user.setLastName("testen");
        return user;
    }

    public static ArrayList<Task> findAllTaskByUserId(int userId) {
        //TODO hier uit db halen alle taken van user.
        // demo code hier
        ArrayList<Task> tasks = new ArrayList<>();
        Task task;
        for (int i = 0; i < 10; i++) {
            task = new Task();
            task.setID(i);
            task.setName("taak " + i);
            task.setTaskMaker("maker " + i);
            task.setTaskUser("user " + i);
            tasks.add(task);
        }
        return tasks;
    }
}
