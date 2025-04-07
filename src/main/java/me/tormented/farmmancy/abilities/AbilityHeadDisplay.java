package me.tormented.farmmancy.abilities;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbilityHeadDisplay {

    private final @NotNull ItemStack itemStack;
    private @Nullable ItemDisplay itemDisplay;
    private Location location;

    public AbilityHeadDisplay(@NotNull ItemStack item) {
        itemStack = item;
    }

    public void spawn(@NotNull Location location) {

        setLocation(location);

        if (itemDisplay == null) {
            itemDisplay = location.getWorld().spawn(this.location, ItemDisplay.class);
            itemDisplay.setItemStack(itemStack);
            itemDisplay.setTeleportDuration(1);
        }
    }

    public void remove() {
        if (itemDisplay != null) {
            itemDisplay.remove();
            itemDisplay = null;
        }
    }

    public boolean isSpawned() {
        return itemDisplay != null;
    }

    public boolean setLocation(@NotNull Location location) {

        this.location = location;

        if (itemDisplay != null) {
            return itemDisplay.teleport(this.location);
        }

        return false;
    }

}
