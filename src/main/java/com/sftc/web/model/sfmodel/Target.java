package com.sftc.web.model.sfmodel;

/**
 * Created by Administrator on 2017/5/22.
 */
public class Target {
    private Address address;
    private Coordinate coordinate;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
