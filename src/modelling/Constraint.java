package modelling;

import java.util.*;

public interface Constraint {
    // l'ensemble des variables sur lesquelles porte la contrainte
    public Set<Variable> getScope();
    public boolean isSatisfiedBy(Map<Variable,Object> map);

}
