package me.tormented.farmmancy.abilities.utils;

import me.tormented.farmmancy.FarmMancer.FarmMancer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class WandUtils {
    public static boolean isHoldingCowWand(@NotNull Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE);
    }

    public static boolean isHoldingCowWand(@NotNull Player player, int slotIndex) {
        ItemStack item = player.getInventory().getItem(slotIndex);
        if (item == null || item.getItemMeta() == null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE);
    }
}
