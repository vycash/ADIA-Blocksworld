package blocksworld.world;
import java.util.*;
import modelling.*;


/**
 *  classe representant un monde avec des contraintes de régularité
 */
public class BlockWorldWithRegularityConstraint extends BlockWorldWithConstraints{
    
    protected Set<Constraint> regularityConstraints;

    /**
     * Constructeur de la classe CustomBlocksWorldWithRegularConstraints.
     *
     * @param numBlocks Le nombre total de blocs dans le monde des blocs.
     * @param numStacks Le nombre total de piles dans le monde des blocs.
     */
    public BlockWorldWithRegularityConstraint(int numBlocks, int numStacks) {
        super(numBlocks, numStacks);

        this.regularityConstraints = new HashSet<>();

        Set<Integer> domainSet = new HashSet<>();

        for (Integer val : this.freeP.keySet()) {
            domainSet.add(val);
        }

        Set<Object> piles = new HashSet<>();
        for ( int i=1 ; i <= numStacks ; i++){
            piles.add(-i);
        }


        for (int i = 0; i <= numBlocks; i++) {
            Variable blockOnI = this.getOn(i); // La variable "on" pour le bloc i

            // Parcourir toutes les valeurs possibles pour le domaine de "on(i)"
            for (Object o : blockOnI.getDomain()) {
                int j = (Integer) o; // Bloc ou pile sur lequel i est posé

                // Vérifier que j est un bloc (pas une pile)
                if (j >= 0) {
                    Variable blockOnJ = this.getOn(j); // La variable "on" pour le bloc j

                    // Construire le domaine valide pour blockOnJ
                    Set<Object> validDomain = new HashSet<>();
                    for (Object k : blockOnJ.getDomain()) {
                        if (k instanceof Integer) {
                            int next = (Integer) k;

                            // Vérifier si la différence entre i, j et next est constante
                            if ((j - i) == (next - j)) {
                                validDomain.add(next);
                            }
                        }
                    }

                    for (int k = -numStacks; k < 0; k++) { // Inclure piles dans le domaine valide
                        validDomain.add(k);
                    }

                    // Ajouter la contrainte d'implication pour la régularité
                    Constraint regularityConstraint = new Implication(blockOnI, Set.of(j), blockOnJ, validDomain);
                    regularityConstraints.add(regularityConstraint);
                }
            }
        }




    }


    /**
     * Obtient l'ensemble des contraintes régulières.
     *
     * @return L'ensemble des contraintes régulières.
     */
    public Set<Constraint> getRegularityConstraints() {
        return this.regularityConstraints;
    }

    public Set<Constraint> getConstraints(){
        Set<Constraint> result = new HashSet<>(super.getConstraints());
        result.addAll(regularityConstraints);
        return result;
    }

    public boolean isStateValid(Map<Variable, Object> state){
        return super.isStateValid(state) && isRegularitySatisfiedByState(state);
    }

    public boolean isRegularitySatisfiedByState(Map<Variable, Object> state) {
        for (Constraint constraint : this.regularityConstraints) {
            if (state.keySet().containsAll(constraint.getScope()) && !constraint.isSatisfiedBy(state)) {
                System.out.println("Contrainte de régularité et increase non satisfaite : " + constraint + " par : ");
                //afficheState(state);
                return false;
            }
        }
        return true;
    }



}
