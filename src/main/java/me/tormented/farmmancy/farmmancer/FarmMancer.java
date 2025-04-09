package me.tormented.farmmancy.farmmancer;

import me.tormented.farmmancy.abilities.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class FarmMancer {
    public final Player _player;

    private final Ability[] equippedAbilities = new Ability[3];
    private Ability specialEquippedAbility;
    private final Set<Ability> unlockedAbilities = new HashSet<>();

    public void setEquippedAbility(int index, @Nullable Ability ability) {
        this.equippedAbilities[index] = ability;
        if (ability != null)
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

    public FarmMancer(Player player) {
        _player = player;
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

}
