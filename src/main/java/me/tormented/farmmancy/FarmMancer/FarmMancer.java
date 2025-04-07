package me.tormented.farmmancy.FarmMancer;

import me.tormented.farmmancy.abilities.*;
import me.tormented.farmmancy.abilities.implementations.BeeAbility;
import me.tormented.farmmancy.abilities.implementations.ChickenAbility;
import me.tormented.farmmancy.abilities.implementations.CowAbility;
import me.tormented.farmmancy.abilities.implementations.PigAbility;
import me.tormented.farmmancy.abilities.utils.WandUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FarmMancer {
    public Player _player;

    private final Ability[] equippedAbilities = new Ability[3];
    private Ability specialEquippedAbility;
    private final Set<Ability> unlockedAbilities = new HashSet<>();

    public void setEquippedAbility(int index, Ability ability) {
        this.equippedAbilities[index] = ability;
        ability.slot = index;
    }

    public void setSpecialEquippedAbility(Ability ability) {
        this.specialEquippedAbility = ability;
    }

    public Ability[] getEquippedAbilities() {
        Ability[] newEquippedAbilities = new Ability[equippedAbilities.length + 1];
        System.arraycopy(equippedAbilities, 0, newEquippedAbilities, 0, equippedAbilities.length);
        newEquippedAbilities[equippedAbilities.length] = specialEquippedAbility;
        return newEquippedAbilities;
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

            itemMeta.getPersistentDataContainer().set(WandUtils.magic_hoe_key, PersistentDataType.BYTE, (byte) 1);

            item.setItemMeta(itemMeta);
        }
        return item;
    }

    public FarmMancer(Player player) {
        _player = player;
    }

    public void deactivateAll(boolean kill) {
        for (Ability ability : getEquippedAbilities())
            if (ability instanceof Hook.Activation activation) activation.onDeactivate(kill);
    }


    public void activateAll(int amount, boolean isBaby) {
        if (getEquippedAbilities().length == 0) {
            _player.sendMessage(Component.text("You have no abilities equipped. (EQUIPPING DEFAULTS)", NamedTextColor.RED));
            setEquippedAbility(0, new ChickenAbility(UUID.randomUUID(), _player.getUniqueId()));
            setEquippedAbility(1, new CowAbility(UUID.randomUUID(), _player.getUniqueId()));
            setEquippedAbility(2, new PigAbility(UUID.randomUUID(), _player.getUniqueId()));
            setSpecialEquippedAbility(new BeeAbility(UUID.randomUUID(), _player.getUniqueId()));
        }
        for (Ability ability : getEquippedAbilities()) {
            if (ability instanceof MobAbility<?> mobAbility) {
                mobAbility.isBaby = isBaby;
                if (mobAbility instanceof MobunitionAbility<?> mobunitionAbility) {
                    for (int i = 0; i < amount; i++) {
                        mobunitionAbility.addMob(null);
                    }
                } else if (mobAbility instanceof MobuvertAbility<?> mobuvertAbility) {
                    mobuvertAbility.setMob(null);
                }
            }
            if (ability instanceof Hook.Activation activation) activation.onActivate(true);
        }
    }

    public void tickEquippedAbilities() {
        for (Ability ability : getEquippedAbilities()) {
            if (ability instanceof Hook.Ticking ticking) ticking.onTick(Hook.CallerSource.PLAYER);
        }
    }

}
