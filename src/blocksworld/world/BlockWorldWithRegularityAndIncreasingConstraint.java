package blocksworld.world;
import modelling.*;
import java.util.*;
import blocksworld.Constants;
/**
 *  classe representant un monde avec des contraintes de régularité et de croissance
 */
public class BlockWorldWithRegularityAndIncreasingConstraint extends BlockWorldWithRegularityConstraint {
    
    private Set<Constraint> increasingConstraints;

    public BlockWorldWithRegularityAndIncreasingConstraint(int numBlocks,int numStacks){
        super(numBlocks, numStacks);
        increasingConstraints = new HashSet<>();

        // Contraintes de croissance
        for (int i = 0; i <= numBlocks; i++) {
            Variable blockOnI = this.getOn(i); // La variable "on" pour le bloc i

            // Générer le domaine valide : tous les blocs ou piles inférieurs à i
            Set<Object> validDomain = new HashSet<>();
            for (int k = -numStacks; k < i; k++) { // Inclure piles et blocs inférieurs à i
                validDomain.add(k);
            }

            // Ajouter la contrainte : "on(i)" doit appartenir au domaine valide
            Constraint growthConstraint = new UnaryConstraint(blockOnI, validDomain);
            increasingConstraints.add(growthConstraint);
        }
    }

    public BlockWorldWithRegularityAndIncreasingConstraint(){
        this(Constants.numBlocks,Constants.numStacks);
    }


    public Set<Constraint> getIncreasingConstraints() {
        return this.increasingConstraints;
    }

    public Set<Constraint> getConstraints(){
        Set<Constraint> result = new HashSet<>(super.getConstraints());
        result.addAll(increasingConstraints);
        return result;
    }

    public boolean isStateValid(Map<Variable, Object> state){
        return super.isStateValid(state) && isSatisfiedByState(state);
    }

    public boolean isSatisfiedByState(Map<Variable, Object> state) {
        for (Constraint constraint : this.increasingConstraints) {
            if (state.keySet().containsAll(constraint.getScope()) && !constraint.isSatisfiedBy(state)) {
                System.out.println("Contrainte de régularité et increase non satisfaite : " + constraint + " par : ");
                //afficheState(state);
                return false;
            }
        }
        return true;
    }

}
