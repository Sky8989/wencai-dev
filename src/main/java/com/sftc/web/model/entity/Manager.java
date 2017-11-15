package com.sftc.web.model.entity;

import lombok.Getter;
import lombok.Setter;

public class Manager {

    @Setter @Getter
    private int id;

    @Setter @Getter
    private String username;

    @Setter @Getter
    private String password;

    @Setter @Getter
    private int level;
}
