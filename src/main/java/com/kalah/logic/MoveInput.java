package com.kalah.logic;

import com.kalah.exception.WrongMoveException;

/**
 * Created by melihgurgah on 15/10/2017.
 */

public class MoveInput {

    private static final int PLAYER1_KALAH = 6;
    private static final int PLAYER2_KALAH = 13;
    private static final int SLOT_SIZE = 14;

    private int[] valueArray;
    private boolean p1Move;
    private int startingIndex;

    public MoveInput() {
        valueArray = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    }

    public MoveInput(int[] valueArray, boolean p1Move, int startingIndex) throws WrongMoveException {
        int number = valueArray[startingIndex];
        if (number == 0 || isPlayer1Side(startingIndex) && !p1Move || isPlayer2Side(startingIndex) && p1Move
                || startingIndex == PLAYER1_KALAH || startingIndex == PLAYER2_KALAH) {
            throw new WrongMoveException("Wrong move");
        }

        this.valueArray = valueArray;
        this.p1Move = p1Move;
        this.startingIndex = startingIndex;
    }

    public MoveOutput move() throws WrongMoveException {
        int lastStep = distribute();
        if (finishedWithKalah(lastStep)) {
            return new MoveOutput(valueArray, true, checkWin());
        } else if (finishedWithEmptySlot(lastStep)) {
            grabOpponentStones(lastStep);
        }

        return new MoveOutput(valueArray, false, checkWin());
    }

    private void grabOpponentStones(int lastStep) {
        int opponent = oppositeSlot(lastStep);
        final int kalah = p1Move ? PLAYER1_KALAH : PLAYER2_KALAH;

        valueArray[kalah] += valueArray[opponent];
        valueArray[opponent] = 0;
    }

    private int checkWin() {
        if (checkPlayerSlotsEmpty()) {
            int p1Stones = totalStones(true);
            int p2Stones = totalStones(false);
            if (p1Stones == p2Stones) return 3;
            else {
                return p1Stones > p2Stones ? 1 : 2;
            }
        } else {
            return 0;
        }
    }

    private boolean checkPlayerSlotsEmpty() {
        int start = p1Move ? 0 : 7;
        int end = p1Move ? 6 : 13;

        for (int i = start; i < end; i++) {
            if (valueArray[i] > 0) {
                return false;
            }
        }
        return true;
    }

    private int totalStones(boolean isPlayer1) {
        int start = isPlayer1 ? 0 : 7;
        int end = isPlayer1 ? 7 : 14;
        int sum = 0;

        for (int i = start; i < end; i++) {
            sum += valueArray[i];
        }
        return sum;
    }

    private int distribute() throws WrongMoveException {

        int number = valueArray[startingIndex];
        int count = 0;
        int lastStep = -1;

        valueArray[startingIndex] = 0;
        while (number > count) {
            lastStep = (startingIndex + count + 1) % SLOT_SIZE;
            if (lastStep == PLAYER2_KALAH && p1Move || !p1Move && lastStep == PLAYER1_KALAH) {
                count++;
                number++;
                continue;
            }
            valueArray[(startingIndex + count + 1) % SLOT_SIZE]++;
            count++;
        }

        return lastStep;
    }

    private int oppositeSlot(int lastStep) {
        return 12 - lastStep;
    }

    private boolean finishedWithEmptySlot(int lastStep) {
        return (p1Move && isPlayer1Side(lastStep) && valueArray[lastStep] == 1 ||
                !p1Move && isPlayer2Side(lastStep) && valueArray[lastStep] == 1);
    }

    private boolean isPlayer1Side(int index) {
        return index < 6;
    }

    private boolean isPlayer2Side(int index) {
        return index > 6 && index < 13;
    }

    private boolean finishedWithKalah(int lastStep) {
        return lastStep == PLAYER1_KALAH && p1Move || lastStep == PLAYER2_KALAH && !p1Move;
    }


}
