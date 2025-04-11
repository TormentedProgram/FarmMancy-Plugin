package me.tormented.farmmancy.abilities;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AbilityFactory {

    private Component name;
    private ItemStack icon;
    private final TriFunction<AbilityFactory, UUID, UUID, Ability> abilityConstructor;

    public AbilityFactory(@NotNull TriFunction<AbilityFactory, UUID, UUID, Ability> abilityConstructor) {
        this.abilityConstructor = abilityConstructor;
    }

    public AbilityFactory setName(@NotNull Component name) {
        this.name = name;
        return this;
    }

    public Component getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public AbilityFactory setIcon(@NotNull ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public @NotNull Ability createAbility(@NotNull UUID id, @NotNull UUID owner) {
        return abilityConstructor.apply(this, id, owner);
    }

}
