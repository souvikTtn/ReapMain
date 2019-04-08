package com.Reap.ReapProject.component;

public class SearchUser {
    private Integer currentUserId;

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "SearchUser{" +
                "currentUserId=" + currentUserId +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
