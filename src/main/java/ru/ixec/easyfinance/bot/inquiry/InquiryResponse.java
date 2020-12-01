package ru.ixec.easyfinance.bot.inquiry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ixec.easyfinance.type.KeyboardType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryResponse {

    private String message;
    private KeyboardType keyboardType;
}
