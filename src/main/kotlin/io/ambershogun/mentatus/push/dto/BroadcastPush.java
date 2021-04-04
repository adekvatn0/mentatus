package io.ambershogun.mentatus.push.dto;

import javax.validation.constraints.NotBlank;

public class BroadcastPush {

    @NotBlank
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
