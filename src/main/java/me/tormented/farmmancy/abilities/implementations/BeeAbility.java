package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobuvertAbility;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BeeAbility extends MobuvertAbility<Bee> implements Hook.EntityInteractedByPlayer{

    @Override
    public Class<Bee> getEntityClass() {
        return Bee.class;
    }

    protected BeeAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    @Override
    public void processPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (player != getOwnerPlayer()) return;

        player.setHealth(player.getHealth() - 8f);

        Vector velocityVector = player.getVelocity();
        velocityVector.setY(2f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 240, 1));
        player.setVelocity(velocityVector);
    }
}
