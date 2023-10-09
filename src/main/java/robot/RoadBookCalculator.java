package robot;

import java.util.*;

import static robot.Direction.*;
import static robot.Instruction.*;
import static robot.MapTools.clockwise;
import static robot.MapTools.nextForwardPosition;

public class RoadBookCalculator {

    RoadBook calculateRoadBook(LandSensor sensor, Direction direction, Coordinates position, Coordinates destination) throws LandSensorDefaillance, UndefinedRoadbookException {
        if (position.equals(destination)) return new RoadBook(Collections.EMPTY_LIST);
        List<Direction> privilegedDirections = calculeDirectionsPrivilégiées(direction, position, destination);
        List<Direction> directionsToExplored = new ArrayList<Direction>(Arrays.asList(NORTH, SOUTH, EAST, WEST));
        List<Instruction> instructions = new ArrayList<>();
        List<Coordinates> trace = new ArrayList<>();
        return exploreAllDirections(sensor, direction, position, destination, privilegedDirections, directionsToExplored, instructions, trace);
    }

    private RoadBook calculateSuiteRoadBook(LandSensor sensor, Direction direction, Coordinates position, Coordinates destination, List<Instruction> instructions, List<Coordinates> trace) throws LandSensorDefaillance, UndefinedRoadbookException {
        if (position.equals(destination)) return new RoadBook(InstructionListTool.compacte(instructions));
        List<Direction> privilegedDirections = calculeDirectionsPrivilégiées(direction, position, destination);
        privilegedDirections.remove(Direction.oppositeDirection(direction));
        List<Direction> directionsToExplored = new ArrayList<Direction>(Arrays.asList(NORTH, SOUTH, EAST, WEST));
        directionsToExplored.remove(Direction.oppositeDirection(direction));
        return exploreAllDirections(sensor, direction, position, destination, privilegedDirections, directionsToExplored, instructions, trace);
    }

    private RoadBook exploreAllDirections(LandSensor sensor, Direction direction, Coordinates position, Coordinates destination, List<Direction> privilegedDirections, List<Direction> directionsToExplored, List<Instruction> instructions, List<Coordinates> trace) throws LandSensorDefaillance, UndefinedRoadbookException {
        while (!directionsToExplored.isEmpty()) {
            if ((privilegedDirections.contains(direction) || privilegedDirections.isEmpty() && directionsToExplored.contains(direction))
                    && sensor.isAccessible(nextForwardPosition(position, direction)) && !trace.contains(nextForwardPosition(position, direction))) {
                try {
                    directionsToExplored.remove(direction);
                    privilegedDirections.remove(direction);
                    Coordinates nextPos = nextForwardPosition(position, direction);
                    return calculateSuiteRoadBook(sensor, direction, nextPos, destination, InstructionListTool.concatene(instructions, FORWARD), InstructionListTool.concatene(trace, nextPos));
                } catch (UndefinedRoadbookException e) {
                }
            } else {
                privilegedDirections.remove(direction);
                directionsToExplored.remove(direction);
                instructions.add(TURNRIGHT);
                direction = clockwise(direction);
            }
        }
        throw new UndefinedRoadbookException();
    }

    private List<Direction> calculeDirectionsPrivilégiées(Direction direction, Coordinates position, Coordinates destination) {
        List<Direction> directionList = new ArrayList<Direction>();
        if (destination.getY() > position.getY()) directionList.add(Direction.SOUTH);
        if (destination.getY() < position.getY()) directionList.add(Direction.NORTH);
        if (destination.getX() < position.getX()) directionList.add(WEST);
        if (destination.getX() > position.getX()) directionList.add(Direction.EAST);
        return directionList;
    }

}