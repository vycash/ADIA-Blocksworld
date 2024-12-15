package blocksworld.world;
import java.util.*;
import modelling.*;



/**
 *  classe representant un monde avec des contraintes de base
 */
public class BlockWorldWithConstraints extends BlockWorld{

    protected Set<Constraint> diffConstraints;
    protected Set<Constraint> impConstraints;

    public BlockWorldWithConstraints(int numberOfBlocks, int numberOfPiles) {
        
        super(numberOfBlocks, numberOfPiles);
        this.diffConstraints = new HashSet<>();
        this.impConstraints = new HashSet<>();

        Iterator<Map.Entry<Integer, Variable>> onBIterator = this.onB.entrySet().iterator();

        /* Pour chaque onBlock, on examine toutes les valeurs possibles (le domaine) de onBlock. Par exemple, 
        si le domaine est {0, 1, -1}, cela signifie que onBlock peut être positionné sur le bloc 0, le bloc 1, ou la pile -1. */
        while (onBIterator.hasNext()) {

            Map.Entry<Integer, Variable> onBEntry = onBIterator.next();

            Variable onBlock = onBEntry.getValue();

            for ( Object o : onBlock.getDomain() ){

                Integer i = (Integer) o; // numéro de bloc, peut etre positif pour bloc ou négatif pour une pile

                Set<Object> s1 = Set.of(i);
                Set<Object> s2 = new HashSet<>();

                Variable v;

                /*Si i est négatif, cela indique qu’on place onBlock sur une pile (valeur dans freeP), et donc on crée une contrainte
                d'implication indiquant que si onBlock est sur la pile i, alors la pile ne sera plus libre (s2.add(false)). 
                si bloc est sur -1 cela implique que pile -1 n'est pas free*/
                if (i < 0) {
                    v = this.freeP.get(i);
                    s2.add(false);
                } 
                /*Si i est positif, cela signifie que onBlock est sur un autre bloc, et une contrainte est ajoutée pour 
                indiquer que le bloc en dessous (représenté par v) doit être fixe (s2.add(true)). 
                si bloc b sur bloc b2 -> b2 est fixe*/
                else {
                    v = this.fixedB.get(i);
                    s2.add(true);
                }
                this.impConstraints.add(new Implication(onBlock, s1, v, s2));
            }
        }

        /*Pour chaque paire de blocs (i, j), une contrainte de différence est ajoutée entre onB.get(i) et onB.get(j), 
        ajoutée à diffConstraints.* */

        // Générer les contraintes de différence pour tous les blocs
        for (int i = 0; i <= numBlocks; i++) {
            for (int j = 0; j <= numBlocks; j++) {
                if (i != j) {
                    this.diffConstraints.add(new DifferenceConstraint(this.onB.get(i), this.onB.get(j)));
                }
            }
        }

    }

    public Set<Constraint> getConstraints(){
        Set<Constraint> constraints = new HashSet<Constraint>(diffConstraints);
        constraints.addAll(impConstraints);
        return constraints;
    }

    /**
     * Vérifie si un état satisfait toutes les contraintes.
     *
     * @param state l'état à vérifier, représenté par une map de variables à leurs valeurs.
     * @return true si l'état satisfait toutes les contraintes, false sinon.
     */
    public boolean isStateValid(Map<Variable, Object> state) {

        // Vérification des implications
        for (Constraint constraint : this.impConstraints) {
            if (state.keySet().containsAll(constraint.getScope()) && !constraint.isSatisfiedBy(state)) {
                System.out.println("Implication non satisfaite : " + constraint + " par : ");
                StateVisualizer.afficheState(state);
                return false;
            }
        }

        return true;
    }
    




}


