package net.terramc.terraitems.weapons.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WeaponEffects {
    @NotNull private final List<WeaponEffectConfiguration> effectList;

    public WeaponEffects() {
        effectList = new ArrayList<>();
    }

    @NotNull public List<WeaponEffectConfiguration> get() {
        return effectList;
    }

    public void add(WeaponEffectConfiguration effect) {
        effectList.add(effect);
    }
}
