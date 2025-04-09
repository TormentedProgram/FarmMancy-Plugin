package me.tormented.farmmancy.abilities;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.abilities.utils.Wand;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

public sealed interface Hook {

    enum CallerSource {
        TRACKED_ENTITY,
        PLAYER
    }

    non-sealed interface EntityDamaged extends Hook {
        void processEntityDamage(@NotNull EntityDamageEvent event);
    }

    non-sealed interface EntityDamagedByEntity extends Hook {
        void processEntityDamagedByEntity(@NotNull EntityDamageByEntityEvent event);
    }

    non-sealed interface playerMove extends Hook {
        void processPlayerMove(@NotNull PlayerMoveEvent event);
    }

    non-sealed interface EntityInteractedByPlayer extends Hook {
        void processPlayerInteractEntity(@NotNull PlayerInteractEntityEvent event, CallerSource callerSource);
    }

    non-sealed interface PlayerInteraction extends Hook {
        void processPlayerInteract(@NotNull PlayerInteractEvent event);
    }

    non-sealed interface PlayerSneak extends Hook {
        void processSneakToggle(@NotNull PlayerToggleSneakEvent event);
    }

    non-sealed interface PlayerSwapItem extends Hook {
        void processSwapItem(@NotNull PlayerItemHeldEvent event);
    }

    non-sealed interface PlayerJoining extends Hook {
        void processPlayerJoin(@NotNull PlayerJoinEvent event);

        void processPlayerQuit(@NotNull PlayerQuitEvent event);
    }

    non-sealed interface EntityMoved extends Hook {
        void processEntityMove(@NotNull EntityMoveEvent event);
    }

    non-sealed interface EntityDeath extends Hook {
        void processEntityDeath(@NotNull EntityDeathEvent event);
    }

    non-sealed interface Ticking extends Hook {
        void onTick(@NotNull CallerSource callerSource);
    }

    non-sealed interface Activation extends Hook {
        void onActivate(boolean visual);

        void onDeactivate(boolean visual);

        boolean isActive();
    }

    non-sealed interface PlayerDroppingItem extends Hook {
        void processPlayerDropItem(@NotNull PlayerDropItemEvent event);
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

        void onSelected(@NotNull Wand wand);
        void onDeselected(@NotNull Wand wand);

        void onWandUse(@NotNull Wand wand, @NotNull PlayerInteractEvent event);

    }


}
