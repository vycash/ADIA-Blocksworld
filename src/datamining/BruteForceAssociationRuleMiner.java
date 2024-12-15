package datamining;
import java.util.*;
import modelling.*;

/**
 * Mineur de règles d'association utilisant la méthode de force brute.
 * Cette classe génère toutes les règles d'association possibles pour des itemsets fréquents
 * en explorant toutes les combinaisons possibles de prémisses et de conclusions.
 */
public class BruteForceAssociationRuleMiner extends AbstractAssociationRuleMiner {
    
    /**
     * Constructeur de la classe BruteForceAssociationRuleMiner.
     * 
     * @param database la base de données booléenne utilisée pour extraire les règles d'association.
     */
    public BruteForceAssociationRuleMiner(BooleanDatabase database) {
        super(database);
    }

    /**
     * Génère tous les candidats possibles de prémisses pour un ensemble d'items donné.
     * Cette méthode explore toutes les combinaisons de sous-ensembles possibles.
     * 
     * @param items l'ensemble des items pour lesquels générer les prémisses candidates.
     * @return un ensemble de sous-ensembles représentant toutes les prémisses candidates possibles.
     */
    public static Set<Set<BooleanVariable>> allCandidatePremises(Set<BooleanVariable> items) {
        Set<Set<BooleanVariable>> candidates = new HashSet<>();
        generateCandidates(items, new HashSet<>(), candidates);
        candidates.remove(items); // Enlever l'ensemble original lui-même
        return candidates;
    }

    /**
     * Méthode récursive pour générer les candidats de sous-ensembles à partir des items restants.
     * 
     * @param remainingItems les items restants à inclure dans les sous-ensembles.
     * @param currentCandidate le sous-ensemble en cours de génération.
     * @param candidates l'ensemble de tous les sous-ensembles générés jusqu'à présent.
     */
    private static void generateCandidates(Set<BooleanVariable> remainingItems, Set<BooleanVariable> currentCandidate, Set<Set<BooleanVariable>> candidates) {
        if (!currentCandidate.isEmpty()) {
            candidates.add(new HashSet<>(currentCandidate));
        }

        for (BooleanVariable item : remainingItems) {
            Set<BooleanVariable> newRemainingItems = new HashSet<>(remainingItems);
            newRemainingItems.remove(item);

            Set<BooleanVariable> newCandidate = new HashSet<>(currentCandidate);
            newCandidate.add(item);

            generateCandidates(newRemainingItems, newCandidate, candidates);
        }
    }

    /**
     * Extrait l'ensemble des règles d'association fréquentes et fiables de la base de données à partir des items fréquents extraits.
     * on a utilisé les items fréquents de Apriori pour optimiser les calculs
     * 
     * @param minFrequency la fréquence minimale pour qu'un itemset soit considéré comme fréquent.
     * @param minConfidence la confiance minimale pour qu'une règle soit considérée comme fiable.
     * @return un ensemble de règles d'association respectant les seuils de fréquence et de confiance.
     */
    @Override
    public Set<AssociationRule> extract(float minFrequency, float minConfidence) {
        Set<Itemset> frequentItemsets = new Apriori(getDatabase()).extract(minFrequency);
        Set<AssociationRule> associationRules = new HashSet<>();

        for (Itemset itemset : frequentItemsets) {
            
            Set<BooleanVariable> items = itemset.getItems();
            Set<Set<BooleanVariable>> premises = allCandidatePremises(items);

            for (Set<BooleanVariable> premise : premises) {
                Set<BooleanVariable> conclusion = new HashSet<>(items);
                conclusion.removeAll(premise);

                float ruleFrequency = frequency(items, frequentItemsets);
                float ruleConfidence = confidence(premise, conclusion, frequentItemsets);

                if (ruleFrequency >= minFrequency && ruleConfidence >= minConfidence) {
                    AssociationRule associationRule = new AssociationRule(premise, conclusion, ruleFrequency, ruleConfidence);
                    associationRules.add(associationRule);
                }
            }
        }

        return associationRules;
    }
}
