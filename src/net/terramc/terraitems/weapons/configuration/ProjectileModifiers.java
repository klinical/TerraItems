package net.terramc.terraitems.weapons.configuration;

public class ProjectileModifiers {
    private Integer projectileDamage;
    private Integer projectileKnockback;
    private Integer reloadSpeed;


    public ProjectileModifiers() {
        this.projectileDamage = 5;
        this.projectileKnockback = 1;
        this.reloadSpeed = 3;
    }
    public Integer getProjectileDamage() {
        return projectileDamage;
    }

    public void setProjectileDamage(Integer projectileDamage) {
        this.projectileDamage = projectileDamage;
    }

    public Integer getProjectileKnockback() {
        return projectileKnockback;
    }

    public void setProjectileKnockback(Integer projectileKnockback) {
        this.projectileKnockback = projectileKnockback;
    }

    public Integer getReloadSpeed() {
        return reloadSpeed;
    }

    public void setReloadSpeed(Integer reloadSpeed) {
        this.reloadSpeed = reloadSpeed;
    }
}
