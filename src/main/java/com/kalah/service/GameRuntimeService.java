package com.kalah.service;

import com.kalah.datatransferobject.Action;
import com.kalah.datatransferobject.Message;

/**
 * Created by melihgurgah on 15/10/2017.
 */
public interface GameRuntimeService {
    Message start(Action action);
    Message move(Action action);
}
