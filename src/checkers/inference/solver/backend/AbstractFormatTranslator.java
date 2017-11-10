package checkers.inference.solver.backend;

import checkers.inference.model.CombVariableSlot;
import checkers.inference.model.CombineConstraint;
import checkers.inference.model.ComparableConstraint;
import checkers.inference.model.ConstantSlot;
import checkers.inference.model.EqualityConstraint;
import checkers.inference.model.ExistentialConstraint;
import checkers.inference.model.ExistentialVariableSlot;
import checkers.inference.model.InequalityConstraint;
import checkers.inference.model.PreferenceConstraint;
import checkers.inference.model.RefinementVariableSlot;
import checkers.inference.model.SubtypeConstraint;
import checkers.inference.model.VariableSlot;
import checkers.inference.solver.backend.encoder.ConstraintEncoderCoordinator;
import checkers.inference.solver.backend.encoder.binary.ComparableConstraintEncoder;
import checkers.inference.solver.backend.encoder.binary.EqualityConstraintEncoder;
import checkers.inference.solver.backend.encoder.binary.InequalityConstraintEncoder;
import checkers.inference.solver.backend.encoder.binary.SubtypeConstraintEncoder;
import checkers.inference.solver.backend.encoder.combine.CombineConstraintEncoder;
import checkers.inference.solver.backend.encoder.existential.ExistentialConstraintEncoder;
import checkers.inference.solver.backend.encoder.preference.PreferenceConstraintEncoder;
import checkers.inference.solver.frontend.Lattice;
import checkers.inference.util.ConstraintVerifier;

/**
 * Abstract implementation of FormatTranslator that all concrete FormatTranslator should extend from.
 * It has the necessary lattice that will be used and already setup the abstract structure and provided
 * default implementations so that you only need to override minimum methods your FormatTranslator is
 * interested.
 */
