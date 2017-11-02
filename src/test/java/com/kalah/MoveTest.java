package com.kalah;

import com.kalah.exception.WrongMoveException;
import com.kalah.logic.MoveInput;
import com.kalah.logic.MoveOutput;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;

/**
 * Created by melihgurgah on 15/10/2017.
 */

public class MoveTest {
    private int[] values = null;

    @Before
    public void init() {
        values = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    }

    // Test One More Move Or Not
    @Test
    public void testOneMoreMoveForPlayer1() throws Exception {
        MoveInput moveInput = new MoveInput(values, true, 0);
        MoveOutput output = moveInput.move();
        assertTrue(output.getFinishedWithKalah());
    }

    // Test One More Move Or Not
    @Test
    public void testOneMoreMoveForPlayer2() throws Exception {

        MoveInput moveInput = new MoveInput(values, false, 7);
        MoveOutput output = moveInput.move();
        assertTrue(output.getFinishedWithKalah());
    }

    // Test One More Move Or Not
    @Test
    public void testNoMoreMoveForPlayer1() throws Exception {

        MoveInput moveInput = new MoveInput(values, true, 3);
        MoveOutput output = moveInput.move();
        assertFalse(output.getFinishedWithKalah());
    }

    // Test One More Move Or Not
    @Test
    public void testNoMoreMoveForPlayer2() throws Exception {

        MoveInput moveInput = new MoveInput(values, false, 11);
        MoveOutput output = moveInput.move();
        assertFalse(output.getFinishedWithKalah());
    }

