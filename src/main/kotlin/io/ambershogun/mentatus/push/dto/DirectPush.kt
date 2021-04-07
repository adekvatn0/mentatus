package io.ambershogun.mentatus.push.dto;

import javax.validation.constraints.NotBlank;

public class DirectPush {

    @NotBlank
    private String text;

    @NotBlank
    private String username;

    public void setText(String text) {
        this.text = text;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }
}
