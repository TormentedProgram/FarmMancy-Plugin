package me.tormented.farmmancy;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.FarmMancer.FarmMancer;
import me.tormented.farmmancy.FarmMancer.TickingCow;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class EntityListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().hasMetadata("FarmMancy_OwnedMob")) {
            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                    && event.getCause() != EntityDamageEvent.DamageCause.KILL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(attribute.getDefaultValue());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Bee bee && bee.hasMetadata("FarmMancy_OwnedMob")) {
            Player player = event.getPlayer();
            for (FarmMancer cowMancer : TickingCow.getInstance().CowMancers) {
                if (cowMancer._player == player) {
                    cowMancer.beeAbility(event);
                }
            }
        }
        if (event.getRightClicked() instanceof Cow cow) {
            Player player = event.getPlayer();
            for (FarmMancer cowMancer : TickingCow.getInstance().CowMancers) {
                if (cowMancer._player == player) {
                    if (!cow.hasMetadata("FarmMancy_OwnedMob") && !cow.hasMetadata("FarmMancy_Projectile")) {
                        cow.remove();
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                        cowMancer.createCows(1, !cow.isAdult());
                    }
                }
            }
        }
        if (event.getRightClicked() instanceof Strider strider) {
            Player player = event.getPlayer();
            for (FarmMancer cowMancer : TickingCow.getInstance().CowMancers) {
                if (cowMancer._player == player) {
                    if (!strider.hasMetadata("FarmMancy_OwnedMob") && !strider.hasMetadata("FarmMancy_Projectile")) {
                        strider.remove();
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                        //cowMancer.createStrider(1, !strider.isAdult());
                    }
                }
            }
        }
        if (event.getRightClicked() instanceof Pig pig) {
            Player player = event.getPlayer();
            for (FarmMancer cowMancer : TickingCow.getInstance().CowMancers) {
                if (cowMancer._player == player) {
                    if (pig.hasMetadata("FarmMancy_OwnedMob")) {
                        ItemStack heldItem = player.getInventory().getItemInMainHand();

                        if (heldItem.getType().isAir() ||
                                !heldItem.hasItemMeta() ||
                                !heldItem.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE)) {

                            cowMancer.pigAbility(event);
                        }
                    } else {
                        pig.remove();
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                        cowMancer.createPigs(1, !pig.isAdult());
                    }
                }
            }
        }
        if (event.getRightClicked() instanceof Chicken chicken) {
            Player player = event.getPlayer();
            for (FarmMancer cowMancer : TickingCow.getInstance().CowMancers) {
                if (cowMancer._player == player) {
                    if (chicken.hasMetadata("FarmMancy_OwnedMob")) {
                        ItemStack heldItem = player.getInventory().getItemInMainHand();

                        if (heldItem.getType().isAir() ||
                                !heldItem.hasItemMeta() ||
                                !heldItem.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE)) {

                            cowMancer.chickenAbility(event);
                        }
                    } else {
                        chicken.remove();
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                        cowMancer.createChickens(1, !chicken.isAdult());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
            for (FarmMancer cowMancer : TickingCow.getInstance().CowMancers) {
                if (cowMancer._player == player) {
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    if (heldItem.getItemMeta() != null) {
                        if (heldItem.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE)) {
                            cowMancer.cowAbility(event);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCowMove(EntityMoveEvent event) {
        if (event.getEntity() instanceof Cow cow && cow.hasMetadata("FarmMancy_Projectile")) {

            Block currentBlock = cow.getLocation().getBlock();
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;
                        Block block = currentBlock.getRelative(x, y, z);
                        if (block.getType() != Material.AIR && block.getType() != Material.CAVE_AIR) {
                            Location location = cow.getLocation();
                            World world = location.getWorld();
                            world.createExplosion(location, FarmConfig.getInstance().getCowExplosionRadius());
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata("FarmMancy_Projectile")) {
            event.getDrops().clear();
            event.getEntity().removeMetadata("FarmMancy_Projectile", FarmMancy.getInstance());
        }
        if (event.getEntity().hasMetadata("FarmMancy_OwnedMob")) {
            List<MetadataValue> metadata = event.getEntity().getMetadata("FarmMancy_OwnedMob");
            if (!metadata.isEmpty() && event.getDamageSource().getDamageType() != DamageType.GENERIC) {
                MetadataValue value = metadata.getFirst();
                FarmMancer cowMancer = (FarmMancer) value.value();
                if (cowMancer != null && cowMancer._player != null) {
                    cowMancer._player.sendMessage(Component.text("One of your farm animals has passed..."));
                }
            }
            event.getDrops().clear();
        }
    }
}
