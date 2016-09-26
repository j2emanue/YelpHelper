package com.example.j2emanue.myyelphelperapp.Events;

/**
 * Created by j2emanue on 9/24/16.
 */

public class TokenRecievedEvent extends BaseEvent {

    private boolean isSuccess;
    private String reason;
    private String token;

    public TokenRecievedEvent(boolean isSuccess, String token, String reason) {
        this.isSuccess = isSuccess;
        this.token = token;
        this.reason = reason;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getReason() {
        return reason;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
