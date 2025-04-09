package me.tormented.farmmancy.abilities.utils;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.Ability;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.utils.UUIDDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Wand {

    public static final NamespacedKey wandAbilityBindingKey = new NamespacedKey(FarmMancy.getInstance(), "wand_ability_binding");
    public static final NamespacedKey wandKey = new NamespacedKey(FarmMancy.getInstance(), "wand");

    private final @NotNull ItemStack item;

    public Wand(@NotNull ItemStack item) {
        this.item = item;
    }

    public @NotNull ItemStack getItem() {
        return item;
    }

    public boolean isWand() {
        return isWand(item);
    }

    public static boolean isWand(@NotNull ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return false;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        return persistentDataContainer.has(wandKey);
    }

    public void convert() {
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
            itemMeta.setEnchantmentGlintOverride(true);

            Component displayName = itemMeta.displayName(); // Adventure API
            String funnyName = (displayName != null) ? displayName.toString() : formattedName;

            itemMeta.customName(Component.text("Magical " + funnyName + " of Destruction", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));

            itemMeta.getPersistentDataContainer().set(wandKey, PersistentDataType.BYTE, (byte) 1);

            item.setItemMeta(itemMeta);
            updateLoreAndName();
        }
    }

    public void updateLoreAndName() {
        ItemMeta itemMeta = item.getItemMeta();
        if (getBoundAbility() instanceof Ability ability) {
            itemMeta.lore(List.of(
                    Component.text("As Legends foretold..", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("A powerful mage wielded this to vanquish the demon lord.", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text(""),
                    Component.text("Bound Ability: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    ability.getName()));
        } else {
            if (itemMeta != null) {
                itemMeta.lore(List.of(
                        Component.text("As Legends foretold..", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("A powerful mage wielded this to vanquish the demon lord.", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text(""),
                        Component.text("Bound Ability: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("RIGHT-CLICK a row to bind an ability", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
            }
        }
        item.setItemMeta(itemMeta);
    }

    public void setBoundAbility(Ability ability) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        if (ability instanceof Hook.WandSelectable wandSelectable) {
            wandSelectable.onSelected(this);
        }

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(wandAbilityBindingKey, new UUIDDataType(), ability.id);

        item.setItemMeta(itemMeta);

        updateLoreAndName();
    }

    public @Nullable Ability getBoundAbility() {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return null;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        UUID uuid = persistentDataContainer.get(wandAbilityBindingKey, new UUIDDataType());

        if (uuid == null) return null;

        return Ability.getAbilityInstance(uuid);
    }


    public boolean clearBoundAbility() {

        if (getBoundAbility() instanceof Hook.WandSelectable wandSelectable) {
            wandSelectable.onDeselected(this);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return false;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (persistentDataContainer.has(wandAbilityBindingKey)) {
            persistentDataContainer.remove(wandAbilityBindingKey);

            item.setItemMeta(itemMeta);

            updateLoreAndName();

            return true;
        }

        return false;
    }
}
