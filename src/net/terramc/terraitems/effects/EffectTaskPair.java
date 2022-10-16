package net.terramc.terraitems.effects;

public class EffectTaskPair {
    private int effectTaskId;
    private int cancelEffectTaskId;

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
