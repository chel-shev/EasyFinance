package ru.ixec.easyfinance.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Configuration
public class BotProxyConfiguration {

    @Bean
    public static DefaultBotOptions defaultBotOptions() {
        return ApiContext.getInstance(DefaultBotOptions.class);
    }
}
