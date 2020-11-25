package ru.ixec.easyfinance.bot.inquiry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryResponse {

    private String message;
    private boolean cancelMode = false;
}
