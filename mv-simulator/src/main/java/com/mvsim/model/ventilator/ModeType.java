package com.mvsim.model.ventilator;

public class ModeType {
    ControlVariableType controlVariableType;
    BreathSequenceType breathSequenceType;
    TargetingSchemeType targetingSchemeType;

    public ModeType(ControlVariableType cv, BreathSequenceType seq, TargetingSchemeType targ) {
        this.controlVariableType = cv;
        this.breathSequenceType = seq;
        this.targetingSchemeType = targ;   
    }

    @Override
    public String toString() {
        return controlVariableType.getName() + "-" + breathSequenceType.getName() + targetingSchemeType.getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((controlVariableType == null) ? 0 : controlVariableType.hashCode());
        result = prime * result + ((breathSequenceType == null) ? 0 : breathSequenceType.hashCode());
        result = prime * result + ((targetingSchemeType == null) ? 0 : targetingSchemeType.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModeType other = (ModeType) obj;
        if (controlVariableType != other.controlVariableType)
            return false;
        if (breathSequenceType != other.breathSequenceType)
            return false;
        if (targetingSchemeType != other.targetingSchemeType)
            return false;
        return true;
    }
}
