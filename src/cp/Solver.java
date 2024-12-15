/**
 * Interface Solver
 *
 * Cette interface sert à créer des solveurs pour des problèmes de contraintes.
 * Elle a une méthode solve qui doit renvoyer une solution ou rien si y a pas de solution possible.
 *
 * @see Variable
 * @see Constraint
 */
package cp;

import java.util.*;
import modelling.*;

/**
 * Interface Solver
 *
 * C'est l'interface pour le solveur de contraintes
 */
public interface Solver {

    /**
     * Cette méthode doit essayer de résoudre le problème.
     *
     * @return un Map avec les variables et leurs valeurs si y a une solution,
     *         sinon renvoie null s'il n'y a pas de solution possible.
     */
    public Map<Variable, Object> solve();
}
