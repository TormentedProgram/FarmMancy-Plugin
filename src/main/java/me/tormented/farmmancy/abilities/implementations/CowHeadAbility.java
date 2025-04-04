package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancer.FarmMancer;
import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.utils.AbilityHeadDisplay;
import me.tormented.farmmancy.utils.HeadProvider;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CowHeadAbility extends MobunitionAbility<ItemDisplay> implements Hook.PlayerInteraction {

    @Override
    public Class<ItemDisplay> getEntityClass() {
        return ItemDisplay.class;
    }

    public CowHeadAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        modRingRadius = 3f;
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/63d621100fea5883922e78bb448056448c983e3f97841948a2da747d6b08b8ab");

    @Override
    public void processPlayerInteract(PlayerInteractEvent event) {
        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (heldItem.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE)) {
            Player player = event.getPlayer();

            if (player != getOwnerPlayer()) return;

            if (entities.isEmpty()) {
                return;
            }

            ItemDisplay flingingCow = entities.getLast();

            entities.remove(flingingCow);

            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(modRingRadius));

            flingingCow.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
            flingingCow.teleport(targetLoc);

            Vector velocity = direction.multiply(1.0);

            flingingCow.setVelocity(velocity);
        }
    }

    @Override
    public ItemDisplay spawnEntity(Location location) {
        return AbilityHeadDisplay.finalizeSpawn(super.spawnEntity(location), headProvider);
    }
}
