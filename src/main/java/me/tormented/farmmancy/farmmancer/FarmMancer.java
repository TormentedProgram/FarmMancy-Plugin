package me.tormented.farmmancy.farmmancer;

import me.tormented.farmmancy.Registries;
import me.tormented.farmmancy.abilities.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FarmMancer {
    private Player farmMancerPlayer;

    private final Ability[] equippedAbilities = new Ability[3];
    private Ability specialEquippedAbility;
    private final Set<Ability> unlockedAbilities = new HashSet<>();

    public Player getPlayer() {
        return farmMancerPlayer;
    }

    public void setEquippedAbility(int index, @Nullable Ability ability) {
        if (index < 0 || index > equippedAbilities.length)
            throw new IllegalArgumentException("Slot index is out of bounds");

        Ability oldAbility = index == equippedAbilities.length ? specialEquippedAbility : equippedAbilities[index];

        if (oldAbility instanceof Hook.Equipping oldEquippingAbility) {
            oldEquippingAbility.onUnequipped();
        }

        if (index == equippedAbilities.length)
            specialEquippedAbility = ability;
        else
            equippedAbilities[index] = ability;

        if (ability != null) {
            ability.slot = index;

            if (ability instanceof Hook.Equipping equippingAbility) {
                equippingAbility.onEquipped();
            }
        }
    }

    @Deprecated
    public void setSpecialEquippedAbility(Ability ability) {
        this.specialEquippedAbility = ability;
    }

    public Ability[] getEquippedAbilities() {
        Ability[] newEquippedAbilities = new Ability[equippedAbilities.length + 1];
        System.arraycopy(equippedAbilities, 0, newEquippedAbilities, 0, equippedAbilities.length);
        newEquippedAbilities[equippedAbilities.length] = specialEquippedAbility;
        return newEquippedAbilities;
    }

    public FarmMancer(Player player) {
        farmMancerPlayer = player;
    }

    public void deactivateAll(boolean kill) {
        for (Ability ability : getEquippedAbilities())
            if (ability instanceof Hook.Activation activation) activation.onDeactivate(kill);
    }


    public void activateAll(int amount, boolean isBaby) {
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

    public boolean isAbilityUnlocked(@NotNull AbilityFactory abilityFactory) {
        for (Ability unlockedAbility : unlockedAbilities) {
            if (unlockedAbility.getAbilityFactory().equals(abilityFactory)) return true;
        }

        return false;
    }

    public Ability unlockAbility(@NotNull AbilityFactory abilityFactory) {
        Ability unlockedAbility = abilityFactory.createAbility(UUID.randomUUID(), farmMancerPlayer.getUniqueId());
        unlockedAbilities.add(unlockedAbility);

        farmMancerPlayer.playSound(farmMancerPlayer, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        farmMancerPlayer.sendMessage(Component.text("Ability unlocked: ", NamedTextColor.GREEN)
                .append(unlockedAbility.getName())
        );

        return unlockedAbility;
    }

    public Iterator<Ability> getUnlockedAbilities() {
        return unlockedAbilities.iterator();
    }

    public Map<String, Ability> getUnlockedAbilitiesOfDoom() {
        Map<String, Ability> abilityMap = new HashMap<>();
        Map<String, AbilityFactory> AbilityNames = Registries.abilityRegistry.getRegistry();

        for (Ability ability : unlockedAbilities) {
            String abilityName = null;
            for (Map.Entry<String, AbilityFactory> entry : AbilityNames.entrySet()) {
                if (entry.getValue().equals(ability.getAbilityFactory())) {
                    abilityName = entry.getKey().split(":", 2)[1].trim();
                    break;
                }
            }
            if (abilityName != null) {
                abilityMap.put(abilityName, ability);
            }
        }

        return abilityMap;
    }
}
