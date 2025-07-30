package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.dto.screen.ScreenRequestDTO;
import com.mritunjay.cineticket.model.Screen;
import com.mritunjay.cineticket.model.Theatre;

public interface ScreenService {

    // Get Screen By id
    Screen getScreenById(Long screenId);

    // Create New Screen
    Screen createNewScreen(Theatre theatre, ScreenRequestDTO screenRequestDTO);

}
