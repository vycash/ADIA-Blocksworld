package planning;
import java.util.*;
import modelling.*;


public interface Heuristic {
    float estimate(Map<Variable, Object> state);
}
