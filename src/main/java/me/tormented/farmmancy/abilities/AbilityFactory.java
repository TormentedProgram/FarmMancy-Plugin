package me.tormented.farmmancy.abilities;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record AbilityFactory(@NotNull TriFunction<AbilityFactory, UUID, UUID, Ability> abilityConstructor) {

    public @NotNull Ability createAbility(@NotNull UUID id, @NotNull UUID owner) {
        return abilityConstructor.apply(this, id, owner);
    }

}
