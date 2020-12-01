package ru.ixec.easyfinance.bot;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.ixec.easyfinance.bot.inquiry.ExpenseInquiry;
import ru.ixec.easyfinance.bot.inquiry.Inquiry;
import ru.ixec.easyfinance.bot.inquiry.InquiryFactory;
import ru.ixec.easyfinance.bot.inquiry.InquiryResponse;
import ru.ixec.easyfinance.entity.AccountEntity;
import ru.ixec.easyfinance.service.AccountService;
import ru.ixec.easyfinance.service.InquiryService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

import static java.util.Objects.isNull;
import static ru.ixec.easyfinance.type.ActionType.*;

@Setter
@Getter
@Slf4j
@Component
public class EasyFinanceBot extends TelegramLongPollingBot {

    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private ReplyKeyboardMarkup cancelKeyboardMarkup;
    private final AccountService as;
    private final InquiryService is;

    @Value("${easy.finance.bot.api.username}")
    private String username;
    @Value("${easy.finance.bot.api.token}")
    private String token;

    public EasyFinanceBot(AccountService as, InquiryService is) {
        super();
        this.as = as;
        this.is = is;
    }

    @PostConstruct
    private void register() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
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
            try {
                AccountEntity accountEntity = as.getAccountByChatId(message.getChatId());
                Objects.requireNonNull(accountEntity);
                Inquiry lastInquiryEntity = is.getLast(accountEntity);
                if (isNull(lastInquiryEntity) || lastInquiryEntity.isCompleted()) {
                    lastInquiryEntity = InquiryFactory.createInquiry(message.getText(), accountEntity);
                    sendMessage(message, lastInquiryEntity.getTextInfo(), true);
                } else {
                    if (message.hasPhoto() && lastInquiryEntity instanceof ExpenseInquiry) {
                        lastInquiryEntity.setText(getQRDataFromPhoto(message));
                    } else {
                        lastInquiryEntity.setText(message.getText());
                    }
                    InquiryResponse response = lastInquiryEntity.process();
                    sendMessage(message, response.getMessage(), response.isCancelMode());
                }
            } catch (NullPointerException e) {
                log.debug(e.getMessage());
                sendMessage(message, "Пользователь не найден!", false);
            } catch (BotException e) {
                log.debug(e.getMessage());
                sendMessage(message, e.getMessage(), e.getInquiryResponse().isCancelMode());
            } catch (IOException e) {
                log.debug(e.getMessage());
                sendMessage(message, "Ошибка добавления!", false);
            }
        }
    }

    public String getQRDataFromPhoto(Message message) throws IOException {
        try {
            PhotoSize photo = getPhoto(message);
            String path = getFilePath(photo);
            java.io.File filePhoto = downloadPhotoByFilePath(path);
            String qr = DecoderPhotoQR.decode(filePhoto);
            Objects.requireNonNull(qr);
            return qr;
        } catch (NullPointerException e) {
            throw new BotException("QR-код прочитать неудалось!", true);
        }
    }

    public String getFilePath(PhotoSize photo) {
        Objects.requireNonNull(photo);
        if (!isNull(photo.getFilePath()) && !photo.getFilePath().isEmpty()) {
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

    public void sendMessage(Message message, String text, Boolean canceledMode) {
        SendMessage sendMessage = SendMessage.builder().chatId(String.valueOf(message.getChatId())).text(text).build();
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

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
