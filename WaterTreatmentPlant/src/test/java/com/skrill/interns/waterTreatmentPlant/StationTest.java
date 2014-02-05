/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class StationTest {
    StatisticsLogger logger = mock(StatisticsLogger.class);

    @Test
    public void when_incrementPipeUsage_increments_the_pipe_the_pipe_field_is_incremented() throws Exception {
        // GIVEN

        Pipe firstPipe = new Pipe(1);
        Pipe secondPipe = new Pipe(2);
        Station st = new Station(1, "Name", "localhost", "1234");
        st.setFirstPipe(firstPipe);
        st.setSecondPipe(secondPipe);
        // WHEN
        st.incrementPipeUsage();
        // THEN
        boolean state = (firstPipe.getUsed() == 1);
        assertTrue(state);
    }

    @Test
    public void when_first_pipe_direction_is_true_second_is_false() throws Exception {
        // GIVEN
        Pipe firstPipe = new Pipe(1);
        Pipe secondPipe = new Pipe(2);
        Station st = new Station(1, "Name", "localhost", "1234");
        st.setFirstPipe(firstPipe);
        st.setSecondPipe(secondPipe);
        firstPipe.setInput(true);
        // WHEN
        st.adjustFlow(firstPipe, secondPipe);
        // THEN
        assertFalse(secondPipe.isInput());
    }

    @Test
    public void when_first_pipe_direction_is_true_second_is_true() throws Exception {
        // GIVEN
        Pipe firstPipe = new Pipe(1);
        Pipe secondPipe = new Pipe(2);
        Station st = new Station(1, "Name", "localhost", "1234");
        st.setFirstPipe(firstPipe);
        st.setSecondPipe(secondPipe);
        firstPipe.setInput(false);
        // WHEN
        st.adjustFlow(firstPipe, secondPipe);
        // THEN
        assertTrue(secondPipe.isInput());
    }

    @Test
    public void when_twoPipesAreUsed_pipes_in_different_direction_asser_working_is_true() throws Exception {
        // GIVEN
        Pipe firstPipe = new Pipe(1);
        firstPipe.setUsed(1);
        firstPipe.setInput(true);
        Pipe secondPipe = new Pipe(2);
        secondPipe.setUsed(1);
        secondPipe.setInput(false);
        Station station = new Station(1, "Name", "localhost", "1234");
        station.setFirstPipe(firstPipe);
        station.setSecondPipe(secondPipe);
        // WHEN
        boolean actual = station.bothPipesAreUsed();
        // THEN
        assertTrue(actual);
    }

    @Test
    public void when_noPipesAreUsed_set_firstPipe_input_and_secondPipe_output() throws Exception {
        // GIVEN
        Pipe firstPipe = new Pipe(1);
        firstPipe.setUsed(0);
        Pipe secondPipe = new Pipe(2);
        secondPipe.setUsed(0);
        Station station = new Station(1, "Name", "localhost", "1234");
        station.setFirstPipe(firstPipe);
        station.setSecondPipe(secondPipe);

        // WHEN
        station.noPipesAreUsed();

        // THEN
        assertTrue(station.getFirstPipe().isInput());
        assertFalse(station.getSecondPipe().isInput());
    }

    @Test
    public void when_onePipesisUsed_pipes_in_different_direction_assert_working_is_true() throws Exception {
        // GIVEN
        Pipe firstPipe = new Pipe(1);
        firstPipe.setUsed(1);
        firstPipe.setInput(true);
        Pipe secondPipe = new Pipe(2);
        secondPipe.setUsed(0);
        Station station = new Station(1, "Name", "localhost", "1234");
        // WHEN
        boolean actual = station.onePipeIsUsed();
        // THEN
        assertTrue(actual);
    }

}
