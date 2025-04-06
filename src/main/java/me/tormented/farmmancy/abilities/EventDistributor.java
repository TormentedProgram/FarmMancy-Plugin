package me.tormented.farmmancy.abilities;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.FarmMancer.FarmMancer;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    public void onEntityDeath(EntityDeathEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDeath entityDeath) entityDeath.processEntityDeath(event);
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
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerJoining playerJoining) playerJoining.processPlayerQuit(event);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractWithEntity(PlayerInteractEntityEvent event) {

        if (entityMobunitionAbilityMap.get(event.getRightClicked()) instanceof MobAbility<? extends Entity> mobAbility && mobAbility instanceof Hook.EntityInteractedByPlayer entityInteractedByPlayer ) {
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


}
