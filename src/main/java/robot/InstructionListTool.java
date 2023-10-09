package robot;

import java.util.ArrayList;
import java.util.List;

import static robot.Instruction.TURNLEFT;
import static robot.Instruction.TURNRIGHT;

public class InstructionListTool {

    static List<Instruction> compacte(List<Instruction> instructions) {
        return compacte1(compacte2(instructions));
    }

    static List<Instruction> compacte1(List<Instruction> instructions) {
        List<Instruction> copieCompacte = new ArrayList<Instruction>();
        List<Instruction> instructionsEnAttente = new ArrayList<Instruction>();
        boolean isTurnLeft = false;
        boolean isTurnRight = false;
        for (int i = 0; i < instructions.size(); i++) {
            if ((instructions.get(i) == TURNRIGHT && isTurnLeft)|| (instructions.get(i) == TURNLEFT && isTurnRight)) {
                instructionsEnAttente.clear();
                isTurnLeft = false;
                isTurnRight = false;
            } else if (instructions.get(i) == TURNRIGHT) {
                copieCompacte.addAll(instructionsEnAttente);
                instructionsEnAttente.clear();
                instructionsEnAttente.add(TURNRIGHT);
                isTurnRight = true;
            } else if (instructions.get(i) == TURNLEFT) {
                copieCompacte.addAll(instructionsEnAttente);
                instructionsEnAttente.clear();
                instructionsEnAttente.add(TURNLEFT);
                isTurnLeft = true;
            } else {
                copieCompacte.addAll(instructionsEnAttente);
                instructionsEnAttente.clear();
                isTurnLeft = false;
                isTurnRight = false;
                copieCompacte.add(instructions.get(i));
            }
        }
        copieCompacte.addAll(instructionsEnAttente);
        return copieCompacte;
    }

    static List<Instruction> compacte2(List<Instruction> instructions) {
        List<Instruction> copieCompacte = new ArrayList<Instruction>();
        List<Instruction> instructionsEnAttente = new ArrayList<Instruction>();
        for (int i = 0; i < instructions.size(); i++) {
            if (instructions.get(i) == TURNRIGHT && instructionsEnAttente.size() == 2) {
                instructionsEnAttente.clear();
                copieCompacte.add(TURNLEFT);
            } else if (instructions.get(i) == TURNRIGHT)
                instructionsEnAttente.add(TURNRIGHT);
            else {
                copieCompacte.addAll(instructionsEnAttente);
                instructionsEnAttente.clear();
                copieCompacte.add(instructions.get(i));
            }
        }
        copieCompacte.addAll(instructionsEnAttente);
        return copieCompacte;
    }

    static <T> List<T> concatene(List<T> trace, T coordinates) {
        ArrayList<T> coordonnees = new ArrayList<T>(trace);
        coordonnees.add(coordinates);
        return coordonnees;
    }
}
