package ru.ixec.easyfinance.type;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum InquiryType {

    EXPENSE("Расход", "Выберите ОДНО из трёх действий:\r\n" +
            "` 1. Отправьте фото QR-кода`\r\n" +
            "` 2. Вышлите строку QR-кода`\r\n" +
            "` 3. Напишите данные о расходах в виде: «Покупка: Сумма с копейками», без кавычек`"),
    INCOME("Доход", "Напишите данные о доходах в виде:\r\n" +
            "` «Название дохода: Сумма с копейками», без кавычек`"),
    LOAN("Займ", "Напишите данные о займе в виде:\r\n" +
            "` «Название: -Сумма с копейками», без кавычек, знак «-», если Вы даете в долг.`\r\n" +
            "\r\n" +
            "Текущие займы (₽):\r\n" +
            "%s"),
    TRANSFER("Перевод", "");

    @Getter
    public final String label;
    @Getter
    public final String info;

    InquiryType(String label, String info) {
        this.label = label;
        this.info = info;
    }

    private static final Map<String, InquiryType> INQUIRY_TYPE_MAP = new HashMap<>();

    static {
        for (InquiryType env : values()) {
            INQUIRY_TYPE_MAP.put(env.label, env);
        }
    }

    public static InquiryType get(String label) {
        return INQUIRY_TYPE_MAP.get(label);
    }
}
