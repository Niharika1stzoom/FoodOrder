package com.zoom.happiestplaces.model;

import java.util.Date;
import java.util.UUID;

public class Issue {
    private UUID user;
    String text,topic;
    Boolean resolved;
    Date create_on;

    public Issue(UUID user, String text, String topic) {
        this.user = user;
        this.text = text;
        this.topic = topic;
    }

    public UUID getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getTopic() {
        return topic;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public Date getCreate_on() {
        return create_on;
    }
}


