package foop.a1.server.messages.request;

import foop.a1.server.messages.Message;

public class GetGames implements Message {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
