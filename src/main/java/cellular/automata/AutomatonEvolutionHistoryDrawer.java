package cellular.automata;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AutomatonEvolutionHistoryDrawer extends Canvas {
    private static final int CELL_SIZE = 4;
    private static final int CANVAS_WIDTH = 1200;
    private static final int CANVAS_HEIGHT = 500;

    private List<List<Integer>> board = new ArrayList<>();

    public AutomatonEvolutionHistoryDrawer() {
        setBackground(Color.BLACK);
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    @Override
    public void paint(Graphics graphics) {
        graphics.setColor(Color.CYAN);

        int generation = 0;
        for (final List<Integer> outer : board) {
            for (final Integer xpos : outer) {
                graphics.fillRect(xpos, generation, CELL_SIZE, CELL_SIZE);
            }
            generation += CELL_SIZE;
        }
    }

    public void setBoard(List<List<Integer>> board) {
        this.board = board;
    }
}
