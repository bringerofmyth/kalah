package com.kalah.dataaccessobject;

import com.kalah.entity.Game;
import com.kalah.domainvalue.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Long> {

    List<Game> findByStatus(Status status);
}
