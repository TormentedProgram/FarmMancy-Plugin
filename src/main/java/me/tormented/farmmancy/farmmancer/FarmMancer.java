package me.tormented.farmmancy.farmmancer;

import me.tormented.farmmancy.abilities.*;
import me.tormented.farmmancy.abilities.implementations.BeeAbility;
import me.tormented.farmmancy.abilities.implementations.ChickenAbility;
import me.tormented.farmmancy.abilities.implementations.CowAbility;
import me.tormented.farmmancy.abilities.implementations.PigAbility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
