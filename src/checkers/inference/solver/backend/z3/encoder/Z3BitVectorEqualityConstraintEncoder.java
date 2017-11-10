package checkers.inference.solver.backend.z3.encoder;

import checkers.inference.model.ConstantSlot;
import checkers.inference.model.Slot;
import checkers.inference.model.VariableSlot;
import checkers.inference.solver.backend.encoder.binary.EqualityConstraintEncoder;
import checkers.inference.solver.backend.z3.Z3BitVectorFormatTranslator;
import checkers.inference.solver.frontend.Lattice;
import checkers.inference.util.ConstraintVerifier;
import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Optimize;

public class Z3BitVectorEqualityConstraintEncoder extends Z3BitVectorAbstractConstraintEncoder implements EqualityConstraintEncoder<BoolExpr> {

    public Z3BitVectorEqualityConstraintEncoder(Lattice lattice, ConstraintVerifier verifier, Context context,
                                                Optimize solver, Z3BitVectorFormatTranslator z3BitVectorFormatTranslator) {
        super(lattice, verifier, context, solver, z3BitVectorFormatTranslator);
    }

    protected BoolExpr encode(Slot fst, Slot snd) {
        BitVecExpr varBv1 = fst.serialize(z3BitVectorFormatTranslator);
        BitVecExpr varBv2 = snd.serialize(z3BitVectorFormatTranslator);
        return context.mkEq(varBv1, varBv2);
    }

    @Override
    public BoolExpr encodeVariable_Variable(VariableSlot fst, VariableSlot snd) {
        return encode(fst, snd);
    }

    @Override
    public BoolExpr encodeVariable_Constant(VariableSlot fst, ConstantSlot snd) {
        return encode(fst, snd);
    }

    @Override
    public BoolExpr encodeConstant_Variable(ConstantSlot fst, VariableSlot snd) {
        return encode(fst, snd);
    }

    @Override
    public BoolExpr encodeConstant_Constant(ConstantSlot fst, ConstantSlot snd) {
        return verifier.areEqual(fst, snd) ? emptyValue : contradictoryValue;
    }
}
