package blocksworld.world;
import java.util.*;
import modelling.Variable;
import modelling.BooleanVariable;

/**
 * Cette classe représente un monde de blocs.
   @author QACH Yahya, MORABET Ahmed
 */
public class BlockWorld {
    
    protected Map<Integer, Variable> onB;
    protected Map<Integer, BooleanVariable> fixedB;
    protected Map<Integer, BooleanVariable> freeP;
    protected static int numBlocks, numStacks;
    protected Set<Object> domain;

    /**
     * Constructeur de la classe BlocksWorldState.
     * @param numBlocks Le nombre total de blocs dans le monde des blocs.
     * @param numStacks Le nombre total de piles dans le monde des blocs.
     */
    public BlockWorld(int numBlocks, int numStacks) {
        this.onB = new HashMap<>();
        this.fixedB = new HashMap<>();
        this.freeP = new HashMap<>();

        this.numBlocks = numBlocks;
        this.numStacks = numStacks;

        Set<Integer> domain = new HashSet<>();
        for ( int i = -numStacks ; i <= numBlocks ; i++ ){
            domain.add(i);
        }
        this.domain = new HashSet<>(domain);

        int j = 0;
        while (j <= numBlocks) {
            Set<Object> actualDomain = new HashSet<>(domain);
            actualDomain.remove(j);
            this.onB.put(j, new Variable("on" + j, actualDomain));
            this.fixedB.put(j, new BooleanVariable("fixed" + j));
            j++;
        }

        int k = -1;
        while (k >= -numStacks) {
            this.freeP.put(k, new BooleanVariable("free" + k));
            k--;
        }
    }

    public Set<Object> domain(){
        return this.domain;
    }


    public int getNumBlocks() {
        return numBlocks;
    }

    public int getNumStacks() {
        return numStacks;
    }


    /**
     * Obtient l'ensemble des variables de l'état du monde des blocs.
     *
     * @return L'ensemble des variables.
     */
    public Set<Variable> getVariables() {
        Set<Variable> variables = new HashSet<>(this.onB.values());
        variables.addAll(this.fixedB.values());
        variables.addAll(this.freeP.values());
        return variables;
    }

    public Variable getOn(int bloc) {
        for (Map.Entry<Integer, Variable> entry : onB.entrySet()) {
            if (entry.getKey().equals(bloc)) {
                return entry.getValue();
            }
        }
        return null; // Return null if the bloc is not found
    }

    /**
     * @return La map des blocs sur d'autres blocs.
     */
    public Map<Integer, Variable> getOnB() {
        return this.onB;
    }

    /**
     * @return La map des blocs fixes.
     */
    public Map<Integer, BooleanVariable> getFixedB() {
        return this.fixedB;
    }

    /**
     * @return La map des piles vides.
     */
    public Map<Integer, BooleanVariable> getfreeP() {
        return this.freeP;
    }

    public Map<Variable, Object> createState(List<List<Integer>> piles) {

        if (piles.size() > numStacks) {
            throw new IllegalArgumentException("Le nombre de piles doit être exactement égal à numStacks : " + numStacks);
        }
    
        Map<Variable, Object> state = new HashMap<>();
    
        for (int pileIndex = 0; pileIndex < piles.size(); pileIndex++) {
            List<Integer> pile = piles.get(pileIndex);
            int pileId = -1 - pileIndex; // IDs de piles négatifs
    
            // Vérification si la variable pour cette pile existe dans le monde
            if (!freeP.containsKey(pileId)) {
                throw new IllegalArgumentException("Pile " + pileId + " n'existe pas dans le monde des blocs.");
            }
    
            if (pile.isEmpty()) {
                // Marquer la pile comme libre si elle est vide
                state.put(freeP.get(pileId), true);
            } else {
                // Marquer la pile comme occupée
                state.put(freeP.get(pileId), false);
    
                // Définir le bloc au sommet de la pile
                int topBlock = pile.get(0);
    
                if (!onB.containsKey(topBlock)) {
                    throw new IllegalArgumentException("Bloc " + topBlock + " n'existe pas dans le monde des blocs.");
                }
    
                state.put(onB.get(topBlock), pileId);
    
                // Parcourir les blocs de la pile
                for (int i = 1; i < pile.size(); i++) {
                    int currentBlock = pile.get(i);
                    int blockBelow = pile.get(i - 1);
    
                    if (!onB.containsKey(currentBlock) ) {
                        throw new IllegalArgumentException("Bloc " + currentBlock + " n'existe pas dans le monde des blocs.");
                    }
                    if (!fixedB.containsKey(blockBelow)){
                        throw new IllegalArgumentException("Bloc " + blockBelow + " n'existe pas dans le monde des blocs.");
                    }
    
                    state.put(onB.get(currentBlock), blockBelow);
                    state.put(fixedB.get(blockBelow), true); // Fixer le bloc en dessous
                }
    
                // Le dernier bloc de la pile n'est pas fixé
                int lastBlock = pile.get(pile.size() - 1);
                if (!fixedB.containsKey(lastBlock)) {
                    throw new IllegalArgumentException("Bloc " + lastBlock + " n'existe pas dans le monde des blocs.");
                }
                state.put(fixedB.get(lastBlock), false);
            }
        }
    
        return state;
    }
    
    
    public Map<Variable, Object> createRandomState() {
        List<List<Integer>> piles = new ArrayList<>();
        Random random = new Random();
    
        // Initialiser les piles
        for (int i = 0; i < numStacks; i++) {
            piles.add(new ArrayList<>());
        }
    
        // Mélanger les blocs aléatoirement dans les piles
        List<Integer> blocks = new ArrayList<>();
        for (int i = 0; i <= numBlocks; i++) { // Génère les blocs de 0 à numBlocks
            blocks.add(i);
        }
        Collections.shuffle(blocks, random);
    
        // Distribuer les blocs dans les piles aléatoirement
        for (int block : blocks) {
            int randomPileIndex = random.nextInt(numStacks);
            piles.get(randomPileIndex).add(block);
        }
    
        // Appeler la méthode createState pour générer l'état
        return createState(piles);
    }
    

    
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("BlockWorld : \n");
        sb.append("------------------------\n");

        // Affiche l'état des variables onB
        sb.append("Positions possibles des blocs (onB):\n");
        for (Map.Entry<Integer, Variable> entry : onB.entrySet()) {
            Variable variable = entry.getValue();
            sb.append(variable.getName()).append(" sur: ").append(variable.getDomain()).append("\n");
        }
        
        // Affiche l'état des variables fixedB
        sb.append("\nÉtat possibles des blocs fixes (fixedB):\n");
        for (Map.Entry<Integer, BooleanVariable> entry : fixedB.entrySet()) {
            BooleanVariable variable = entry.getValue();
            sb.append(variable.getName()).append(" fixé: ").append(variable.getDomain()).append("\n");
        }
        
        // Affiche l'état des piles libres (freeP)
        sb.append("\nÉtat possibles des piles libres (freeP):\n");
        for (Map.Entry<Integer, BooleanVariable> entry : freeP.entrySet()) {
            BooleanVariable variable = entry.getValue();
            sb.append(variable.getName()).append(" libre: ").append(variable.getDomain()).append("\n");
        }

        sb.append("------------------------\n");
        return sb.toString();
    }

    
    




}

