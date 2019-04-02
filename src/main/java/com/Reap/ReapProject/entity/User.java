package com.Reap.ReapProject.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Arrays;
import java.util.List;

@Entity(name = "user")
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstName")
    @NotBlank(message = "cannot be a blank field")
    @NotNull
    private String firstName;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", image=" + Arrays.toString(image) +
                ", password='" + password + '\'' +
                ", gold=" + gold +
                ", silver=" + silver +
                ", bronze=" + bronze +
                ", roleList=" + roleList +
                ", active=" + active +
                '}';
    }

    @Column(name = "lastName")
    @NotBlank(message = "cannot be a blank field")
    @NotNull
    private String lastName;

    @Column(name = "email")
    @Email(message = "email should be valid")
    private String email;

    @Lob
    @Column(name = "image")
    private Byte[] image;

    @Column(name = "password")
    @Size(min = 6)
    private String password;

    @Column(name = "gold")
    Integer gold;

    @Column(name = "silver")
    Integer silver;

    @Column(name = "bronze")
    Integer bronze;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Role> roleList;

    @Column(name = "active")
    Boolean active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getSilver() {
        return silver;
    }

    public void setSilver(Integer silver) {
        this.silver = silver;
    }

    public Integer getBronze() {
        return bronze;
    }

    public void setBronze(Integer bronze) {
        this.bronze = bronze;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
