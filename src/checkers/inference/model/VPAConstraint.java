package checkers.inference.model;

import java.util.Arrays;
import org.checkerframework.javacutil.BugInCF;

/**
 * Represents a constraint that the viewpoint adaptation between
 * target and decl gives result.
 */
public class VPAConstraint extends Constraint {

    private final Slot target;
    private final Slot decl;
    private final VPAVariableSlot result;

    private VPAConstraint(Slot target, Slot decl, VPAVariableSlot result, AnnotationLocation location) {
        super(Arrays.asList(target, decl, result), location);
        this.target = target;
        this.decl = decl;
        this.result = result;
    }

    protected static VPAConstraint create(Slot target, Slot decl, VPAVariableSlot result,
            AnnotationLocation location) {
        if (target == null || decl == null || result == null) {
            throw new BugInCF("Create combine constraint with null argument. Target: "
                    + target + " Decl: " + decl + " Result: " + result);
        }

        return new VPAConstraint(target, decl, result, location);
    }

    @Override
    public <S, T> T serialize(Serializer<S, T> serializer) {
        return serializer.serialize(this);
    }

    public Slot getTarget() {
        return target;
    }

    public Slot getDeclared() {
        return decl;
    }

    public VPAVariableSlot getResult() {
        return result;
    }

    @Override
    public int hashCode() {
        int hc = 1;
        hc += ((target == null) ? 0 : target.hashCode());
        hc += ((decl == null) ? 0 : decl.hashCode());
        hc += ((result == null) ? 0 : result.hashCode());
        return hc;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VPAConstraint other = (VPAConstraint) obj;
        if (target.equals(other.target) &&
                decl.equals(other.decl) &&
                result.equals(other.result)) {
            return true;
        } else {
            return false;
        }
    }
}