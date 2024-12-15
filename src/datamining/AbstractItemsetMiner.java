package datamining;

import java.util.*;
import modelling.BooleanVariable;

/**
 * Classe abstraite représentant un miner d'itemsets fréquents.
 */
public abstract class AbstractItemsetMiner implements ItemsetMiner {

    private BooleanDatabase database;
    public static final Comparator<BooleanVariable> COMPARATOR = Comparator.comparing(BooleanVariable::getName);


    /**
     * Constructeur de la classe AbstractItemsetMiner.
     *
     * @param database La base de données utilisée pour l'extraction.
     */
    public AbstractItemsetMiner(BooleanDatabase database) {
        this.database = database;
    }

    /**
     * @return La base de données booléenne associée.
     */
    @Override
    public BooleanDatabase getDatabase() {
        return database;
    }

    /**
     * Calcule la fréquence d'un ensemble d'items dans la base de données.
     *
     * @param items L'ensemble des items.
     * @return La fréquence de cet ensemble.
     */
    public float frequency(Set<BooleanVariable> items) {
        List<Set<BooleanVariable>> transactions = database.getTransactions();
        float count = 0;

        for (Set<BooleanVariable> transaction : transactions) {
            if (transaction.containsAll(items)) {
                count++;
            }
        }

        return count / transactions.size();
    }
}
