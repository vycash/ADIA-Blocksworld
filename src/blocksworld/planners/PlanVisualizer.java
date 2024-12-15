package blocksworld.planners;
import blocksworld.world.*;
import javax.swing.JFrame;
import bwmodel.BWStateBuilder;
import bwmodel.BWState;
import bwui.BWIntegerGUI;
import bwui.BWComponent;
import planning.*;
import java.util.*;
import modelling.*;


/*
 * classe qui permet de visualiser un plan en interface graphique
 */
public class PlanVisualizer {

    // Visualisation d'un plan avec animation
    public static void visualizePlan(Map<Variable, Object> initState, List<Action> plan, int numBlocks, String title) {
        int actualNumBlocks = getActualNumBlocks(initState, numBlocks);

        // Initialisation de l'interface graphique
        BWIntegerGUI gui = new BWIntegerGUI(actualNumBlocks);
        JFrame frame = new JFrame(title);

        // Création de l'état initial pour l'affichage
        BWState<Integer> bwState = makeBWState(initState, actualNumBlocks);
        BWComponent<Integer> component = gui.getComponent(bwState);

        // Configuration de la fenêtre
        frame.add(component);
        frame.setSize(600, 400); // Dimensions élargies
        frame.setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
        frame.setVisible(true);

        // Animation du plan
        Map<Variable, Object> currentState = initState;
        for (Action action : plan) {
            try {
                Thread.sleep(700); // Pause pour chaque étape
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Appliquer l'action et mettre à jour l'état
            currentState = action.successor(currentState);

            // Mettre à jour l'affichage graphique
            component.setState(makeBWState(currentState, actualNumBlocks));
        }

        System.out.println("Simulation du plan : terminée.");
    }

    // Méthode pour construire un BWState à partir d'une instanciation
    private static BWState<Integer> makeBWState(Map<Variable, Object> instanciation, int numBlocks) {
        BWStateBuilder<Integer> builder = BWStateBuilder.makeBuilder(numBlocks);

        for (int b = 0; b <= numBlocks; b++) {
            Variable onB = new Variable("on" + b);
            Object underObj = instanciation.get(onB);

            if (underObj instanceof Integer) {
                int under = (Integer) underObj;
                if (under >= 0) { // Si la valeur représente un bloc
                    builder.setOn(b, under);
                }
            }
        }

        return builder.getState();
    }

    // Méthode pour obtenir le nombre réel de blocs
    private static int getActualNumBlocks(Map<Variable, Object> initState, int defaultNumBlocks) {
        int maxBlock = defaultNumBlocks - 1;

        for (Variable var : initState.keySet()) {
            if (var.getName().startsWith("on")) {
                int blockNum = Integer.parseInt(var.getName().substring(2));
                maxBlock = Math.max(maxBlock, blockNum);
            }
        }

        return maxBlock + 1; // Inclure le bloc avec l'indice maximal
    }
}
