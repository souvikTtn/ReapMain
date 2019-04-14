package com.Reap.ReapProject.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class OrderSummary {
    @Override
    public String toString() {
        return "OrderSummary{" +
                "id=" + id +
                ", itemQuantity=" + itemQuantity +
                ", userId=" + userId +
                ", totalPointsRedeemed=" + totalPointsRedeemed +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //key=itemId value=itemQuantity
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Integer,Integer> itemQuantity=new LinkedHashMap<>();

    private Integer userId;

    private Integer totalPointsRedeemed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Map<Integer, Integer> getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Map<Integer, Integer> itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Integer getTotalPointsRedeemed() {
        return totalPointsRedeemed;
    }

    public void setTotalPointsRedeemed(Integer totalPointsRedeemed) {
        this.totalPointsRedeemed = totalPointsRedeemed;
    }


    //utility method to store all the item ids of the item in the order summary
    public List<Integer> getItemIdsInOrderSummary() {
        List<Integer> itemIds = new ArrayList<>();
        for (Integer key : this.getItemQuantity().keySet()) {
            itemIds.add(key);
        }
        return itemIds;
    }
}
