package me.lenyz.olobby.storages;

import me.lenyz.olobby.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserStorage {

    private Map<UUID, User> users;

    public UserStorage(){
        this.users = new HashMap<>();
    }

    public User getUser(UUID owner){
        if(!users.containsKey(owner)){
            users.put(owner, new User(null, false));
        }
        return users.get(owner);
    }

    public void addUser(UUID owner, User user){
        users.put(owner, user);
    }

    public void removeUser(UUID owner){
        users.remove(owner);
    }

    public boolean containsUser(UUID owner){
        return users.containsKey(owner);
    }


}
