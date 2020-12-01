package ru.ixec.easyfinance.type;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum ActionType {

    EXPENSE("Расход", "Выберите ОДНО из трёх действий:\r\n" +
            "` 1. Отправьте фото QR-кода`\r\n" +
            "` 2. Вышлите строку QR-кода`\r\n" +
            "` 3. Напишите данные о расходах в виде: «Покупка: Сумма», без кавычек`"),
    INCOME("Доход", "Напишите данные о доходах в виде:\r\n" +
            "` «Название дохода: Сумма», без кавычек`"),
    LOAN("Займ", "Напишите данные о займе в виде:\r\n" +
            "` «Название: -Сумма», без кавычек, знак «-», если Вы даете в долг.`\r\n" +
            "\r\n" +
            "Текущие займы (₽):\r\n" +
            "%s"),
    TRANSFER("Перевод", "Напишите сумму перевода:\r\n");

    @Getter
    public final String label;
    @Getter
    public final String info;

    ActionType(String label, String info) {
        this.label = label;
        this.info = info;
    }

    private static final Map<String, ActionType> ACTION_TYPE_MAP = new HashMap<>();

    static {
        for (ActionType env : values()) {
            ACTION_TYPE_MAP.put(env.label, env);
        }
    }

    public static ActionType get(String label) {
        return ACTION_TYPE_MAP.get(label);
    }
}
