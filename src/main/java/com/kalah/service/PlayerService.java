package com.kalah.service;

import com.kalah.datatransferobject.PlayerDTO;
import com.kalah.entity.Player;
import com.kalah.exception.ConstraintsViolationException;
import com.kalah.exception.EntityNotFoundException;

public interface PlayerService {
    Player create(PlayerDTO player) throws ConstraintsViolationException;
    Player find(Long id) throws EntityNotFoundException;
}
