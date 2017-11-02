package com.kalah.controller;

import com.kalah.datatransferobject.Action;
import com.kalah.datatransferobject.Message;
import com.kalah.service.GameRuntimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by melihgurgah on 14/10/2017.
 */
@Slf4j
@Controller
public class ActionController {

    private final GameRuntimeService gameRuntimeService;

    @Autowired
    public ActionController(final GameRuntimeService gameRuntimeService) {
        this.gameRuntimeService = gameRuntimeService;
    }


    @MessageMapping("/room/{room}")
    @SendTo("/topic/move/{room}")
    public Message play(Action action, @DestinationVariable Long room) throws Exception {
        Thread.sleep(1000); // simulated delay
        log.info("Receiving action: " + action);
        Action startAction = new Action();
        startAction.setGameId(action.getGameId());
        startAction.setPlayerId(action.getPlayerId());
        startAction.setIsStart(action.getIsStart());
        startAction.setActionValue(action.getActionValue());
        Message message;

        if (action.getIsStart() != null && action.getIsStart()) {
            message = gameRuntimeService.start(startAction);
        } else {
            message = gameRuntimeService.move(startAction);
        }

        log.info("Game message: " + message);

        return message;
    }


}
