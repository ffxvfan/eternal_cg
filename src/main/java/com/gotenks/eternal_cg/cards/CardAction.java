package com.gotenks.eternal_cg.cards;

import java.util.function.Supplier;

public class CardAction {
    public String name;
    public Supplier<?> action;

    public CardAction(String name, Supplier<?> action) {
        this.name = name;
        this.action = action;
    }

}
