package com.tennis.scoring.adapter.web;

import com.tennis.scoring.application.service.TennisGameFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tennis")
public class TennisScoringController {

    Logger logger = LoggerFactory.getLogger(TennisScoringController.class);
    private final TennisGameFacade gameFacade;

    public TennisScoringController(TennisGameFacade gameFacade) {
        this.gameFacade = gameFacade;
    }

    @PostMapping("/score")
    public List<String> play(@RequestBody String sequence) {
        logger.info("new game sequence received: {}", sequence);
        return gameFacade.playSequence(sequence.trim());
    }
}

