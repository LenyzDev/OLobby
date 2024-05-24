package me.lenyz.olobby.objects;

public class User {

    private String queue;
    private boolean inArena;

    public User(String queue, boolean inArena){
        this.queue = queue;
        this.inArena = inArena;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public boolean isInArena() {
        return inArena;
    }

    public void setInArena(boolean inArena) {
        this.inArena = inArena;
    }
}
