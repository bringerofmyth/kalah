package com.kalah.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kalah.domainvalue.Status;
import lombok.Data;

/**
 * Created by melihgurgah on 14/10/2017.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private int[] resultSet;
    private Status status;
    private String gameMessage;
    private String errorMessage;

    public Message(String errorMessage) {
        this.errorMessage = errorMessage;
        this.gameMessage = errorMessage;
    }

    public Message() {
    }
}
