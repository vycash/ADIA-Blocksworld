package datamining;

import java.util.*;
import modelling.BooleanVariable;

/**
 * Classe représentant une règle d'association.
 */
public class AssociationRule {

    private Set<BooleanVariable> premise;
    private Set<BooleanVariable> conclusion;
    private float frequency;
    private float confidence;

    /**
     * Constructeur de la classe AssociationRule.
     *
     * @param premise L'ensemble des items formant la prémisse.
     * @param conclusion L'ensemble des items formant la conclusion.
     * @param frequency La fréquence d'apparition de la règle.
     * @param confidence La confiance de la règle.
     */
    public AssociationRule(Set<BooleanVariable> premise, Set<BooleanVariable> conclusion, float frequency, float confidence) {
        this.premise = premise;
        this.conclusion = conclusion;
        this.frequency = frequency;
        this.confidence = confidence;
    }

    /**
     * @return L'ensemble des items de la prémisse.
     */
    public Set<BooleanVariable> getPremise() {
        return premise;
    }

    /**
     * @return L'ensemble des items de la conclusion.
     */
    public Set<BooleanVariable> getConclusion() {
        return conclusion;
    }

    /**
     * @return La fréquence associée à cette règle.
     */
    public float getFrequency() {
        return frequency;
    }

    /**
     * @return La confiance associée à cette règle.
     */
    public float getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("AssociationRule : ");
        for ( BooleanVariable v : premise){
            res.append(v.getName() + ", ");
        }
        res.append("---> ");
        for ( BooleanVariable v : conclusion){
            res.append(v.getName() + ", ");

        }
        res.append(", { frequency = " + frequency + ", confidence = " + confidence + " }\n");
        return res.toString();
    }
}
