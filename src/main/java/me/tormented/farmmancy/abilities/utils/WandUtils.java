package me.tormented.farmmancy.abilities.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WandUtils {

    public static boolean isHoldingWand(@NotNull Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return false;
        return Wand.isWand(item);
    }

    public static boolean isHoldingWand(@NotNull Player player, int slotIndex) {
        ItemStack item = player.getInventory().getItem(slotIndex);
        if (item == null || item.getItemMeta() == null) return false;
        return Wand.isWand(item);
    }

    public static void giveWandIfMissing(@NotNull Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && Wand.isWand(item)) return;
        }

        giveWand(player);
    }

    public static void giveWand(@NotNull Player player) {
        Wand wand = new Wand(new ItemStack(Material.NETHERITE_HOE, 1));
        wand.convert();
        player.give(wand.getItem());
    }
}
