package com.kalah.service;

import com.kalah.datatransferobject.Action;
import com.kalah.datatransferobject.Message;
import com.kalah.domainvalue.Status;
import com.kalah.entity.Game;
import com.kalah.exception.*;
import com.kalah.logic.MoveInput;
import com.kalah.logic.MoveOutput;
import com.kalah.util.ValuesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


@Service
@Slf4j
public class DefaultGameRuntimeService implements GameRuntimeService {

    private final GameService gameService;

    public DefaultGameRuntimeService(final GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    @Transactional
    public Message start(Action action) {

        Game game;
        try {
            game = gameService.find(action.getGameId());
        } catch (EntityNotFoundException e) {
            return new Message(e.getMessage());
        }
        try {
            validateStart(game, action.getPlayerId());
        } catch (GameAlreadyStartedException | InvalidInputException e) {
            return new Message(e.getMessage());
        }

        game.setStatus(game.getPlayer1().getId().equals(action.getPlayerId()) ? Status.P2_MOVE : Status.P1_MOVE);
        game.setLastActivity(ZonedDateTime.now());

        return convertMoveToMessage(game, action, true, false);
    }

    @Override
    @Transactional
    public Message move(Action action) {

        Game game;
        try {
            game = gameService.find(action.getGameId());
        } catch (EntityNotFoundException e) {
            return new Message(e.getMessage());
        }
        try {
            validateMove(game, action.getPlayerId());
        } catch (WrongMoveException | NotYourTurnException e) {
            return new Message(e.getMessage());
        }
        MoveOutput output;
        int[] values = ValuesUtil.decode(game.getValues());
        boolean isPlayer1 = action.getPlayerId().equals(game.getPlayer1().getId());
        MoveInput moveInput;
        try {
            moveInput = new MoveInput(values, isPlayer1, action.getActionValue());
            output = moveInput.move();
        } catch (WrongMoveException e) {
            return new Message(e.getMessage());
        }
        if (output.getWinningStatus() > 0) {
            Status status = (output.getWinningStatus() == 1) ? Status.P1_WINS : ((output.getWinningStatus() == 2)
                    ? Status.P2_WINS : Status.TIE);
            game.setStatus(status);
        } else {
            if (!output.getFinishedWithKalah()) {
                game.setStatus(game.getStatus() == Status.P1_MOVE ? Status.P2_MOVE : Status.P1_MOVE);
            }
        }
        game.setValues(ValuesUtil.encode(output.getResultArray()));
        game.setLastActivity(ZonedDateTime.now());

        return convertMoveToMessage(game, action, false, output.getFinishedWithKalah());
    }

    private void validateMove(Game game, Long attemptedBy) throws WrongMoveException,
            NotYourTurnException {

        if (!(Status.P1_MOVE == game.getStatus() || Status.P2_MOVE == game.getStatus())) {
            throw new WrongMoveException("Action disabled.");
        } else if (Status.P1_MOVE == game.getStatus() && !attemptedBy.equals(game.getPlayer1().getId()) || attemptedBy == null) {
            throw new NotYourTurnException("Not your turn.");
        } else if (Status.P2_MOVE == game.getStatus() && !attemptedBy.equals(game.getPlayer2().getId())) {
            throw new NotYourTurnException("Not your turn.");
        }

    }

    private void validateStart(Game game, Long requesterId) throws GameAlreadyStartedException, InvalidInputException {
        if (Status.WAITING_TO_START != game.getStatus()) {
            throw new GameAlreadyStartedException("Game already started.");
        }
        if (!(game.getPlayer1().getId().equals(requesterId) || game.getPlayer2().getId().equals(requesterId))) {
            throw new InvalidInputException("Invalid player to start.");
        }
    }


    private static Message convertMoveToMessage(Game game, Action action, boolean isStart, boolean finishedWithKalah) {
        Message message = new Message();
        Status status = game.getStatus();

        message.setStatus(status);
        message.setResultSet(ValuesUtil.decode(game.getValues()));

        String firstString = "";
        String secondString = "";
        if (Status.P1_MOVE.equals(status)) {
            firstString = !finishedWithKalah ? "Player 2" : "Player 1";
            secondString = !finishedWithKalah ? "Awaiting move from Player 1" :
                    "Awaiting one more move from Player 1 due to finishing on own Kalah.";

        } else if (Status.P2_MOVE.equals(status)) {
            firstString = !finishedWithKalah ? "Player 1" :  "Player 2";
            secondString = !finishedWithKalah ? "Awaiting move from Player 2" :
                    "Awaiting one more move from Player 2 due to finishing on own Kalah.";
        } else if (Status.P2_WINS.equals(status)) {
            firstString = "Player 2";
            secondString = "Player 2 wins";
        } else if (Status.P1_WINS.equals(status)) {
            firstString = "Player 1";
            secondString = "Player 1 wins";
        }

        String builder;
        int pit = action.getActionValue() < 6 ? action.getActionValue() + 1 : action.getActionValue() - 6;
        if (!isStart) {
            builder = firstString + " played pit number " + pit + ". " + secondString;
        } else {
            builder = firstString + " joined and started the game. " + secondString;
        }

        message.setGameMessage(builder);

        return message;
    }
}
