package ru.ixec.easyfinance.bot;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.ixec.easyfinance.bot.receipt.Receipt;
import ru.ixec.easyfinance.entity.Account;
import ru.ixec.easyfinance.entity.Expense;
import ru.ixec.easyfinance.entity.ExpenseProduct;
import ru.ixec.easyfinance.entity.Inquiry;
import ru.ixec.easyfinance.service.AccountService;
import ru.ixec.easyfinance.service.ExpenseService;
import ru.ixec.easyfinance.service.InquiryService;
import ru.ixec.easyfinance.type.InquiryType;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.isNull;
import static ru.ixec.easyfinance.type.InquiryType.*;

@Setter
@Getter
@Slf4j
@Component
public class EasyFinanceBot extends TelegramLongPollingBot {

    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private ReplyKeyboardMarkup cancelKeyboardMarkup;
    private final AccountService as;
    private final ExpenseService es;
    private final InquiryService is;
    private final Receipt receipt;

    @Value("${easy.finance.bot.api.username}")
    private String username;
    @Value("${easy.finance.bot.api.token}")
    private String token;

    public EasyFinanceBot(AccountService as, ExpenseService es, InquiryService is, Receipt receipt) {
        super();
        this.as = as;
        this.es = es;
        this.is = is;
        this.receipt = receipt;
    }

    @PostConstruct
    private void register() {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(getDefaultKeyboard());
        cancelKeyboardMarkup = new ReplyKeyboardMarkup();
        cancelKeyboardMarkup.setSelective(true);
        cancelKeyboardMarkup.setResizeKeyboard(true);
        cancelKeyboardMarkup.setOneTimeKeyboard(false);
        cancelKeyboardMarkup.setKeyboard(getCancelKeyboard());
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!isNull(update.getMessage())) {
            Message message = update.getMessage();
            Account account = as.getAccountByChatId(message.getChatId());
            try {
                Objects.requireNonNull(account);
                Inquiry lastInquiry = is.getLast(account.getClient());
                if (!isNull(lastInquiry) && !lastInquiry.getCompleted())
                    processInquiry(message, account, lastInquiry);
                else
                    initInquiry(message, account);
            } catch (NullPointerException e) {
                log.debug(e.getMessage());
                sendMessage(message, "Пользователь не найден!", false);
            }
        }

    }

    private void initInquiry(Message message, Account account) {
        Inquiry inquiry = new Inquiry();
        inquiry.setClient(account.getClient());
        inquiry.setDate(LocalDateTime.now());
        InquiryType type = InquiryType.get(message.getText());
        if (isNull(type)) {
            sendMessage(message, "Ошибочный запрос!", false);
            return;
        }
        inquiry.setType(type);
        sendMessage(message, type.info, true);
        is.save(inquiry);
    }

    private void processInquiry(Message message, Account account, Inquiry lastInquiry) {
        if (lastInquiry.getType() == EXPENSE) {
            processExpense(message, account, lastInquiry);
        } else if (lastInquiry.getType() == INCOME) {
            processIncome(message, account);
        } else if (lastInquiry.getType() == LOAN) {
            processLoan(message, account);
        } else if (lastInquiry.getType() == TRANSFER) {
            processTransfer(message, account);
        } else {
            sendMessage(message, "Ошибочный запрос!", false);
        }
    }

    private void processIncome(Message message, Account account) {

    }

    private void processLoan(Message message, Account account) {

    }

    private void processTransfer(Message message, Account account) {

    }

    private void processExpense(Message message, Account account, Inquiry lastInquiry) {
        try {
            String text;
            if (message.hasPhoto()) {
                text = getQRDataFromPhoto(message);
                lastInquiry.setText(text);
                saveReceipt(message, account, text);
            } else if (message.hasText()) {
                text = message.getText();
                lastInquiry.setText(text);
                Objects.requireNonNull(text);
                if (text.equals("Отмена")) {
                    sendMessage(message, "Добавление отменено!", false);
                } else if (text.split(":").length == 2) {
                    savePurchase(message, account, text);
                } else {
                    saveReceipt(message, account, text);
                }
            }
            completeInquiry(lastInquiry);
        } catch (JSONException | NullPointerException | IOException e) {
            log.debug(e.getMessage());
            sendMessage(message, "Ошибка добавления!", false);
            completeInquiry(lastInquiry);
        }
    }

    private String getQRDataFromPhoto(Message message) throws IOException, NullPointerException {
        PhotoSize photo = getPhoto(message);
        String path = getFilePath(photo);
        java.io.File filePhoto = downloadPhotoByFilePath(path);
        String qr = DecoderPhotoQR.decode(filePhoto);
        Objects.requireNonNull(qr);
        return qr;
    }

    private void savePurchase(Message message, Account account, String text) {
        String name = text.split(":")[0];
        double value = Double.parseDouble(text.split(":")[1].replace(",", ".").trim()) * 100;
        ExpenseProduct expenseProduct = new ExpenseProduct(name, null);
        Expense expense = new Expense(LocalDateTime.now(), (long) value, (long) value, 1.0, expenseProduct);
        es.save(expense, account);
        sendMessage(message, "Покупка добавлена!", false);
    }

    private void saveReceipt(Message message, Account account, String qr) throws JSONException {
        receipt.setQR(qr);
        List<Expense> expenses = receipt.getExpenses();
        es.saveAll(expenses, account);
        sendMessage(message, "Чек добавлен!", false);
    }

    private void completeInquiry(Inquiry lastInquiry) {
        receipt.clear();
        lastInquiry.setCompleted(true);
        is.save(lastInquiry);
    }

    private void sendMessage(Message message, String text, Boolean canceledMode) {
        SendMessage sendMessage = new SendMessage(message.getChatId(), text);
        if (canceledMode)
            sendMessage.setReplyMarkup(cancelKeyboardMarkup);
        else
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.enableMarkdown(true);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<KeyboardRow> getDefaultKeyboard() {
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.addAll(Arrays.asList(EXPENSE.label, INCOME.label));
        keyboardSecondRow.addAll(Arrays.asList(LOAN.label, TRANSFER.label));
        return Arrays.asList(keyboardFirstRow, keyboardSecondRow);
    }

    private List<KeyboardRow> getCancelKeyboard() {
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.addAll(Collections.singletonList("Отмена"));
        return Collections.singletonList(keyboardFirstRow);
    }

    public String getFilePath(PhotoSize photo) {
        Objects.requireNonNull(photo);
        if (photo.hasFilePath()) {
            return photo.getFilePath();
        } else {
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(photo.getFileId());
            try {
                File file = execute(getFileMethod);
                return file.getFilePath();
            } catch (TelegramApiException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public PhotoSize getPhoto(Message message) {
        if (message.hasPhoto()) {
            List<PhotoSize> photos = message.getPhoto();
            return photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
        }
        return null;
    }

    public java.io.File downloadPhotoByFilePath(String filePath) {
        try {
            return downloadFile(filePath);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
