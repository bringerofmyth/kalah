package com.kalah.logic;

import lombok.Data;
import lombok.Getter;

/**
 * Created by melihgurgah on 15/10/2017.
 */

@Data
public class MoveOutput {

    @Getter
    private int[] resultArray;

    @Getter
    private Boolean finishedWithKalah;

    //1:P1, 2:P2, 3:TIE, 0: ONGOING
    @Getter
    private int winningStatus = 0;

    public MoveOutput(int[] resultArray, Boolean finishedWithKalah) {
        this.resultArray = resultArray;
        this.finishedWithKalah = finishedWithKalah;
    }

    public MoveOutput(int[] resultArray, Boolean finishedWithKalah, int winningStatus) {
        this.resultArray = resultArray;
        this.finishedWithKalah = finishedWithKalah;
        this.winningStatus = winningStatus;
    }
}
