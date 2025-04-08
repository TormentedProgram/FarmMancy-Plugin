package me.tormented.farmmancy.abilities;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.FarmMancer.FarmMancer;
import me.tormented.farmmancy.abilities.utils.Wand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventDistributor implements Listener {

    private static final EventDistributor instance = new EventDistributor();

    public static EventDistributor getInstance() {
        return instance;
    }

    private EventDistributor() {
    }

    public Map<Entity, MobAbility<? extends Entity>> entityMobunitionAbilityMap = new HashMap<>();
    public Map<UUID, FarmMancer> playerAbilityMap = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDamaged entityDamaged) entityDamaged.processEntityDamage(event);
        }
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.playerMove playerSneak) playerSneak.processPlayerMove(event);
            }
        }
    }

    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDamagedByEntity entityDamaged) entityDamaged.processEntityDamagedByEntity(event);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDeath entityDeath) entityDeath.processEntityDeath(event);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerSneak playerSneak) playerSneak.processSneakToggle(event);
            }
        }
    }

    @EventHandler
    public void onSwapItem(PlayerItemHeldEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerSwapItem playerSwap) playerSwap.processSwapItem(event);
            }
        }
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityMoved entityMoved) entityMoved.processEntityMove(event);
        }
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerInteraction playerInteraction)
                    playerInteraction.processPlayerInteract(event);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerJoining playerJoining) playerJoining.processPlayerJoin(event);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer != null) {
            for (Ability ability : farmMancer.getEquippedAbilities()) {
                if (ability != null) {
                    if (ability instanceof Hook.PlayerJoining playerJoining) playerJoining.processPlayerQuit(event);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractWithEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) return;

        if (entityMobunitionAbilityMap.get(event.getRightClicked()) instanceof MobAbility<? extends Entity> mobAbility && mobAbility instanceof Hook.EntityInteractedByPlayer entityInteractedByPlayer) {
            entityInteractedByPlayer.processPlayerInteractEntity(event, Hook.CallerSource.TRACKED_ENTITY);
        }

        if (playerAbilityMap.get(event.getPlayer().getUniqueId()) instanceof FarmMancer farmMancer) {
            for (Ability ability : farmMancer.getEquippedAbilities()) {
                if (ability instanceof Hook.EntityInteractedByPlayer entityInteractedByPlayer) {
                    entityInteractedByPlayer.processPlayerInteractEntity(event, Hook.CallerSource.PLAYER);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (playerAbilityMap.get(event.getPlayer().getUniqueId()) instanceof FarmMancer farmMancer) {
            for (Ability ability : farmMancer.getEquippedAbilities()) {
                if (ability instanceof Hook.PlayerDroppingItem playerDroppingItem) {
                    playerDroppingItem.processPlayerDropItem(event);
                }
            }
        }

        Wand checkingWand = new Wand(event.getItemDrop().getItemStack());
        if (checkingWand.isWand() && checkingWand.clearBoundAbility()) {
            event.setCancelled(true);
        }
    }


}
