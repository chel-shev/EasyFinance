package ru.ixec.easyfinance.bot.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.service.ClientService;
import ru.ixec.easyfinance.type.KeyboardType;
import ru.ixec.easyfinance.utils.ApplicationContextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ixec.easyfinance.type.ActionType.*;

@Component
@RequiredArgsConstructor
public class KeyboardFactory {

    public static ReplyKeyboardMarkup createKeyboard(KeyboardType type, ClientEntity client) {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        switch (type) {
            case INQUIRIES:
                KeyboardRow inquiriesFirstRow = new KeyboardRow();
                KeyboardRow inquiriesSecondRow = new KeyboardRow();
                inquiriesFirstRow.addAll(Arrays.asList(EXPENSE.label, INCOME.label));
                inquiriesSecondRow.addAll(Arrays.asList(LOAN.label, TRANSFER.label));
                keyboardMarkup.setKeyboard(Arrays.asList(inquiriesFirstRow, inquiriesSecondRow));
                break;
            case CANCEL:
                keyboardMarkup.setKeyboard(Collections.singletonList(getCancelRow()));
                break;
            case ACCOUNTS:
                List<KeyboardRow> rowList = new ArrayList<>(getAccountRowList(client));
                rowList.add(getCancelRow());
                keyboardMarkup.setKeyboard(rowList);
                break;
            default:
                throw new BotException("Неверный тип клавиатуры!", KeyboardType.INQUIRIES);
        }
        return keyboardMarkup;
    }

    private static KeyboardRow getCancelRow() {
        KeyboardRow cancel = new KeyboardRow();
        cancel.addAll(Collections.singletonList("Отмена"));
        return cancel;
    }

    private static List<KeyboardRow> getAccountRowList(ClientEntity client) {
        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        ClientService cliS = (ClientService) appCtx.getBean("clientService");
        List<KeyboardRow> rowList = new ArrayList<>();
        List<String> accountList = cliS.getAccountList(client.getClientId()).stream().map(a -> a.getName() + " (" + String.format("%.2f", a.getAmount() / 100d) + ")").collect(Collectors.toList());
        KeyboardRow row = new KeyboardRow();
        List<String> rowsName = new ArrayList<>();
        for (int i = 0; i < accountList.size(); i++) {
            if (i % 2 == 0) {
                if (i != 0) {
                    row.addAll(rowsName);
                    rowList.add(row);
                }
                row = new KeyboardRow();
                rowsName = new ArrayList<>();
            }
            rowsName.add(accountList.get(i));
        }
        row.addAll(rowsName);
        rowList.add(row);
        return rowList;
    }
}