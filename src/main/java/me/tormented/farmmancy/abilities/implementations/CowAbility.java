package me.tormented.farmmancy.abilities.implementations;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.FarmMancer.FarmMancer;
import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CowAbility extends MobunitionAbility<Cow> implements Hook.PlayerInteraction, Hook.EntityMoved, Hook.EntityDeath {


    @Override
    public Class<Cow> getEntityClass() {
        return Cow.class;
    }

    public CowAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        mobCenterOffset = new Vector(0.0f, 1.0f, 0.0f);
        modRingRadius = 3f;
    }

    @Override
    public void processPlayerInteract(PlayerInteractEvent event) {
        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (heldItem.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE)) {
            Player player = event.getPlayer();

            if (player != getOwnerPlayer()) return;

            if (entities.isEmpty()) {
                return;
            }

            Cow flingingCow = entities.getLast();


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
    public void processEntityMove(EntityMoveEvent event) {
        Block currentBlock = event.getEntity().getLocation().getBlock();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    Block block = currentBlock.getRelative(x, y, z);
                    if (block.getType() != Material.AIR) {
                        Location location = event.getEntity().getLocation();
                        World world = location.getWorld();
                        world.createExplosion(location, 4.0F);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void processEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata("FarmMancy_Projectile")) {
            event.getDrops().clear();
            event.getEntity().removeMetadata("FarmMancy_Projectile", FarmMancy.getInstance());
        }
        if (EventDistributor.getInstance().entityMobunitionAbilityMap.containsKey(event.getEntity())) {
            event.getDrops().clear();
        }
    }
}
