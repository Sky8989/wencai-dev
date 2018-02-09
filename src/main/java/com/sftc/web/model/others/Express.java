package com.sftc.web.model.others;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by samsunug on 2017/9/15.
 */
public class Express {
    @Setter @Getter
    private String cargoTypeCode;

    @Setter @Getter
    private String cargoTypeName;

    @Setter @Getter
    private String currencyName;

    @Setter @Getter
    private String destCurrencyName;

    @Setter @Getter
    private int destFreight;

    @Setter @Getter
    private int destFuelCost;

    @Setter @Getter
    private int freight;

    @Setter @Getter
    private int fuelCost;

    @Setter @Getter
    private String limitTypeCode;

    @Setter @Getter
    private String limitTypeName;

    @Setter @Getter
    private double weight;

    @Setter @Getter
    private String deliverTime;

    @Setter @Getter
    private String addTime;

    @Setter @Getter
    private String distanceTypeCode;

    @Setter @Getter
    private String distanceTypeName;

    @Setter @Getter
    private boolean internet;

    @Setter @Getter
    private String closedTime;

    @Setter @Getter
    private String orgionView;

    @Setter @Getter
    private String destView;

    @Setter @Getter
    private boolean destFuelFlg;

    @Setter @Getter
    private String weightUnit;

    @Setter @Getter
    private String destWeightUnit;

    @Setter @Getter
    private double destWeight;
}
