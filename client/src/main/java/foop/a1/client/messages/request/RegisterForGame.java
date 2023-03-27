package foop.a1.client.messages.request;

import foop.a1.client.messages.Message;

public class RegisterForGame implements Message {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
