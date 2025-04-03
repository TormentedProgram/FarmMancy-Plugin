package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import org.bukkit.Location;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CowAbility extends MobunitionAbility<Cow> implements Hook.PlayerInteraction {


    @Override
    public Class<Cow> getEntityClass() {
        return Cow.class;
    }

    protected CowAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        mobCenterOffset = new Vector(0.0f, 1.0f, 0.0f);
        modRingRadius = 3f;
    }

    @Override
    public void processPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player != getOwnerPlayer()) return;

        if (entities.isEmpty()) {
            return;
        }

        Cow flingingCow = entities.getLast();

        if (!flingingCow.hasMetadata("FarmMancy_OwnedMob")) {
            return;
        }

        entities.remove(flingingCow);

        Location loc = player.getLocation();
        Vector direction = loc.getDirection();
        Location targetLoc = loc.add(direction.multiply(modRingRadius));

        flingingCow.removeMetadata("FarmMancy_OwnedMob", FarmMancy.getInstance());
        flingingCow.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
        flingingCow.teleport(targetLoc);

        Vector velocity = direction.multiply(1.0);

        flingingCow.setVelocity(velocity);
    }
}
