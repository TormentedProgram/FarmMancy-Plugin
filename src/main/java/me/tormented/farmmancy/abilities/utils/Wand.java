package me.tormented.farmmancy.abilities.utils;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.Ability;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.utils.StringUtils;
import me.tormented.farmmancy.utils.UUIDDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
        String formattedName = StringUtils.makeIDHumanReadable(itemName);

        if (itemMeta != null) {
            itemMeta.setEnchantmentGlintOverride(true);

            Component displayName = itemMeta.displayName(); // Adventure API
            String funnyName = (displayName != null) ? displayName.toString() : formattedName;

            Component customName = MiniMessage.miniMessage().deserialize("<!italic><light_purple>Magical</light_purple> <name> <light_purple>of Farm</light_purple> <dark_purple>Manipulation</dark_purple></!italic>", Placeholder.parsed("name", "<aqua>" + funnyName + "</aqua>"));

            itemMeta.customName(customName);

            itemMeta.getPersistentDataContainer().set(wandKey, PersistentDataType.BYTE, (byte) 1);

            item.setItemMeta(itemMeta);
            updateLoreAndName();
        }
    }

    public void updateLoreAndName() {
        ItemMeta itemMeta = item.getItemMeta();
        if (getBoundAbility() instanceof Ability ability) {
            String owningPlayerName = (ability.getOwnerPlayer() != null) ? ability.getOwnerPlayer().getName() : "Unknown";
            itemMeta.lore(List.of(
                    Component.text("As Legends foretold..", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("A powerful mage wielded this to vanquish the demon lord.", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text(""),
                    Component.text("Bound Ability: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    ability.getName(),
                    Component.text("(Drop Item to unbind current ability)", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text(""),
                    Component.text("Ability soul-bounded to", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                            .append(Component.text(" " + owningPlayerName, NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
            ));
        } else {
            if (itemMeta != null) {
                itemMeta.lore(List.of(
                        Component.text("As Legends foretold..", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("A powerful mage wielded this to vanquish the demon lord.", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text(""),
                        Component.text("Bound Ability: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("RIGHT-CLICK a row to bind an ability", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)
                ));
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
