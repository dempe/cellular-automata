package cellular.automata;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Integer.toBinaryString;
import static org.apache.commons.lang3.StringUtils.reverse;
import static org.apache.commons.lang3.StringUtils.rightPad;

public class CellularAutomata {
    private static final int ITERATIONS = 100;
    private static final int STARTING_X_POSITION = 150;
    private static final int PRODUCTION_RULE_SET_COUNT = 256;
    private static final int RULE_SET_SIZE = 8;

    private final AutomatonEvolutionHistoryDrawer automatonEvolutionHistoryDrawer = new AutomatonEvolutionHistoryDrawer();

    private boolean randomInitialConditions = false;

    public static void main(String[] args) throws InterruptedException {
        final CellularAutomata ca = new CellularAutomata();

        ca.setRandomInitialConditions(true);
        ca.run();
    }

    public void run() throws InterruptedException {
        setupCanvas();

//        automatonEvolutionHistoryDrawer.setBoard(buildCellLocations(generateProductionRuleSets().get(30)));
//        automatonEvolutionHistoryDrawer.repaint();

        for (final Map.Entry<Integer, List<Boolean>> ruleSet : generateProductionRuleSets().entrySet()) {
            automatonEvolutionHistoryDrawer.setBoard(buildCellLocations(ruleSet.getValue()));
            automatonEvolutionHistoryDrawer.repaint();
            System.out.println(ruleSet.getKey());
            Thread.sleep(1000);
        }
    }

    private static Map<Integer, List<Boolean>> generateProductionRuleSets() {
        final Map<Integer, List<Boolean>> productionRuleSets = new HashMap<>();

        for (int i = 0; i < PRODUCTION_RULE_SET_COUNT; i++) {
            productionRuleSets.put(i, buildRuleSetFromInteger(i));
        }

        return productionRuleSets;
    }

    private static List<Boolean> buildRuleSetFromInteger(final int ruleSetNumber) {
        final List<Boolean> ruleSet = new ArrayList<>(RULE_SET_SIZE);
        final String normalizedRules = rightPad(reverse(toBinaryString(ruleSetNumber)), RULE_SET_SIZE, "0");

        for (int i = 0; i < RULE_SET_SIZE; i++) {
            if (normalizedRules.charAt(i) == '0') {
                ruleSet.add(false);
            } else {
                ruleSet.add(true);
            }
        }

        return ruleSet;
    }

    private void setupCanvas() {
        final Frame frame = new Frame();

        frame.add(automatonEvolutionHistoryDrawer);
        frame.setLayout(new FlowLayout());
        frame.setSize(automatonEvolutionHistoryDrawer.getWidth(), automatonEvolutionHistoryDrawer.getHeight());
        frame.setVisible(true);
    }

    private List<List<Integer>> buildCellLocations(final List<Boolean> productionRules) {
        final List<List<Integer>> cellLocations = new ArrayList<>();
        List<Boolean> list = buildProgenitor();

        for (int generationNumber = 0; generationNumber < ITERATIONS; generationNumber++) {
            cellLocations.add(generateXLocations(list));
            list = generateNextGen(list, productionRules);
        }

        return cellLocations;
    }

    private static List<Boolean> generateNextGen(final List<Boolean> previousGeneration, final List<Boolean> productionRules) {
        if (previousGeneration == null) { return  null; }

        final List<Boolean> nextGen = new ArrayList<>(previousGeneration.size());

        for (int i = 0; i < previousGeneration.size(); i++) {
            final Boolean left = i == 0 ? false : previousGeneration.get(i - 1);
            final Boolean center = previousGeneration.get(i);
            final Boolean right = i == previousGeneration.size() - 1 ? false : previousGeneration.get(i + 1);

            nextGen.add(generateCell(left, right, center, productionRules));
        }

        return nextGen;
    }

    private static List<Integer> generateXLocations(final List<Boolean> list) {
        final List<Integer> generation = new ArrayList<>();

        int xpos = STARTING_X_POSITION;
        for (final Boolean bool : list) {
            if (bool) {
                generation.add(xpos);
            }

            xpos += 4;
        }

        return generation;
    }

    private static Boolean generateCell(
            final Boolean left,
            final Boolean right,
            final Boolean center,
            final List<Boolean> productionRules) {
        if (!left && !center && !right) { return productionRules.get(0); }
        if (left && !center && !right) { return productionRules.get(1); }
        if (!left && center && !right) { return productionRules.get(2); }
        if (left && center && !right) { return productionRules.get(3); }
        if (!left && !center && right) { return productionRules.get(4); }
        if (left && !center && right) { return productionRules.get(5); }
        if (!left && center && right) { return productionRules.get(6); }
        return productionRules.get(7); // will always be true
    }

    private List<Boolean> buildProgenitor() {
        final List<Boolean> list = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < 200; i++) {
            if (randomInitialConditions) {
                list.add(random.nextBoolean());
            }
            if (i == 100) {
                list.add(true);
                continue;
            }
            list.add(false);
        }

        return list;
    }

    public void setRandomInitialConditions(boolean randomInitialConditions) {
        this.randomInitialConditions = randomInitialConditions;
    }
}
