package robot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static robot.Direction.*;
import static robot.Instruction.*;

public class RoadBookCalculatorUnitTest {
    RoadBook book;
    Coordinates startPosition;
    RoadBookCalculator calculator;
    private LandSensor sensor;

    @Before
    public void setUp(){
        book = null;
        startPosition = new Coordinates(1,1);
        calculator = new RoadBookCalculator();
        sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
    }


    @Test
    public void testCalculateAtDestination() throws Exception {
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, startPosition);
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionNorthRoad()  throws Exception {
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(1, 0));
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionSouthRoad()  throws Exception {
        book = calculator.calculateRoadBook(sensor, SOUTH, startPosition, new Coordinates(1, 2));
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionEastRoad()  throws Exception {
        book = calculator.calculateRoadBook(sensor, EAST, startPosition, new Coordinates(2, 1));
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionWestRoad() throws Exception  {
        book = calculator.calculateRoadBook(sensor, WEST, startPosition, new Coordinates(0, 1));
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateStraightForwardRoad()  throws Exception {
        book = calculator.calculateRoadBook(sensor, WEST, startPosition, new Coordinates(-4, 1));
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateNEroad()  throws Exception {
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(2, 0));
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateNWroad()  throws Exception {
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(0, 0));
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertEquals(TURNLEFT, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateSEroad()  throws Exception {
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(2, 2));
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateSWroad()  throws Exception {
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(0, 2));
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }





    @Test
    public void testRouteSimple1() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, 0))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(3, 1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(5, -2))).thenReturn(false);
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5, -5));
        List<Instruction> expected = new ArrayList<Instruction>(Arrays.asList(TURNRIGHT, FORWARD, TURNLEFT, FORWARD, FORWARD, FORWARD, TURNRIGHT, FORWARD, FORWARD, TURNLEFT, FORWARD, FORWARD, FORWARD, TURNRIGHT, FORWARD));
        while (book.hasInstruction()) {
            Assert.assertEquals(expected.remove(0), book.next());
        }
        Assert.assertTrue(expected.isEmpty());
    }

    @Test
    public void testRouteSimple2() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, 0))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(5, -2))).thenReturn(false);
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5, -5));
        List<Instruction> expected = new ArrayList<Instruction>(Arrays.asList(TURNRIGHT, FORWARD, FORWARD, FORWARD, FORWARD, TURNLEFT, FORWARD, FORWARD, TURNRIGHT, FORWARD, TURNLEFT, FORWARD, FORWARD, FORWARD, FORWARD, TURNLEFT, FORWARD));
        while (book.hasInstruction()) {
            Assert.assertEquals(expected.remove(0), book.next());
        }
        Assert.assertTrue(expected.isEmpty());
    }

    @Test
    public void testRouteCulDeSac() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, -6))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -5))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -5))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -4))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -4))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, 0))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, 0))).thenReturn(false);
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5, -5));
        List<Instruction> expected = new ArrayList<Instruction>(Arrays.asList(TURNRIGHT, FORWARD, FORWARD, FORWARD, FORWARD, TURNLEFT, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD));
        while (book.hasInstruction()) {
            Assert.assertEquals(expected.remove(0), book.next());
        }
        Assert.assertTrue(expected.isEmpty());
    }

    @Test(expected = UndefinedRoadbookException.class)
    public void testArriveeInaccessible() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        cartographie(sensor, startPosition, 2);
        when(sensor.isAccessible(new Coordinates(0, 0))).thenReturn(false);
        calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(0, 0));
    }

    @Test(expected = UndefinedRoadbookException.class, timeout = 10000)
    public void testArriveeHorsCarte() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        cartographie(sensor, startPosition, 3);
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(10, 10));
    }

    @Test(expected = UndefinedRoadbookException.class, timeout = 10000)
    public void testArriveeIsolee() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        cartographie(sensor, startPosition, 4);
        when(sensor.isAccessible(new Coordinates(3, -1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(4, -2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(3, -3))).thenReturn(false);
        book = calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(3, -2));
    }

    @Test(expected = UndefinedRoadbookException.class)
    public void testArriveeHorsCarteSurCarteReduite() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(1, 0))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, -1))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, -2))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(2, -2))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(3, -2))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(3, -1))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(3, 0))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(2, 0))).thenReturn(true);
        calculator.calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5, -5));
    }


    private void cartographie(LandSensor sensor, Coordinates startPosition, int distance) {
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(false);
        for (int i = startPosition.getX() - distance; i < startPosition.getX() + distance + 1; i++) {
            for (int j = startPosition.getY() - distance; j < startPosition.getY() + distance + 1; j++) {
                when(sensor.isAccessible(new Coordinates(i, j))).thenReturn(true);
            }
        }
    }

}
