package com.Reap.ReapProject.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity(name = "user")
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstName")
    @NotBlank(message = "first name cannot be a blank field")
    @NotNull
    private String firstName;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", password='" + password + '\'' +
                ", goldSharable=" + goldSharable +
                ", silverSharable=" + silverSharable +
                ", bronzeSharable=" + bronzeSharable +
                ", goldRedeemable=" + goldRedeemable +
                ", silverRedeemable=" + silverRedeemable +
                ", bronzeRedeemable=" + bronzeRedeemable +
                ", roleSet=" + roleSet +
                ", active=" + active +
                ", points=" + points +
                '}';
    }

    @Column(name = "lastName")
    @NotBlank(message = "last name cannot be a blank field")
    @NotNull
    private String lastName;

    @Column(name = "email")
    @Email(message = "email should be valid")
    private String email;

    @Lob
    @Column(name = "image")
    private String image;

    @NotBlank(message = "password field cannot be blank")
    @Column(name = "password")
    @Size(min = 6,message = "password should be atleast 6 characters long")
    private String password;


    @Column(name = "goldSharable")
    Integer goldSharable=0;

    @Column(name = "silverSharable")
    Integer silverSharable=0;

    @Column(name = "bronzeSharable")
    Integer bronzeSharable=0;

    @Column(name = "goldRedeemable")
    Integer goldRedeemable=0;

    @Column(name = "silverRedeemable")
    Integer silverRedeemable=0;

    @Column(name = "bronzeRedeemable")
    Integer bronzeRedeemable=0;


    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Role> roleSet=new HashSet<>(Arrays.asList(Role.USER));

    @Column(name = "active")
    Boolean active=true;

    @Column(name="points")
    private Integer points=0;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGoldSharable() {
        return goldSharable;
    }

    public void setGoldSharable(Integer goldSharable) {
        this.goldSharable = goldSharable;
    }

    public Integer getSilverSharable() {
        return silverSharable;
    }

    public void setSilverSharable(Integer silverSharable) {
        this.silverSharable = silverSharable;
    }

    public Integer getBronzeSharable() {
        return bronzeSharable;
    }

    public void setBronzeSharable(Integer bronzeSharable) {
        this.bronzeSharable = bronzeSharable;
    }

    public Integer getGoldRedeemable() {
        return goldRedeemable;
    }

    public void setGoldRedeemable(Integer goldRedeemable) {
        this.goldRedeemable = goldRedeemable;
    }

    public Integer getSilverRedeemable() {
        return silverRedeemable;
    }

    public void setSilverRedeemable(Integer silverRedeemable) {
        this.silverRedeemable = silverRedeemable;
    }

    public Integer getBronzeRedeemable() {
        return bronzeRedeemable;
    }

    public void setBronzeRedeemable(Integer bronzeRedeemable) {
        this.bronzeRedeemable = bronzeRedeemable;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
