package net.terramc.terraitems.shared;

public class StatModifier {
    private final StatModifierType modifierType;
    private final int amount;

    public StatModifier(StatModifierType modifierType, int amount) {
        this.modifierType = modifierType;
        this.amount = amount;
    }

    public StatModifierType getModifierType() {
        return modifierType;
    }

    public int getAmount() {
        return amount;
    }

    public String getDisplayName() {
        return modifierType.getDisplayName();
    }
}
