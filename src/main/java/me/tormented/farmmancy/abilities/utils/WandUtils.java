package me.tormented.farmmancy.abilities.utils;

import me.tormented.farmmancy.FarmMancer.FarmMancer;
import me.tormented.farmmancy.FarmMancy;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class WandUtils {
    public static NamespacedKey magic_hoe_key = new NamespacedKey(FarmMancy.getInstance(), "magical_hoe");

    public static boolean isHoldingCowWand(@NotNull Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(magic_hoe_key, PersistentDataType.BYTE);
    }

    public static boolean isHoldingCowWand(@NotNull Player player, int slotIndex) {
        ItemStack item = player.getInventory().getItem(slotIndex);
        if (item == null || item.getItemMeta() == null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(magic_hoe_key, PersistentDataType.BYTE);
    }

    public static void giveCowWand(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getPersistentDataContainer().has(magic_hoe_key, PersistentDataType.BYTE)) {
                    return;
                }
            }
        }

        ItemStack MagicHoe = FarmMancer.Cowification(new ItemStack(Material.NETHERITE_HOE, 1));
        player.give(MagicHoe);
    }
}
