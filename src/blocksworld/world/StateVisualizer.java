package blocksworld.world;
import java.util.*;
import bwmodel.*;
import bwui.*;
import javax.swing.JFrame;
import modelling.Variable;


/*
 * classe qui permet de visualiser un état soit en terminal ou en interface graphique
 */
public class StateVisualizer {
    public static void afficheState(Map<Variable, Object> state) {
        System.out.println("État du monde des blocs :");
        System.out.println("-------------------------");
    
        // Préparer une map pour organiser les blocs par pile ou bloc en dessous
        Map<Integer, List<Integer>> piles = new HashMap<>();
    
        // Remplir la map avec chaque bloc placé sur un autre bloc ou une pile
        for (Map.Entry<Variable, Object> entry : state.entrySet()) {
            Variable variable = entry.getKey();
            Object value = entry.getValue();
            System.out.println(variable.toString() + " , " + value);
    
            if (variable.getName().startsWith("on")) {
                int blockId = Integer.parseInt(variable.getName().substring(2));
                int belowId = (Integer) value;
    
                piles.putIfAbsent(belowId, new ArrayList<>());
                piles.get(belowId).add(blockId);
            }
        }
    
        System.out.println("-------------------------");
    
        // Construire l'affichage pour chaque pile
        for (int pileId = -1; pileId >= -BlockWorld.numStacks ; pileId--) {
            StringBuilder sb = new StringBuilder();
            sb.append("Pile ").append(pileId).append(" : ");
    
            // Si la pile a des blocs, les afficher
            if (piles.containsKey(pileId)) {
                int current = pileId;
                while (piles.containsKey(current) && !piles.get(current).isEmpty()) {
                    int nextBlock = piles.get(current).remove(0);
                    sb.append("").append(nextBlock).append(" -> ");
                    current = nextBlock;
                }
                // Supprimer la flèche finale
                if (sb.toString().endsWith(" -> ")) {
                    sb.setLength(sb.length() - 4);
                }
            } else {
                sb.append(""); // Indiquer que la pile est vide
            }
    
            System.out.println(sb);
        }
    
        System.out.println("-------------------------");
    }
    

    public static void afficheStatGUI(String title, BlockWorld world, Map<Variable, Object> etat) {
        // Utiliser uniquement les blocs présents dans l'état
        int n = world.getNumBlocks() + 1; // n est le nombre total de blocs, mais on filtre plus tard
        BWStateBuilder<Integer> builder = BWStateBuilder.makeBuilder(n);
    
        for (int b = 0; b < n; b++) {
            Variable onB = world.getOn(b); // Get instance of Variable for "on_b"
            Object position = etat.get(onB);
    
            // Vérifier que le bloc a une position définie dans l'état avant de l'afficher
            if (etat.containsKey(onB) && position instanceof Integer) {
                int underB = (int) position;
                // Si la valeur est un bloc (par opposition à une pile), l'ajouter à l'affichage
                if (underB >= 0) {
                    builder.setOn(b, underB);
                }
            }
        }
    
        BWState<Integer> state = builder.getState();
    
        // Affichage graphique de l'état actuel
        BWIntegerGUI gui = new BWIntegerGUI(n);
        JFrame frame = new JFrame(title);
    
        // Définir les dimensions plus grandes
        frame.add(gui.getComponent(state));
        frame.setSize(400, 300); // Dimensions élargies
        frame.setLocation(200,200); // Position personnalisée
        frame.setVisible(true);
    }
    
    
    
}
