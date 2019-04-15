package com.Reap.ReapProject.entity;

import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
public class Recognition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer senderId;

    private Integer receiverId;

    private String receiverName;

    private String senderName;

    private String receiverImage;

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    private Boolean revoked=false;

    @Override
    public String toString() {
        return "Recognition{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", receiverName='" + receiverName + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverImage='" + receiverImage + '\'' +
                ", revoked=" + revoked +
                ", goldRedeemable=" + goldRedeemable +
                ", silverRedeemable=" + silverRedeemable +
                ", bronzeRedeemable=" + bronzeRedeemable +
                ", badge='" + badge + '\'' +
                ", date=" + date +
                ", reason='" + reason + '\'' +
                ", comment='" + comment + '\'' +
                ", time=" + time +
                '}';
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    //these fields are added just to render the badges in thymeleaf page
    private Integer goldRedeemable;
    private Integer silverRedeemable;
    private Integer bronzeRedeemable;

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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    private String badge;

    private LocalDate date;

    private String reason;

    @Lob
    @NotNull
    private String comment;

    @Temporal(TemporalType.TIME)
    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
