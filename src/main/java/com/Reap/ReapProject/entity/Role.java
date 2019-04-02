package com.Reap.ReapProject.entity;

import javax.persistence.Entity;
import javax.persistence.Transient;

public enum  Role {
    USER("User"),SUPERVISOR("Supervisor"),PRACTICE_HEAD("Practice Head"),ADMIN("Admin");

    private String value;

    Role(String value){
        this.value=value;
    }

}
