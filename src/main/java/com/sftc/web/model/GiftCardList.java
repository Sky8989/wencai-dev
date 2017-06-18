package com.sftc.web.model;

import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */
public class GiftCardList {
    private List<GiftCard> expressList;
    private List<GiftCard> FestivalBlessingList;

    public List<GiftCard> getExpressList() {
        return expressList;
    }

    public void setExpressList(List<GiftCard> expressList) {
        this.expressList = expressList;
    }

    public List<GiftCard> getFestivalBlessingList() {
        return FestivalBlessingList;
    }

    public void setFestivalBlessingList(List<GiftCard> festivalBlessingList) {
        FestivalBlessingList = festivalBlessingList;
    }
}