public abstract class AbstractFormatTranslator<SlotEncodingT, ConstraintEncodingT, SlotSolutionT>
        implements FormatTranslator<SlotEncodingT, ConstraintEncodingT, SlotSolutionT>{

    /* lattice is protected because every subclass of AbstractFormatTranslator uses it.*/
    protected final Lattice lattice;
    /* verifier is private because subclasses doesn't use it. It only needs to be passed
     * as an argument to encoder factory methods*/
    private final ConstraintVerifier verifier;

    protected SubtypeConstraintEncoder<ConstraintEncodingT> subtypeConstraintEncoder;
    protected EqualityConstraintEncoder<ConstraintEncodingT> equalityConstraintEncoder;
    protected InequalityConstraintEncoder<ConstraintEncodingT> inequalityConstraintEncoder;
    protected ComparableConstraintEncoder<ConstraintEncodingT> comparableConstraintEncoder;
    protected PreferenceConstraintEncoder<ConstraintEncodingT> preferenceConstraintEncoder;
    protected CombineConstraintEncoder<ConstraintEncodingT> combineConstraintEncoder;
    protected ExistentialConstraintEncoder<ConstraintEncodingT> existentialConstraintEncoder;

    public AbstractFormatTranslator(Lattice lattice, ConstraintVerifier verifier) {
        this.lattice = lattice;
        this.verifier = verifier;
    }

    /** Remember to call this method at the end of subclass FormatTranslator constructor(MaxSAT and LogiQL) or at the last
     * step of initializing FormatTranslator - in Z3BitVector backend, it's initSolver() method. Because the creation of
     * encoders might not only need the parameter - verifier, but also need fields depending on concrete backend
     * So after those dependant fields are initialized, call this method to finish initializing encoders*/
    protected void postInit() {
        subtypeConstraintEncoder = createSubtypeConstraintEncoder(verifier);
        equalityConstraintEncoder = createEqualityConstraintEncoder(verifier);
        inequalityConstraintEncoder = createInequalityConstraintEncoder(verifier);
        comparableConstraintEncoder = createComparableConstraintEncoder(verifier);
        preferenceConstraintEncoder = createPreferenceConstraintEncoder(verifier);
        combineConstraintEncoder = createCombineConstraintEncoder(verifier);
        existentialConstraintEncoder = createExistentialConstraintEncoder(verifier);
    }

    /**Only override corresponding encoder creation method if your format translator supports encoding such constraint*/
    protected SubtypeConstraintEncoder<ConstraintEncodingT> createSubtypeConstraintEncoder(ConstraintVerifier verifier) {
        return null;
    }

    protected EqualityConstraintEncoder<ConstraintEncodingT> createEqualityConstraintEncoder(ConstraintVerifier verifier) {
        return null;
    }

    protected InequalityConstraintEncoder<ConstraintEncodingT> createInequalityConstraintEncoder(ConstraintVerifier verifier) {
        return null;
    }

    protected ComparableConstraintEncoder<ConstraintEncodingT> createComparableConstraintEncoder(ConstraintVerifier verifier) {
        return null;
    }

    protected CombineConstraintEncoder<ConstraintEncodingT> createCombineConstraintEncoder(ConstraintVerifier verifier) {
        return null;
    }

    protected PreferenceConstraintEncoder<ConstraintEncodingT> createPreferenceConstraintEncoder(ConstraintVerifier verifier) {
        return null;
    }

    protected ExistentialConstraintEncoder<ConstraintEncodingT> createExistentialConstraintEncoder(ConstraintVerifier verifier) {
        return null;
    }

    /** Caution: if subclass overrides the serialize(XXXConstraint) methods, nullness of the corresponding encoder
     * field must be checked before dereferencing unless you overrode corresponding createXXXConstraintEncoder to
     * return a non-null value. Because subclass might not support some constraint encoding and the encoders of those
     * unsupported constraints are by default null. By default, serialize(XXXConstraint) methods don't encode constraints*/
    @Override
    public ConstraintEncodingT serialize(SubtypeConstraint constraint) {
        return subtypeConstraintEncoder == null ? null :
                ConstraintEncoderCoordinator.dispatch(constraint, subtypeConstraintEncoder);
    }

    @Override
    public ConstraintEncodingT serialize(EqualityConstraint constraint) {
        return equalityConstraintEncoder == null ? null :
                ConstraintEncoderCoordinator.dispatch(constraint, equalityConstraintEncoder);
    }

    @Override
    public ConstraintEncodingT serialize(InequalityConstraint constraint) {
        return inequalityConstraintEncoder == null ? null :
                ConstraintEncoderCoordinator.dispatch(constraint, inequalityConstraintEncoder);
    }

    @Override
    public ConstraintEncodingT serialize(ComparableConstraint constraint) {
        return comparableConstraintEncoder == null ? null :
                ConstraintEncoderCoordinator.dispatch(constraint, comparableConstraintEncoder);
    }

    @Override
    public ConstraintEncodingT serialize(PreferenceConstraint constraint) {
        return constraint == null ? null :
                ConstraintEncoderCoordinator.redirect(constraint, preferenceConstraintEncoder);
    }

    @Override
    public ConstraintEncodingT serialize(CombineConstraint combineConstraint) {
        return comparableConstraintEncoder == null ? null :
                ConstraintEncoderCoordinator.dispatch(combineConstraint, combineConstraintEncoder);
    }

    @Override
    public ConstraintEncodingT serialize(ExistentialConstraint constraint) {
        return existentialConstraintEncoder == null ? null :
                ConstraintEncoderCoordinator.redirect(constraint, existentialConstraintEncoder);
    }

    /** Override serialize(XXXSlot) if your FormatTranslator subclass has a concrete logic to serialize
     * these slots. By default serialize(XXXSlot) methods don't serialize slots so return null*/
    @Override
    public SlotEncodingT serialize(VariableSlot slot) {
        return null;
    }

    @Override
    public SlotEncodingT serialize(ConstantSlot slot) {
        return null;
    }

    @Override
    public SlotEncodingT serialize(ExistentialVariableSlot slot) {
        return null;
    }

    @Override
    public SlotEncodingT serialize(RefinementVariableSlot slot) {
        return null;
    }

    @Override
    public SlotEncodingT serialize(CombVariableSlot slot) {
        return null;
    }
}
