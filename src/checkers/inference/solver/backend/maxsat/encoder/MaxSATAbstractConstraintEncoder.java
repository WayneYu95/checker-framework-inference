package checkers.inference.solver.backend.maxsat.encoder;

import checkers.inference.solver.backend.encoder.AbstractConstraintEncoder;
import checkers.inference.solver.backend.maxsat.VectorUtils;
import checkers.inference.solver.frontend.Lattice;
import checkers.inference.util.ConstraintVerifier;
import org.sat4j.core.VecInt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import java.util.Map;
import java.util.Set;

/**
 * MaxSAT side of AbstractConstraintEncoder. It contains MaxSAT specific logic for encoding yet general enough
 * to let all concrete MaxSAT encoders share the logic inside this class.
 */
public abstract class MaxSATAbstractConstraintEncoder extends AbstractConstraintEncoder<VecInt[]> {

    private static VecInt[] EMPTY_CLAUSE = new VecInt[0];
    /** Any contradictory two clauses can be used to model contradiction. But here 1,-1 are chosen because it's simple and
     * doesn't collides with real variable id 1 since 1 is almost always reserved for ConstantSlot(because every type system must
     * have at least one real qualifier). The only exception to this assumption is when storeConstants is false when initializing
     * DefaultSlotManager, but right now all the creation of DefaultSlotManager stores constants.
     * @see checkers.inference.DefaultSlotManager#DefaultSlotManager(ProcessingEnvironment, Set, boolean)
     * */
    private static VecInt[] CONTRADICTORY_CLAUSES = new VecInt[]{VectorUtils.asVec(1), VectorUtils.asVec(-1)};

    protected final Map<AnnotationMirror, Integer> typeToInt;

    public MaxSATAbstractConstraintEncoder(Lattice lattice, ConstraintVerifier verifier, Map<AnnotationMirror, Integer> typeToInt) {
        super(lattice, verifier, EMPTY_CLAUSE, CONTRADICTORY_CLAUSES);
        this.typeToInt = typeToInt;
    }
}
