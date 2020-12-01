package ru.ixec.easyfinance.exception;

import lombok.Getter;
import ru.ixec.easyfinance.bot.inquiry.InquiryResponse;
import ru.ixec.easyfinance.type.KeyboardType;

@Getter
public class BotException extends RuntimeException {

    private final InquiryResponse inquiryResponse;

    public BotException(String massage, KeyboardType keyboardType) {
        super(massage);
        inquiryResponse = new InquiryResponse(massage, keyboardType);
    }
}
