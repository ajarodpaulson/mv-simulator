package com.mvsim.model;

public abstract class Setting {
    private final String name;
    private final Units units;
    private Number value;
    private float min;
    private float max;
    private float stepSize;

    /**
     * Constructor for a setting.
     * @param staticName  static name property of the concrete class
     * @param staticUnits static unit property of the concrete class
     * @param value
     */
    protected /*
     * XXX: There is no compiler enforcement of the pattern you are using to
     * instantiate your concrete classes. Needs work.
     */
    Setting(String staticName, Units staticUnits, Number value, float min, float max, float stepSize) {
        this.name = staticName;
        this.units = staticUnits;
        this.value = value;
        this.min = min;
        this.max = max;
        this.stepSize = stepSize;
    }

    public String getName() {
        return name;
    }

    public Units getUnits() {
        return units;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

	public float getMin() {
		return min;
	}

    public float getMax() {
        return max;
    }

    public float getStepSize() {
        return stepSize;
    }
}
