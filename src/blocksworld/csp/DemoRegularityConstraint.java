package blocksworld.csp;
import blocksworld.world.*;


/**
 *  classe qui fait démonstration de recherche d'un état qui satisfait les contraintes de Régularité
 */
public class DemoRegularityConstraint {
    public static void main(String[] args){

        int b = 6;
        int s = 3;

        BlockWorldWithConstraints w = new BlockWorldWithRegularityConstraint(b, s);

        DemoCSP d = new DemoCSP(w,"contrainte de Regularité");
        d.demo();
    }
}
