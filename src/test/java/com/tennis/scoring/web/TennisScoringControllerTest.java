package com.tennis.scoring.web;

import com.tennis.scoring.adapter.web.TennisScoringController;
import com.tennis.scoring.application.service.TennisGameFacade;
import com.tennis.scoring.domain.exception.GameFinishedException;
import com.tennis.scoring.domain.exception.InvalidSequenceException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TennisScoringController.class)
public class TennisScoringControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TennisGameFacade gameFacade;

    @Test
    void should_return_score_history() throws Exception {
        List<String> expected = List.of(
                "Player A: 15 / Player B: 0",
                "Player A: 30 / Player B: 0",
                "Player A: 30 / Player B: 15",
                "Player A: 40 / Player B: 15",
                "Player A wins the game!"
        );

        when(gameFacade.playSequence("AABAA")).thenReturn(expected);

        mockMvc.perform(post("/api/tennis/score")
                        .content("AABAA")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[4]").value("Player A wins the game!"));
    }

    @Test
    void should_return_400_when_sequence_is_empty() throws Exception {
        Mockito.when(gameFacade.playSequence(anyString()))
                .thenThrow(new InvalidSequenceException("Sequence must not be null or empty"));

        mockMvc.perform(post("/api/tennis/score")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(" "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Invalid sequence"))
                .andExpect(jsonPath("$.message").value("Sequence must not be null or empty"))
                .andExpect(jsonPath("$.path").value("/api/tennis/score"));
    }

    @Test
    void should_return_400_when_sequence_contains_invalid_character() throws Exception {
        when(gameFacade.playSequence(anyString()))
                .thenThrow(new InvalidSequenceException(
                        "Invalid character: 'X'. Only 'A' or 'B' allowed."));

        mockMvc.perform(post("/api/tennis/score")
                        .content("ABF")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid sequence"))
                .andExpect(jsonPath("$.message").value("Invalid character: 'X'. Only 'A' or 'B' allowed."));
    }

    @Test
    void should_return_400_when_trying_to_play_after_game_is_won() throws Exception {
        when(gameFacade.playSequence("AAAA")) //
                .thenReturn(java.util.List.of(
                        "Player A: 15 / Player B: 0",
                        "Player A: 30 / Player B: 0",
                        "Player A: 40 / Player B: 0",
                        "Player A wins the game!"
                ));

        when(gameFacade.playSequence("AAAAA"))
                .thenThrow(new GameFinishedException(
                        "Game is already finished"));

        mockMvc.perform(post("/api/tennis/score")
                        .content("AAAAA")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Game already finished"))
                .andExpect(jsonPath("$.message").value("Game is already finished"));
    }
}
