package me.tormented.farmmancy.abilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;

public class AbilityRegistry {

    private final Map<String, BiFunction<UUID, UUID, Ability>> abilitySuppliers = new HashMap<>();

    public void register(String key, BiFunction<UUID, UUID, Ability> biFunction) {
        abilitySuppliers.put(key, biFunction);
    }

    public @Nullable BiFunction<UUID, UUID, Ability> getFunction(@NotNull String key) {
        return abilitySuppliers.get(key);
    }

    public @Nullable Ability getAbility(@NotNull String key, UUID id, UUID owner) {
        if (getFunction(key) instanceof BiFunction<UUID, UUID, Ability> biFunction) {
            return biFunction.apply(id, owner);
        }
        return null;
    }

    public @NotNull Set<String> getAllKeys() {
        return abilitySuppliers.keySet();
    }


}
