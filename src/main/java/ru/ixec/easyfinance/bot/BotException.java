package ru.ixec.easyfinance.bot;

import lombok.Getter;
import ru.ixec.easyfinance.bot.inquiry.InquiryResponse;

@Getter
public class BotException extends RuntimeException {

    private final InquiryResponse inquiryResponse;

    public BotException(String massage, boolean cancelMode) {
        super(massage);
        inquiryResponse = new InquiryResponse(massage, cancelMode);
    }
}
