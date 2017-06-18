package com.sftc.web.model;

import java.util.List;


public class GiftCardList {

    private String type;
    private List<GiftCard> giftCards;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GiftCard> getGiftCards() {
        return giftCards;
    }

    public void setGiftCards(List<GiftCard> giftCards) {
        this.giftCards = giftCards;
    }
}
