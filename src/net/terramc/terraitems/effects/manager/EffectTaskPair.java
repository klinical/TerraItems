package net.terramc.terraitems.effects.manager;

public class EffectTaskPair {
    private final int effectTaskId;
    private final int cancelEffectTaskId;

    public EffectTaskPair(int effectTaskId, int cancelEffectTaskId) {
        this.effectTaskId = effectTaskId;
        this.cancelEffectTaskId = cancelEffectTaskId;
    }

    public int getEffectTaskId() {
        return effectTaskId;
    }

    public int getCancelEffectTaskId() {
        return cancelEffectTaskId;
    }
}
