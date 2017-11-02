package com.kalah.service;

import com.kalah.datatransferobject.GameDTO;
import com.kalah.datatransferobject.JoinRequest;
import com.kalah.domainvalue.Status;
import com.kalah.entity.Game;
import com.kalah.exception.ConstraintsViolationException;
import com.kalah.exception.EntityNotFoundException;

import java.util.List;

public interface GameService {

    Game find(Long gameId) throws EntityNotFoundException;

    Game create(GameDTO game) throws ConstraintsViolationException, EntityNotFoundException;

    List<Game> list(Status status);

    void join(JoinRequest request) throws EntityNotFoundException;
}
