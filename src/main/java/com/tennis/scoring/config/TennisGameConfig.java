package com.tennis.scoring.config;

import com.tennis.scoring.domain.service.TennisGameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TennisGameConfig {

    @Bean
    public TennisGameService tennisGameService() {
        return new TennisGameService();
    }
}
