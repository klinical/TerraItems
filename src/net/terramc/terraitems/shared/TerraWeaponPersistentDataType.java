package net.terramc.terraitems.shared;

import com.google.gson.Gson;
import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.weapons.configuration.WeaponConfiguration;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class TerraWeaponPersistentDataType implements PersistentDataType<String, WeaponConfiguration> {

    public static final TerraWeaponPersistentDataType DATA_TYPE = new TerraWeaponPersistentDataType();
    private static final Gson gson = TerraItems.getGson();

    @NotNull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @NotNull
    @Override
    public Class<WeaponConfiguration> getComplexType() {
        return WeaponConfiguration.class;
    }

    @NotNull
    @Override
    public String toPrimitive(
            @NotNull WeaponConfiguration weapon,
            @NotNull PersistentDataAdapterContext persistentDataAdapterContext
    ) {
        return gson.toJson(weapon);
    }

    @NotNull
    @Override
    public WeaponConfiguration fromPrimitive(
            @NotNull String weaponString,
            @NotNull PersistentDataAdapterContext persistentDataAdapterContext
    ) {
        return gson.fromJson(weaponString, WeaponConfiguration.class);
    }
}
