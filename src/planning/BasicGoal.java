package planning;
import java.util.*;
import modelling.*;

/*
on appelle but basique un but spécifié par une instanciation partielle des variables.
Un tel but sera satisfait par un état "state" si state affecte toutes les variables de l’instanciation à la bonne valeur.
Par exemple, 
le but spécifié par (x : "a", z : "c", t : "d") est satisfait par l’état 
                s = (x : "a", y : "b", z :"c", t : "d", u : "e")
mais pas par l’état 
                s = (x : "a", y : "b", z : "d", t : "d").
*/

public class BasicGoal implements Goal{

    private Map<Variable,Object> condition;

    public BasicGoal(Map<Variable,Object> condition){
        this.condition = condition;
    }
    public Map<Variable,Object> getCondition(){
        return this.condition;
    }
    public boolean isSatisfiedBy(Map<Variable,Object> state){
        for (Map.Entry<Variable, Object> entry : condition.entrySet()) {

            Variable key = entry.getKey();
            Object value = entry.getValue();

            // si l'etat passé en paramètre ne contient pas une variable de preconditionMap ou si la valeur de cette variable
            // dans l'etat n'est pas égale à celle de preconditionMap on renvoie false car au moins une des conditions d'application de l'action ne sont pas vérifiés
            if (!state.containsKey(key) ) {
                return false;
            }
            if(!state.get(key).equals(value)){
                return false;
            }
        }
        // si toutes les conditions sont vérifiés l'action est applicable
        return true;
    }

}
