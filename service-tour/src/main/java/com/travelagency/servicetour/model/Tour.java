package com.travelagency.servicetour.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tour {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    Long id;
    private String name;
    private String startDate;
    private String lastDate;
    private String route;
    private Integer cost;
    private Integer numberOfSeats;
    private Long guiderId;

    public Tour() {
    }

    public Tour(String name, String startDate, String lastDate, String route, Integer cost, Integer numberOfSeats, Long guiderId) {
        this.name = name;
        this.startDate = startDate;
        this.lastDate = lastDate;
        this.route = route;
        this.cost = cost;
        this.numberOfSeats = numberOfSeats;
        this.guiderId = guiderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Long getGuiderId() {
        return guiderId;
    }

    public void setGuiderId(Long guiderId) {
        this.guiderId = guiderId;
    }
}
