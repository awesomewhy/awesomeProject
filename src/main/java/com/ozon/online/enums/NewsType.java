package com.ozon.online.enums;

import lombok.Getter;

@Getter
public enum NewsType {
    SEARCH_ENGINES("Поисковики"),
    WALLETS("Кошельки");

    private final String value;

    NewsType(String value) {
        this.value = value;
    }

}