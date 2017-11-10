package checkers.inference.solver.backend.z3.encoder;

import checkers.inference.solver.backend.encoder.AbstractConstraintEncoder;
import checkers.inference.solver.backend.z3.Z3BitVectorFormatTranslator;
import checkers.inference.solver.frontend.Lattice;
import checkers.inference.util.ConstraintVerifier;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Optimize;

/**
 * Z3 side AbstractConstraintEncoder. It contains necessary references to Context, Optimize and Z3BitVectorFormatTranslator
 * objects to aid encoding constraints. This part of logic is shared by all Z3 constraint encoders.
 */
public class Z3BitVectorAbstractConstraintEncoder extends AbstractConstraintEncoder<BoolExpr> {

    protected final Context context;
    protected final Optimize solver;
    protected final Z3BitVectorFormatTranslator z3BitVectorFormatTranslator;

    public Z3BitVectorAbstractConstraintEncoder(Lattice lattice, ConstraintVerifier verifier, Context context,
                                                Optimize solver, Z3BitVectorFormatTranslator z3BitVectorFormatTranslator) {
        super(lattice, verifier, context.mkTrue(), context.mkFalse());
        this.context = context;
        this.solver = solver;
        this.z3BitVectorFormatTranslator = z3BitVectorFormatTranslator;
    }
}
