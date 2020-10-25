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
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.ixec.easyfinance.bot.receipt.Receipt;
import ru.ixec.easyfinance.entity.Account;
import ru.ixec.easyfinance.service.AccountService;
import ru.ixec.easyfinance.service.ExpenseService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Setter
@Getter
@Slf4j
@Component
public class EasyFinanceBot extends TelegramLongPollingBot {

    private final AccountService as;
    private final ExpenseService es;
    private final Receipt receipt;
    @Value("${easy.finance.bot.api.username}")
    private String username;
    @Value("${easy.finance.bot.api.token}")
    private String token;

    public EasyFinanceBot(AccountService as, ExpenseService es, Receipt receipt) {
        super();
        this.as = as;
        this.es = es;
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
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!isNull(update.getMessage())) {
            try {
                String qrInfo = "";
                Long chatId = update.getMessage().getChatId();
                if (update.hasMessage() && update.getMessage().hasPhoto()) {
                    PhotoSize photo = getPhoto(update);
                    String path = getFilePath(photo);
                    java.io.File filePhoto = downloadPhotoByFilePath(path);
                    qrInfo = DecoderQR.decode(filePhoto);
                } else if (update.hasMessage() && update.getMessage().hasText()) {
                    qrInfo = update.getMessage().getText();
                }
                Account account = as.getAccountByChatId(chatId);
                Objects.requireNonNull(account);
                receipt.setQR(qrInfo);
                es.saveAll(receipt.getExpenses(), account);
                sendMessage(String.valueOf(chatId), "Чек добавлен!");
            } catch (JSONException | IOException | NullPointerException e) {
                log.debug(e.getMessage());
                sendMessage(String.valueOf(update.getMessage().getChatId()), "Ошибка добавления!");
            }
        }
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

    public PhotoSize getPhoto(Update update) {
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            List<PhotoSize> photos = update.getMessage().getPhoto();
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

    private void sendMessage(String chat_id, String message) {
        try {
            execute(new SendMessage(chat_id, message));
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
