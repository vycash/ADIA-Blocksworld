package datamining;

import java.util.*;
import modelling.BooleanVariable;

/**
 * Classe représentant un itemset (ensemble d'items avec une fréquence associée).
 */
public class Itemset {

    private Set<BooleanVariable> items;
    private float frequency;

    /**
     * Constructeur de la classe Itemset.
     *
     * @param items L'ensemble d'items de cet itemset.
     * @param frequency La fréquence d'apparition de cet itemset (entre 0 et 1).
     */
    public Itemset(Set<BooleanVariable> items, float frequency) {
        if (frequency < 0.0 || frequency > 1.0) {
            throw new IllegalArgumentException("La fréquence doit être comprise entre 0.0 et 1.0.");
        }
        this.items = items;
        this.frequency = frequency;
    }

    /**
     * @return L'ensemble des items de cet itemset.
     */
    public Set<BooleanVariable> getItems() {
        return items;
    }

    /**
     * @return La fréquence associée à cet itemset.
     */
    public float getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return "Itemset : " + items + " , Frequency : " + frequency + "\n";
    }
}
