package me.tormented.farmmancy.abilities;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.abilities.utils.Wand;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;

public sealed interface Hook {

    enum CallerSource {
        TRACKED_ENTITY,
        PLAYER
    }

    non-sealed interface EntityDamaged extends Hook {
        void processEntityDamage(EntityDamageEvent event);
    }

    non-sealed interface EntityDamagedByEntity extends Hook {
        void processEntityDamagedByEntity(EntityDamageByEntityEvent event);
    }

    non-sealed interface playerMove extends Hook {
        void processPlayerMove(PlayerMoveEvent event);
    }

    non-sealed interface EntityInteractedByPlayer extends Hook {
        void processPlayerInteractEntity(PlayerInteractEntityEvent event, CallerSource callerSource);
    }

    non-sealed interface PlayerInteraction extends Hook {
        void processPlayerInteract(PlayerInteractEvent event);
    }

    non-sealed interface PlayerSneak extends Hook {
        void processSneakToggle(PlayerToggleSneakEvent event);
    }

    non-sealed interface PlayerSwapItem extends Hook {
        void processSwapItem(PlayerItemHeldEvent event);
    }

    non-sealed interface PlayerJoining extends Hook {
        void processPlayerJoin(PlayerJoinEvent event);

        void processPlayerQuit(PlayerQuitEvent event);
    }

    non-sealed interface EntityMoved extends Hook {
        void processEntityMove(EntityMoveEvent event);
    }

    non-sealed interface EntityDeath extends Hook {
        void processEntityDeath(EntityDeathEvent event);
    }

    non-sealed interface Ticking extends Hook {
        void onTick(CallerSource callerSource);
    }

    non-sealed interface Activation extends Hook {
        void onActivate(boolean visual);

        void onDeactivate(boolean visual);

        boolean isActive();
    }

    non-sealed interface PlayerDroppingItem extends Hook {
        void processPlayerDropItem(PlayerDropItemEvent event);
    }


    /**
     * Hooks into when the ability is being assigned in the ability menu.
     */
    non-sealed interface MenuAssignment extends Hook {

        /**
         * Called when the ability is assigned from the ability menu.
         */
        void onAssign();

        /**
         * Called when the ability is unassigned from the ability menu.
         */
        void onUnassign();
    }

    /**
     * Hooks into when the ability is being registered into the ability registry.
     */
    non-sealed interface MemoryRegister extends Hook {
        /**
         * Called when the ability is being added to the registry. This will be called regardless if it's equipped or not.
         */
        void onRegister();

        /**
         * Called when the ability is being marked for unloading (No longer present in the registry). This will be called regardless if it's equipped or not.
         */
        void onDeregister();
    }

    non-sealed interface WandSelectable extends Hook {

        void onSelected(Wand wand);
        void onDeselected(Wand wand);

        void onWandUse(Wand wand, PlayerInteractEvent event);

    }


}