    // Player grabs the opponent stones
    @Test
    public void testSendPlayer1StonesToPlayer2() throws Exception {
        MoveInput moveInput = new MoveInput(values, true, 5);
        MoveOutput output = moveInput.move();
        assertFalse(output.getFinishedWithKalah());
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(6, 6, 6, 6, 6, 0, 1, 7, 7, 7, 7, 7, 6, 0));
    }

    // Player grabs the opponent stones
    @Test
    public void testSendPlayer2StonesToPlayer1() throws Exception {
        MoveInput moveInput = new MoveInput(values, false, 12);
        MoveOutput output = moveInput.move();
        assertFalse(output.getFinishedWithKalah());
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(7, 7, 7, 7, 7, 6, 0, 6, 6, 6, 6, 6, 0, 1));
    }

    // Player grabs the opponent stones
    @Test
    public void testPlayer1GetsPlayer2Stones() throws Exception {
        int[] tempValues = {4, 4, 6, 6, 6, 0, 4, 6, 6, 6, 6, 6, 6, 0};
        MoveInput moveInput = new MoveInput(tempValues, true, 1);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(4, 0, 7, 7, 7, 1, 10, 0, 6, 6, 6, 6, 6, 0));
    }

    // Test continuing distribution from opponent to player side by skipping opponent kalah
    @Test
    public void testPlayer1ContinueToDisribute() throws Exception {
        int[] tempValues = {3, 6, 6, 6, 8, 9, 0, 7, 7, 6, 6, 6, 6, 0};
        MoveInput moveInput = new MoveInput(tempValues, true, 5);
        MoveOutput output = moveInput.move();
        assertFalse(output.getFinishedWithKalah());
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(4, 7, 6, 6, 8, 0, 1, 8, 8, 7, 7, 7, 7, 0));
    }

    // Test continuing distribution from opponent to player side by skipping opponent kalah
    @Test
    public void testPlayer2ContinueToDisribute() throws Exception {
        int[] tempValues = {2, 6, 6, 6, 8, 6, 0, 6, 6, 4, 6, 6, 10, 0};
        MoveInput moveInput = new MoveInput(tempValues, false, 12);
        MoveOutput output = moveInput.move();
        assertFalse(output.getFinishedWithKalah());
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(3, 7, 7, 7, 9, 7, 0, 7, 7, 5, 6, 6, 0, 1));
    }

    // Test continuing distribution from opponent to player side by skipping opponent kalah
    @Test
    public void testDoubleDisributePlayer1() throws Exception {
        int[] tempValues = {0, 0, 0, 10, 5, 21, 0, 6, 6, 4, 6, 6, 10, 0};
        MoveInput moveInput = new MoveInput(tempValues, true, 5);
        MoveOutput output = moveInput.move();
        assertFalse(output.getFinishedWithKalah());
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(2, 1, 1, 11, 6, 1, 2, 8, 8, 6, 8, 8, 12, 0));
    }

    // Test continuing distribution from opponent to player side by skipping opponent kalah
    @Test
    public void testDoubleDisributePlayer2() throws Exception {
        int[] tempValues = {6, 6, 4, 6, 6, 10, 0, 0, 0, 0, 10, 5, 21, 0};
        MoveInput moveInput = new MoveInput(tempValues, false, 12);
        MoveOutput output = moveInput.move();
        assertFalse(output.getFinishedWithKalah());
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(8, 8, 6, 8, 8, 12, 0, 2, 1, 1, 11, 6, 1, 2));
    }

    // Player 1 Won
    @Test
    public void testPlayer1Won() throws Exception {
        int[] tempValues = {0, 0, 0, 0, 0, 1, 38, 0, 0, 0, 4, 0, 1, 28};
        MoveInput moveInput = new MoveInput(tempValues, true, 5);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertEquals(1, output.getWinningStatus());
        assertThat(actual, contains(0, 0, 0, 0, 0, 0, 39, 0, 0, 0, 4, 0, 1, 28));

    }

    // Player 2 Won
    @Test
    public void testPlayer2Won() throws Exception {
        int[] tempValues = {0, 0, 0, 4, 0, 1, 28, 0, 0, 0, 0, 0, 1, 38};
        MoveInput moveInput = new MoveInput(tempValues, false, 12);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertEquals(2, output.getWinningStatus());
        assertThat(actual, contains(0, 0, 0, 4, 0, 1, 28, 0, 0, 0, 0, 0, 0, 39));
    }

    // Tie Game
    @Test
    public void testTieGame() throws Exception {
        int[] tempValues = {0, 2, 0, 4, 0, 2, 28, 0, 0, 0, 0, 0, 1, 35};
        MoveInput moveInput = new MoveInput(tempValues, false, 12);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertEquals(3, output.getWinningStatus());
        assertThat(actual, contains(0, 2, 0, 4, 0, 2, 28, 0, 0, 0, 0, 0, 0, 36));
    }

    // Wrong Move
    @Test(expected = WrongMoveException.class)
    public void testWrongMoveForPlayingOnKalah() throws Exception {

        MoveInput moveInput = new MoveInput(values, true, 6);
        moveInput.move();
    }

    // Wrong Move
    @Test(expected = WrongMoveException.class)
    public void testWrongMoveForPlayingOnWrongSide() throws Exception {

        MoveInput moveInput = new MoveInput(values, true, 7);
        moveInput.move();
    }

    // Wrong Move
    @Test(expected = WrongMoveException.class)
    public void testWrongMoveForPlayingOnEmptySlot() throws Exception {
        int[] tempValues = {4, 4, 6, 6, 6, 0, 4, 6, 6, 6, 6, 6, 6, 0};
        MoveInput moveInput = new MoveInput(tempValues, true, 5);
        moveInput.move();
    }

    // Test all kind of moves from both players
    @Test
    public void testMoveForPlayer1MovesPit1() throws Exception {
        MoveInput moveInput = new MoveInput(values, true, 0);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0));
        assertTrue(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer1MovesPit2() throws Exception {
        MoveInput moveInput = new MoveInput(values, true, 1);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(6, 0, 7, 7, 7, 7, 1, 7, 6, 6, 6, 6, 6, 0));
        assertFalse(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer1MovesPit3() throws Exception {
        MoveInput moveInput = new MoveInput(values, true, 2);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(6, 6, 0, 7, 7, 7, 1, 7, 7, 6, 6, 6, 6, 0));
        assertFalse(output.getFinishedWithKalah());

    }

    @Test
    public void testMoveForPlayer1MovesPit4() throws Exception {
        MoveInput moveInput = new MoveInput(values, true, 3);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(6, 6, 6, 0, 7, 7, 1, 7, 7, 7, 6, 6, 6, 0));
        assertFalse(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer1MovesPit5() throws Exception {
        MoveInput moveInput = new MoveInput(values, true, 4);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(6, 6, 6, 6, 0, 7, 1, 7, 7, 7, 7, 6, 6, 0));
        assertFalse(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer1MovesPit6() throws Exception {
        MoveInput moveInput = new MoveInput(values, true, 5);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(6, 6, 6, 6, 6, 0, 1, 7, 7, 7, 7, 7, 6, 0));
        assertFalse(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer2MovesPit1() throws Exception {
        MoveInput moveInput = new MoveInput(values, false, 7);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(6, 6, 6, 6, 6, 6, 0, 0, 7, 7, 7, 7, 7, 1));
        assertTrue(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer2MovesPit2() throws Exception {
        MoveInput moveInput = new MoveInput(values, false, 8);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(7, 6, 6, 6, 6, 6, 0, 6, 0, 7, 7, 7, 7, 1));
        assertFalse(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer2MovesPit3() throws Exception {
        MoveInput moveInput = new MoveInput(values, false, 9);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(7, 7, 6, 6, 6, 6, 0, 6, 6, 0, 7, 7, 7, 1));
        assertFalse(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer2MovesPit4() throws Exception {
        MoveInput moveInput = new MoveInput(values, false, 10);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(7, 7, 7, 6, 6, 6, 0, 6, 6, 6, 0, 7, 7, 1));
        assertFalse(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer2MovesPit5() throws Exception {
        MoveInput moveInput = new MoveInput(values, false, 11);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(7, 7, 7, 7, 6, 6, 0, 6, 6, 6, 6, 0, 7, 1));
        assertFalse(output.getFinishedWithKalah());
    }

    @Test
    public void testMoveForPlayer2MovesPit6() throws Exception {
        MoveInput moveInput = new MoveInput(values, false, 12);
        MoveOutput output = moveInput.move();
        List<Integer> actual = TestUtil.toIntegerList(output.getResultArray());
        assertThat(actual, contains(7, 7, 7, 7, 7, 6, 0, 6, 6, 6, 6, 6, 0, 1));
        assertFalse(output.getFinishedWithKalah());
    }
}
