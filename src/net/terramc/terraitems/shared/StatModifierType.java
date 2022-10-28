package net.terramc.terraitems.shared;

public enum StatModifierType {
    SPELL_POWER,
    ATTACK_POWER,
    CRIT_CHANCE;

    public String getDisplayName() {
        switch (this) {
            default:
                return "Undefined";
            case SPELL_POWER:
                return "Spell power";
            case ATTACK_POWER:
                return "Attack power";
            case CRIT_CHANCE:
                return "Critical hit chance";
        }
    }
}
