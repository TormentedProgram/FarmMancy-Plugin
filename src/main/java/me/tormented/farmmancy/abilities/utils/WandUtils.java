package me.tormented.farmmancy.abilities.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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
        Material hoeMaterial = Material.WOODEN_HOE;
        if (player.getUniqueId() == UUID.fromString("5e3fd60a-0a0c-4a36-8f68-685902285b56")) {
            hoeMaterial = Material.BREEZE_ROD;
        } //totally not me making it a breeze rod for only me because it's cooler... (let me have my fantasies ok?)
        Wand wand = new Wand(new ItemStack(hoeMaterial, 1));
        wand.convert();
        player.give(wand.getItem());
    }
}
