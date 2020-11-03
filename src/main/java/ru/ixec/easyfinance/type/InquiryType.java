package ru.ixec.easyfinance.type;

import java.util.HashMap;
import java.util.Map;

public enum InquiryType {

    EXPENSE("Расход", "Скиньте одно из: `фото` или `строку` QR-кода, либо данные о расходах в виде: `Покупка: Сумма с копейками`"),
    INCOME("Доход", "Скиньте название дохода, и через двоеточие сумму..."),
    LOAN("Займ", ""),
    TRANSFER("Перевод", "");

    public final String label;
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
