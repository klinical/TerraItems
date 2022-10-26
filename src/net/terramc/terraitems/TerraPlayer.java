package net.terramc.terraitems;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class TerraPlayer {
    private final Player player;

    private Double mana;
    private Double maxMana;
    private Double manaRegenRate;
    private Double magicPower;

    private Double critChance;

    private Double attackPower;

    private Double physicalResistance;
    private Double magicResistance;

    private Double physicalNullificationChance;
    private Double magicNullificationChance;

    private final BossBar manaBar;
    private boolean showManaBar;
    private int manaBarTask;

    public TerraPlayer(Player player) {
        this.player = player;

        this.mana = 5.0;
        this.maxMana = 5.0;
        this.manaRegenRate = 1.0;
        this.magicPower = 1.0;
        this.critChance = 0.0;
        this.attackPower = 1.0;
        this.physicalResistance = 0.0;
        this.magicResistance = 0.0;
        this.physicalNullificationChance = 0.0;
        this.magicNullificationChance = 0.0;

        this.showManaBar = false;
        this.manaBar = Bukkit.createBossBar("Mana",
                BarColor.BLUE,
                BarStyle.SEGMENTED_20
        );
    }

    public boolean isShowingManaBar() {
        return this.showManaBar;
    }

    public void updateManaBar() {
        manaBar.setProgress(mana / maxMana);
    }

    public Double getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(Double maxMana) {
        this.maxMana = maxMana;
    }

    public void showManaBar() {
        manaBar.addPlayer(player);
        showManaBar = true;

        initManaBarTask();
    }

    public void hideManaBar() {
        manaBar.removePlayer(player);
        showManaBar = false;

        Bukkit.getScheduler().cancelTask(manaBarTask);
        manaBarTask = 0;
    }

    private void initManaBarTask() {
        manaBarTask = Bukkit.getScheduler().scheduleSyncDelayedTask(
                TerraItems.lookupTerraPlugin(),
                this::hideManaBar,
                20 * 10
        );
    }

    public void refreshManaBarDisplayDuration() {
        if (manaBarTask != 0)
            Bukkit.getScheduler().cancelTask(manaBarTask);

        initManaBarTask();
    }

    public Player getPlayer() {
        return player;
    }

    public Double getMana() {
        return mana;
    }

    public void setMana(Double mana) {
        if (mana < 0)
            this.mana = 0.0;
        else if (mana > maxMana)
            this.mana = maxMana;
        else
            this.mana = mana;

        updateManaBar();
    }

    public Double getManaRegenRate() {
        return manaRegenRate;
    }

    public void setManaRegenRate(Double manaRegenRate) {
        this.manaRegenRate = manaRegenRate;
    }

    public Double getMagicPower() {
        return magicPower;
    }

    public void setMagicPower(Double magicPower) {
        this.magicPower = magicPower;
    }

    public Double getCritChance() {
        return critChance;
    }

    public void setCritChance(Double critChance) {
        this.critChance = critChance;
    }

    public Double getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(Double attackPower) {
        this.attackPower = attackPower;
    }

    public Double getPhysicalResistance() {
        return physicalResistance;
    }

    public void setPhysicalResistance(Double physicalResistance) {
        this.physicalResistance = physicalResistance;
    }

    public Double getMagicResistance() {
        return magicResistance;
    }

    public void setMagicResistance(Double magicResistance) {
        this.magicResistance = magicResistance;
    }

    public Double getPhysicalNullificationChance() {
        return physicalNullificationChance;
    }

    public void setPhysicalNullificationChance(Double physicalNullificationChance) {
        this.physicalNullificationChance = physicalNullificationChance;
    }

    public Double getMagicNullificationChance() {
        return magicNullificationChance;
    }

    public void setMagicNullificationChance(Double magicNullificationChance) {
        this.magicNullificationChance = magicNullificationChance;
    }
}
