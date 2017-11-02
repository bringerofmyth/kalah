package com.kalah.controller.mapper;

import com.kalah.datatransferobject.GameDTO;
import com.kalah.entity.Game;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GameMapper {

    public static GameDTO makeGameDTO(Game game) {
        GameDTO gameDTO = new GameDTO();

        gameDTO.setName(game.getName());
        gameDTO.setId(game.getId());

        return gameDTO;
    }

    public static List<GameDTO> makeGameDTOList(Collection<Game> games) {
        return games.stream()
                .map(GameMapper::makeGameDTO)
                .collect(Collectors.toList());
    }

}
