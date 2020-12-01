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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.ixec.easyfinance.bot.factory.InquiryFactory;
import ru.ixec.easyfinance.bot.factory.KeyboardFactory;
import ru.ixec.easyfinance.bot.inquiry.ExpenseInquiry;
import ru.ixec.easyfinance.bot.inquiry.Inquiry;
import ru.ixec.easyfinance.bot.inquiry.InquiryResponse;
import ru.ixec.easyfinance.entity.ClientEntity;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.service.AccountService;
import ru.ixec.easyfinance.service.ClientService;
import ru.ixec.easyfinance.service.InquiryService;
import ru.ixec.easyfinance.type.KeyboardType;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Comparator;

import static java.util.Objects.isNull;

@Setter
@Getter
@Slf4j
@Component
public class EasyFinanceBot extends TelegramLongPollingBot {

    private final AccountService accS;
    private final ClientService cliS;
    private final InquiryService inqS;

    @Value("${easy.finance.bot.api.username}")
    private String username;
    @Value("${easy.finance.bot.api.token}")
    private String token;

    public EasyFinanceBot(AccountService accS, ClientService cliS, InquiryService inqS) {
        super();
        this.accS = accS;
        this.cliS = cliS;
        this.inqS = inqS;
    }

    @PostConstruct
    private void register() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!isNull(update.getMessage())) {
            Message message = update.getMessage();
            try {
                ClientEntity client = cliS.getClientByChatId(message.getChatId());
                Inquiry lastInquiryEntity = inqS.getLast(client);
                if (isNull(lastInquiryEntity) || lastInquiryEntity.isCompleted()) {
                    InquiryFactory.initInquiry(message.getText(), client);
                    sendMessage(client, "Выберите счет, с которым будет производится операция:", KeyboardType.ACCOUNTS);
                } else if (!lastInquiryEntity.isReadyForProcess()) {
                    InquiryResponse response = lastInquiryEntity.setAccountFromText(message.getText());
                    sendMessage(client, response.getMessage(), response.getKeyboardType());
                } else {
                    if (message.hasPhoto() && lastInquiryEntity instanceof ExpenseInquiry) {
                        lastInquiryEntity.setText(getQRDataFromPhoto(message));
                    } else {
                        lastInquiryEntity.setText(message.getText());
                    }
                    InquiryResponse response = lastInquiryEntity.process();
                    sendMessage(client, response.getMessage(), response.getKeyboardType());
                }
            } catch (BotException e) {
                log.debug(e.getMessage());
                sendMessage(message, e.getMessage(), e.getInquiryResponse().getKeyboardType());
            }
        }
    }

    public String getQRDataFromPhoto(Message message) {
        try {
            @NotNull PhotoSize photo = getPhoto(message);
            String path = getFilePath(photo);
            java.io.File filePhoto = downloadFile(path);
            @NotNull String qr = DecoderPhotoQR.decode(filePhoto);
            return qr;
        } catch (TelegramApiException e) {
            throw new BotException("Ошибка получения фотографии!", KeyboardType.CANCEL);
        } catch (NullPointerException | IOException e) {
            throw new BotException("QR-код прочитать не удалось!", KeyboardType.CANCEL);
        }
    }

    public String getFilePath(PhotoSize photo) {
        if (isNull(photo.getFilePath()) || photo.getFilePath().isEmpty()) {
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(photo.getFileId());
            try {
                File file = execute(getFileMethod);
                return file.getFilePath();
            } catch (TelegramApiException e) {
                throw new BotException("Не удалось получить файл!", KeyboardType.CANCEL);
            }
        } else {
            return photo.getFilePath();
        }
    }

    public PhotoSize getPhoto(Message message) {
        return message.getPhoto()
                .stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElseThrow(() -> new BotException("Ошибка получения фотографии!", KeyboardType.CANCEL));
    }

    public void sendMessage(ClientEntity client, String text, KeyboardType keyboardType) {
        try {
            SendMessage sendMessage = SendMessage.builder().chatId(String.valueOf(client.getChatId())).text(text).build();
            sendMessage.setReplyMarkup(KeyboardFactory.createKeyboard(keyboardType, client));
            sendMessage.enableMarkdown(true);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message, String text, KeyboardType keyboardType) {
        try {
            SendMessage sendMessage = SendMessage.builder().chatId(String.valueOf(message.getChatId())).text(text).build();
            sendMessage.setReplyMarkup(KeyboardFactory.createKeyboard(keyboardType, null));
            sendMessage.enableMarkdown(true);
            execute(sendMessage);
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
