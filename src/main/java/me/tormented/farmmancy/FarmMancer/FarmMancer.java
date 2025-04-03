package me.tormented.farmmancy.FarmMancer;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.Ability;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobAbility;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FarmMancer {
    public static NamespacedKey magic_hoe_key = new NamespacedKey(FarmMancy.getInstance(), "magical_hoe");

    public Player _player;

    private final Ability[] equippedAbilities = new Ability[3];
    private Ability specialEquippedAbility;
    private final Set<Ability> unlockedAbilities = new HashSet<>();

    public void setEquippedAbility(int index, Ability ability) {
        this.equippedAbilities[index] = ability;
    }

    public void setSpecialEquippedAbility(Ability ability) {
        this.specialEquippedAbility = ability;
    }

    public static ItemStack Cowification(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        String itemName = item.getType().toString();
        String[] words = itemName.split("_");
        StringBuilder formattedNameBuilder = new StringBuilder();

        for (String word : words) {
            formattedNameBuilder.append(word.substring(0, 1).toUpperCase())  // Capitalize first letter
                    .append(word.substring(1).toLowerCase())  // Lowercase the rest
                    .append(" ");
        }

        String formattedName = formattedNameBuilder.toString().trim();

        if (itemMeta != null) {
            itemMeta.lore(List.of(
                    Component.text("As Legends foretold..", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("A powerful mage wielded this to vanquish the demon lord.", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));

            itemMeta.setEnchantmentGlintOverride(true);

            Component displayName = itemMeta.displayName(); // Adventure API
            String funnyName = (displayName != null) ? displayName.toString() : formattedName;

            itemMeta.customName(Component.text("Magical " + funnyName + " of Destruction", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));

            itemMeta.getPersistentDataContainer().set(FarmMancer.magic_hoe_key, PersistentDataType.BYTE, (byte) 1);

            item.setItemMeta(itemMeta);
        }
        return item;
    }

    public FarmMancer(Player player) {
        _player = player;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getPersistentDataContainer().has(magic_hoe_key, PersistentDataType.BYTE)) {
                    return;
                }
            }
        }

        ItemStack MagicHoe = Cowification(new ItemStack(Material.NETHERITE_HOE, 1));

        player.give(MagicHoe);
    }

    public void deactivateAll(boolean kill) {
        for (Ability ability : equippedAbilities)
            if (ability instanceof Hook.Activation activation) activation.onDeactivate(kill);
    }


    public void activateAll(int amount, boolean isBaby) {
        for (Ability ability : equippedAbilities) {
            if (ability instanceof MobAbility<?> mobAbility) {
                mobAbility.isBaby = isBaby;
                if (mobAbility instanceof MobunitionAbility<?> mobunitionAbility) {
                    mobunitionAbility.amount = amount;
                }
            }
            if (ability instanceof Hook.Activation activation) activation.onActivate(true);
        }
    }

    public void tickEquippedAbilities() {
        for (Ability ability : this.equippedAbilities) {
            if (ability instanceof Hook.Ticking ticking) ticking.onTick(Hook.CallerSource.PLAYER);
        }
        if (specialEquippedAbility instanceof Hook.Ticking ticking) ticking.onTick(Hook.CallerSource.PLAYER);
    }

}
