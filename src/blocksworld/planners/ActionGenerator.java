package blocksworld.planners;
import planning.*;
import modelling.*;
import blocksworld.world.*;
import java.util.*;


/*
 * classe qui genere toutes les actions possibles d'un monde donné
 */
public class ActionGenerator {

    private boolean useRandomCosts; // Indique si des coûts aléatoires doivent être utilisés
    private Random random; // Générateur de nombres aléatoires pour les coûts
    private int cost; // Coût par défaut de chaque action
    private int numBlocks;
    private int numStacks;

    private Map<Integer, Variable> worldOn;
    private Map<Integer, BooleanVariable> worldFixed;
    private Map<Integer, BooleanVariable> worldFree;

    // Constructeur avec coût aléatoire activable
    public ActionGenerator(BlockWorld world, int numBlocks, int numStacks, boolean useRandomCosts) {
        this.numBlocks = numBlocks;
        this.numStacks = numStacks;
        this.cost = 1; // Coût par défaut
        this.useRandomCosts = useRandomCosts;
        this.random = new Random();
        this.worldOn = world.getOnB();
        this.worldFixed = world.getFixedB();
        this.worldFree = world.getfreeP();
    }

    // Constructeur sans coût aléatoire
    public ActionGenerator(BlockWorld world, int numBlocks, int numStacks) {
        this(world, numBlocks, numStacks, false);
    }

    public ActionGenerator(BlockWorld world, boolean useRandomCosts) {
        this(world, world.getNumBlocks(), world.getNumStacks(), useRandomCosts);
    }

    public ActionGenerator(BlockWorld world) {
        this(world, world.getNumBlocks(), world.getNumStacks(), false);
    }

    public int getNumBlocks() {
        return numBlocks;
    }

    public int getNumStacks() {
        return numStacks;
    }

    // Générer un coût aléatoire ou utiliser le coût par défaut
    private int getCost() {
        return useRandomCosts ? random.nextInt(10) + 1 : cost;
    }

    public void useRandomCosts(boolean b){
        this.useRandomCosts = b;
    }


    public Set<Action> generateAllActions() {

        Set<Action> actions = new HashSet<>();

        // Générer les actions pour chaque bloc
        for (int block = 0; block <= numBlocks; block++) {
            
            for (int sourceBlock = 0; sourceBlock <= numBlocks; sourceBlock++) {
                for (int targetBlock = 0; targetBlock <= numBlocks; targetBlock++) {
                    if (block != sourceBlock && block != targetBlock && sourceBlock != targetBlock) {
                        // Action 1 : Déplacer un bloc b1 du dessus d’un bloc b2 vers le dessus d’un bloc b3
                        actions.add(createActionBlockToBlock(block, sourceBlock, targetBlock));
                    }
                }
            }

            // Action 2 : Déplacer un bloc b1 du dessus d’un bloc b2 vers une pile vide p
            for (int sourceBlock = 0; sourceBlock <= numBlocks; sourceBlock++) {
                for (int pile = -1; pile >= -numStacks; pile--) {
                    if (block != sourceBlock ) {
                        actions.add(createActionBlockToFreePile(block, pile,sourceBlock));
                    }
                }
            }

            // Action 3 : Déplacer un bloc b du dessus d’une pile p vers une autre pile vide p'
            for (int sourcePile = -1; sourcePile >= -numStacks; sourcePile--) {
                for (int targetPile = -1; targetPile >= -numStacks; targetPile--) {
                    if (sourcePile != targetPile) {
                        actions.add(createActionFromFreePileToFreePile(block, sourcePile, targetPile));
                    }
                }
            }

            // Action 4 : Déplacer un bloc b du dessus d’une pile p vers le dessus d’un bloc b′
            for (int sourcePile = -1; sourcePile >= -numStacks; sourcePile--) {
                for (int targetBlock = 0; targetBlock <= numBlocks; targetBlock++) {
                    if (block != targetBlock) {
                        actions.add(createActionFromFreePileToBlock(block, sourcePile, targetBlock));
                    }
                }
            }
        }
        return actions;
    }

