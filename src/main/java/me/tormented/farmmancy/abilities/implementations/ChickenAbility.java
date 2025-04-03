package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChickenAbility extends MobunitionAbility<Chicken> implements Hook.EntityInteractedByPlayer {

    @Override
    public Class<Chicken> getEntityClass() {
        return Chicken.class;
    }

    protected ChickenAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        mobCenterOffset = new Vector(0.0f, 1.0f, 0.0f);
        modRingRadius = 0f;
    }

    @Override
    public void processPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (player != getOwnerPlayer()) return;

        Chicken chicken = (Chicken) event.getRightClicked();
        entities.remove(chicken);

        Location loc = player.getLocation();
        Vector direction = loc.getDirection();
        Location targetLoc = loc.add(direction.multiply(1));

        chicken.teleport(targetLoc);
        chicken.setHealth(0);

        Vector velocityVector = player.getVelocity();
        velocityVector.setY(1.1f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 80, 1));
        player.setVelocity(velocityVector);
    }
}
