package com.kalah.controller;

import com.kalah.controller.mapper.GameMapper;
import com.kalah.datatransferobject.BaseResponse;
import com.kalah.datatransferobject.GameDTO;
import com.kalah.datatransferobject.JoinRequest;
import com.kalah.datatransferobject.PlayerDTO;
import com.kalah.domainvalue.Status;
import com.kalah.entity.Game;
import com.kalah.entity.Player;
import com.kalah.exception.ConstraintsViolationException;
import com.kalah.exception.EntityNotFoundException;
import com.kalah.service.GameService;
import com.kalah.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/game")
@Slf4j
public class GameController {

    private final GameService gameService;
    private final PlayerService playerService;

    @Autowired
    public GameController(final GameService gameService, final PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @GetMapping("/{gameId}")
    public GameDTO getGame(@Valid @PathVariable long gameId) throws EntityNotFoundException {
        return GameMapper.makeGameDTO(gameService.find(gameId));
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public GameDTO createGame(@Valid @RequestBody GameDTO gameDTO) throws ConstraintsViolationException,
            EntityNotFoundException {
        Game game = gameService.create(gameDTO);
        GameDTO response = new GameDTO(game.getId(), game.getName());

        return response;
    }


    @PostMapping("/player")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerDTO createPlayer(@Valid @RequestBody PlayerDTO playerDTO) throws ConstraintsViolationException,
            EntityNotFoundException {

        log.info("player create request " + playerDTO);
        Player player = playerService.create(playerDTO);
        PlayerDTO response = new PlayerDTO(player.getId(), player.getUsername());

        return response;
    }

    @GetMapping("/list")
    public List<GameDTO> list() {
        return GameMapper.makeGameDTOList(gameService.list(Status.P2_WAITING_TO_JOIN));
    }

    @PostMapping("/join")
    public BaseResponse join(@RequestBody JoinRequest request) throws EntityNotFoundException {
        gameService.join(request);
        return new BaseResponse();
    }


}
