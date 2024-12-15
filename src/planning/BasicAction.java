package planning;
import java.util.*;
import modelling.*;

/*
on appelle action basique une action dont la précondition preconditionMap et l’effet effect sont des instanciations partielles des variables ( MAPS ). 
Une telle action sera applicable dans un état MAP si MAP affecte toutes les variables de preconditionMap à la bonne valeur. 
Son effet dans MAP consistera à créer un nouvel état MAPprime, égal à MAP sauf pour les variables presentes dans effect, 
qui auront dans MAPprime′ la valeur prescrite par effect 
*/

public class BasicAction implements Action{

    private Map<Variable,Object> preconditionMap;
    private Map<Variable,Object> effectMap;


    private int cost;

    public BasicAction(Map<Variable,Object> preconditionMap,Map<Variable,Object> effect,int cost) {
        this.preconditionMap = preconditionMap;
        this.effectMap = effect;
        this.cost = cost;
    }

    public int getCost(){ return this.cost; }
    public Map<Variable, Object> getPreconditionMap() {
        return preconditionMap;
    }

    public Map<Variable, Object> getEffectMap() {
        return effectMap;
    }

    /*
    Pour que l'action soit applicable il faut que la map de precondition aye les memes entrées 
    key:value avec la map d'entrée càd que la map de precondition est plus petite ou égale à la map 
    d'entrée sinon l'action n'est pas applicable, donc preconditionMap.size() =< entryMap.size()
    Donc on va itérer sur la preconditionMap pour des raisons de compléxité
    */
    public boolean isApplicable(Map<Variable, Object> state) {
        for (Map.Entry<Variable, Object> entry : preconditionMap.entrySet()) {

            Variable key = entry.getKey();
            Object value = entry.getValue();

            // si l'etat passé en paramètre ne contient pas une variable de preconditionMap ou si la valeur de cette variable
            // dans l'etat n'est pas égale à celle de preconditionMap, on renvoie false car au moins une des conditions d'application de l'action ne sont pas vérifiés
            if (!state.containsKey(key) ) {
                return false;
            }
            if(!state.get(key).equals(value)){
                return false;
            }
        }
        // si toutes les conditions sont vérifiées, l'action est applicable
        return true;
    }

    /*
    on cree un nouvel etat avec les valeurs de effectMap sans changer les valeurs des autres variables qui ne sont pas presents 
    dans effectMap 
    */
    public Map<Variable,Object> successor(Map<Variable,Object> state){

        //On crée la map de retour, successorMap avec les entrées de l'entrée
        Map<Variable,Object> successorMap = new HashMap<>(state);

        //On itère sur la map d'effet et on change la valeur de la variable dans le succèsseurMap
        for(Map.Entry<Variable,Object> entry : effectMap.entrySet()){

            Variable key = entry.getKey();
            Object new_value = entry.getValue();

            // on met à jour les valeurs dans l'etat de succession
            successorMap.put(key, new_value);
            
        }
        return successorMap;
    
    }

@Override
public String toString() {
    StringBuilder res = new StringBuilder("Action needs ==>\n");
    
    for (Map.Entry<Variable, Object> entry : preconditionMap.entrySet()) {
        Variable key = entry.getKey();
        Object value = entry.getValue();
        res.append("    { ").append(key).append(" = ").append(value).append(" }   \n");
    }
    
    res.append("Action leads to ==>\n");
    for (Map.Entry<Variable, Object> entry : effectMap.entrySet()) {
        Variable key = entry.getKey();
        Object value = entry.getValue();
        res.append("    { ").append(key).append(" = ").append(value).append(" }   \n");
    }
    
    return res.toString();
}

    

}
