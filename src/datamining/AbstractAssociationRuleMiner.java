package datamining;

import java.util.*;
import modelling.BooleanVariable;

/**
 * Classe abstraite pour l'extraction de règles d'association.
 */
public abstract class AbstractAssociationRuleMiner implements AssociationRuleMiner {

    private BooleanDatabase database;

    /**
     * Constructeur de la classe AbstractAssociationRuleMiner.
     *
     * @param database La base de données utilisée.
     */
    public AbstractAssociationRuleMiner(BooleanDatabase database) {
        this.database = database;
    }

    /**
     * @return La base de données utilisée par le mineur.
     */
    @Override
    public BooleanDatabase getDatabase() {
        return database;
    }

    /**
     * Calcule la fréquence d'un ensemble d'items dans une collection d'itemsets.
     *
     * @param items L'ensemble d'items.
     * @param itemsets L'ensemble des itemsets.
     * @return La fréquence de l'ensemble.
     */
    public static float frequency(Set<BooleanVariable> items, Set<Itemset> itemsets) {
        for (Itemset itemset : itemsets) {
            if (itemset.getItems().equals(items)) {
                return itemset.getFrequency();
            }
        }
        throw new IllegalArgumentException("Items non trouvés dans les itemsets fréquents.");
    }

    /**
     * Calcule la confiance d'une règle d'association.
     *
     * @param premise La prémisse.
     * @param conclusion La conclusion.
     * @param itemsets L'ensemble des itemsets fréquents.
     * @return La confiance de la règle.
     */
    public static float confidence(Set<BooleanVariable> premise, Set<BooleanVariable> conclusion, Set<Itemset> itemsets) {
        Set<BooleanVariable> combined = new HashSet<>(premise);
        combined.addAll(conclusion);

        return frequency(combined, itemsets) / frequency(premise, itemsets);
    }
}
