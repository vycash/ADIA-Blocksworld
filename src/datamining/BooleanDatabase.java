package datamining;

import java.util.*;
import modelling.*;

/**
 * Classe représentant une base de données booléenne contenant un ensemble d'items
 * et une liste de transactions (chaque transaction est un ensemble d'items présents).
 */
public class BooleanDatabase {

    private Set<BooleanVariable> items;
    private List<Set<BooleanVariable>> transactions;

    /**
     * Constructeur de la classe BooleanDatabase.
     * Initialise la base de données avec un ensemble d'items et une liste vide de transactions.
     *
     * @param items L'ensemble des items qui composent la base de données.
     */
    public BooleanDatabase(Set<BooleanVariable> items) {
        this.items = items;
        this.transactions = new ArrayList<>();
    }

    /**
     * Ajoute une transaction à la base de données.
     *
     * @param transaction L'ensemble d'items à ajouter comme transaction.
     * @throws IllegalArgumentException si la transaction contient des items non présents dans la base.
     */
    public void add(Set<BooleanVariable> transaction) {
        if (!items.containsAll(transaction)) {
            // Trouver les items qui ne sont pas présents dans la base
            Set<Object> missingItems = new HashSet<>(transaction);
            missingItems.removeAll(items); // Garde seulement les éléments manquants
            
            throw new IllegalArgumentException(
                "La transaction contient des items non présents dans la base : \n" + missingItems
            );
        }
        
        transactions.add(transaction);
    }

    /**
     * @return L'ensemble des items de la base de données.
     */
    public Set<BooleanVariable> getItems() {
        return items;
    }

    /**
     * @return La liste des transactions dans la base de données.
     */
    public List<Set<BooleanVariable>> getTransactions() {
        return transactions;
    }

    /**
     * @return La taille de l'ensemble des items dans la base de données.
     */
    public int size() {
        return this.items.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BooleanDatabase:\nItems: ").append(items).append("\nTransactions:\n");
        for (Set<BooleanVariable> transaction : transactions) {
            sb.append("  ").append(transaction).append("\n");
        }
        return sb.toString();
    }
}