    // Création d'une action : Déplacer un bloc b1 du dessus d’un bloc b2 vers le dessus d’un bloc b3
    public Action createActionBlockToBlock(int block, int targetBlock,int sourceBlock) {

        Map<Variable, Object> preconditions = new HashMap<>();
        Map<Variable, Object> effects = new HashMap<>();

        // Préconditions
        preconditions.put(worldFixed.get(block), false);            // Le bloc `block` doit être non fixe
        preconditions.put(worldFixed.get(targetBlock), false);      // Le bloc `targetBlock` doit être non fixe
        preconditions.put(worldFixed.get(sourceBlock), true);      // Le bloc `sourceBlock` doit être fixe
        preconditions.put(worldOn.get(block), sourceBlock);         // `block` doit être sur `sourceblock`


        // Effets
        effects.put(worldOn.get(block), targetBlock);               // `block` est maintenant sur `targetBlock`
        effects.put(worldFixed.get(targetBlock), true);             // `targetBlock` devient fixe
        effects.put(worldFixed.get(block), false);                  // `block` devient non fixe
        effects.put(worldFixed.get(sourceBlock), false);                  // `sourceblock` devient non fixe

        return new BasicAction(preconditions, effects, getCost());
    }


    // Création d'une action : Déplacer un bloc b1 du dessus d’un bloc b2 vers une pile vide p
    public Action createActionBlockToFreePile(int block, int pile,int sourceBlock) {
        Map<Variable, Object> preconditions = new HashMap<>();
        Map<Variable, Object> effects = new HashMap<>();

        // preconditions
        preconditions.put(worldFree.get(pile), true); // la pile target doit etre libre
        preconditions.put(worldFixed.get(block), false); // le block doit etre pas fixe
        preconditions.put(worldOn.get(block), sourceBlock);// `block` doit être sur `sourceblock`
        preconditions.put(worldFixed.get(sourceBlock), true); // Le bloc `sourceBlock` doit être fixe

        // effets
        effects.put(worldFree.get(pile), false); // pile devient pas vide
        effects.put(worldOn.get(block), pile); // block devient sur pile
        effects.put(worldFixed.get(sourceBlock), false); // `sourceblock` devient non fixe
        effects.put(worldFixed.get(block), false); // `block` devient non fixe


        return new BasicAction(preconditions, effects, getCost());
    }

    // Création d'une action : Déplacer un bloc b du dessus d’une pile p vers une autre pile vide p'
    public Action createActionFromFreePileToFreePile(int block, int sourcePile, int targetPile) {
        Map<Variable, Object> preconditions = new HashMap<>();
        Map<Variable, Object> effects = new HashMap<>();
    
        preconditions.put(worldFree.get(targetPile), true); // la pile target doit etre vide
        preconditions.put(worldFree.get(sourcePile), false); // la pile courante doit etre pas vide
        preconditions.put(worldFixed.get(block), false); // le block que l'on veut deplacer doit etre deplacable
        preconditions.put(worldOn.get(block), sourcePile); // le bloc que l'on veut deplacer doit etre sur la pile courante
    
        effects.put(worldFree.get(sourcePile), true); // la pile courante devient libre apres deplacement
        effects.put(worldFree.get(targetPile), false); // la pile target devient pas libre
        effects.put(worldOn.get(block), targetPile); // le bloc courant devient on la pile target
        effects.put(worldFixed.get(block), false); // `block` devient non fixe

        return new BasicAction(preconditions, effects, getCost());
    }
    

    // Création d'une action : Déplacer un bloc b du dessus d’une pile p vers le dessus d’un bloc b′
    public Action createActionFromFreePileToBlock(int block, int sourcePile, int targetBlock) {

        Map<Variable, Object> preconditions = new HashMap<>();
        Map<Variable, Object> effects = new HashMap<>();
    
        preconditions.put(worldFixed.get(targetBlock), false); // target block doit pas etre fixe
        preconditions.put(worldFree.get(sourcePile), false); // la pile courante ne doit pas etre vide
        preconditions.put(worldFixed.get(block), false); // le block que l'on veut deplacer doit etre deplacable
        preconditions.put(worldOn.get(block), sourcePile); // le bloc que l'on veut deplacer doit etre sur la pile courante
    
        effects.put(worldOn.get(block), targetBlock); // le bloc devient sur le bloc target
        effects.put(worldFixed.get(targetBlock), true); // le bloc target devient fixe
        effects.put(worldFree.get(sourcePile), true); // la pile où etait le bloc devient libre
        effects.put(worldFixed.get(block), false); // `block` devient non fixe

        return new BasicAction(preconditions, effects, getCost());
    }
    

    public Set<Action> getApplicableActions(Map<Variable,Object> state){
        Set<Action> res= new HashSet<>();
        for ( Action a : generateAllActions()){
            if ( a.isApplicable(state) ){
                res.add(a);
            }
        }
        return res;
    }

    public static Map<Variable,Object> executePlan(Map<Variable,Object> initalState,List<Action> actions){
        Map<Variable,Object> res = new HashMap<Variable,Object>(initalState);
        for ( Action a : actions ){
            res = a.successor(res);
        }
        return res;
    }


    
}
