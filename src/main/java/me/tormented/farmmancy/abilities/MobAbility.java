package me.tormented.farmmancy.abilities;


import me.tormented.farmmancy.FarmMancy;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class MobAbility<EntityType extends Entity> extends Ability implements Hook.Activation, Hook.Ticking, Hook.EntityDamaged, Hook.EntityDeath {

    public static final NamespacedKey MobunitionEntityKey = new NamespacedKey(FarmMancy.getInstance(), "mobunition_entity");

    public abstract Class<EntityType> getEntityClass();

    public Vector mobCenterOffset;
    protected int startTick;
    public boolean isBaby = false;

    protected MobAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        mobCenterOffset = new Vector(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void processEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().hasMetadata("FarmMancy_Projectile")) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getCause() != EntityDamageEvent.DamageCause.KILL) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onActivate(boolean visual) {
        startTick = FarmMancy.getInstance().getServer().getCurrentTick();
    }

    @Override
    public abstract void onDeactivate(boolean visual);

    @Override
    public abstract void onTick(CallerSource callerSource);

    @Override
    public void processEntityDeath(EntityDeathEvent event) {
        event.getDrops().clear();
    }
}
