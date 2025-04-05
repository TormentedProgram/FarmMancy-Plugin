package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancer.FarmMancer;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PigAbility extends MobunitionAbility<Pig> implements Hook.EntityInteractedByPlayer, Hook.PlayerJoining {

    @Override
    public Class<Pig> getEntityClass() {
        return Pig.class;
    }

    public PigAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        modRingRadius = 3f;
    }

    private static final float healingValue = 5f;

    @Override
    public void onDeactivate(boolean visual) {
        super.onDeactivate(visual);
        if (getOwnerPlayer() instanceof Player player) {
            AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
            if (attribute != null) {
                attribute.setBaseValue(attribute.getDefaultValue());
            }
        }
    }

    @Override
    public void processPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (player != getOwnerPlayer()) return;

        if (player.getAttribute(Attribute.MAX_HEALTH) instanceof AttributeInstance healthAttribute) {
            if (player.getHealth() < healthAttribute.getValue()) {
                ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
                if (heldItem.getType().isAir() ||
                        !heldItem.hasItemMeta() ||
                        !heldItem.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE)) {

                    Pig pig = (Pig) event.getRightClicked();
                    headDisplays.remove(pig);
                    Location loc = player.getLocation();
                    Vector direction = loc.getDirection();
                    Location targetLoc = loc.add(direction.multiply(1));
                    pig.teleport(targetLoc);
                    pig.setHealth(0);
                    player.heal(healingValue);
                }
            }
        }
    }

    @Override
    public void onTick(CallerSource callerSource) {
        super.onTick(callerSource);

        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() instanceof Player player) {
            AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
            if (attribute != null) {
                attribute.setBaseValue(attribute.getDefaultValue() + (headDisplays.size() * 2.5));
            }
        }
    }

    @Override
    public void processPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(attribute.getDefaultValue());
        }
    }

    @Override
    public void processPlayerQuit(PlayerQuitEvent event) {

    }
}
