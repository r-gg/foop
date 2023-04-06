package foop.a1.server.messages.request;

import foop.a1.server.messages.Message;

public class RegisterForGame implements Message {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
