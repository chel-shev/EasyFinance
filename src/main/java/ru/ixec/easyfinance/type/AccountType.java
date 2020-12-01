package ru.ixec.easyfinance.type;

import lombok.Getter;

public enum AccountType {

    DEBIT("\uD83D\uDCB5"),
    CREDIT("\uD83D\uDCB3"),
    CASH("\uD83D\uDCB0");

    @Getter
    public final String icon;

    AccountType(String icon) {
        this.icon = icon;
    }
}
