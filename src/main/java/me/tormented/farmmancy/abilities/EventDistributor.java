package me.tormented.farmmancy.abilities;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

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

    public final Map<Entity, MobAbility<? extends Entity>> entityMobunitionAbilityMap = new HashMap<>();
    public final Map<UUID, FarmMancer> playerAbilityMap = new HashMap<>();

    @EventHandler
    public void onEntityDamage(@NotNull EntityDamageEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDamaged entityDamaged) entityDamaged.processEntityDamage(event);
        }
    }

    @EventHandler
    public void playerMove(@NotNull PlayerMoveEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.playerMove playerSneak) playerSneak.processPlayerMove(event);
            }
        }
    }

    @EventHandler
    public void onEntityDamagedByEntity(@NotNull EntityDamageByEntityEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDamagedByEntity entityDamaged)
                entityDamaged.processEntityDamagedByEntity(event);
        }
    }

    @EventHandler
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDeath entityDeath) entityDeath.processEntityDeath(event);
        }
    }

    @EventHandler
    public void onSneak(@NotNull PlayerToggleSneakEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerSneak playerSneak) playerSneak.processSneakToggle(event);
            }
        }
    }

    @EventHandler
    public void onSwapItem(@NotNull PlayerItemHeldEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerSwapItem playerSwap) playerSwap.processSwapItem(event);
            }
        }
    }

    @EventHandler
    public void onEntityMove(@NotNull EntityMoveEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityMoved entityMoved) entityMoved.processEntityMove(event);
        }
    }

    @EventHandler
    public void onPlayerInteraction(@NotNull PlayerInteractEvent event) {
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
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        FarmMancerManager.getInstance().FarmMancerToUnload.remove(farmMancer);
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerJoining playerJoining) playerJoining.processPlayerJoin(event);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer != null) {
            FarmMancerManager.getInstance().FarmMancerToUnload.add(farmMancer);
            for (Ability ability : farmMancer.getEquippedAbilities()) {
                if (ability != null) {
                    if (ability instanceof Hook.PlayerJoining playerJoining) playerJoining.processPlayerQuit(event);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractWithEntity(@NotNull PlayerInteractEntityEvent event) {
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
    public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
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
