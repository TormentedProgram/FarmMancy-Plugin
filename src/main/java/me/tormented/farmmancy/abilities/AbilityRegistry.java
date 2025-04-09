package me.tormented.farmmancy.abilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AbilityRegistry {

    private final Map<String, AbilityFactory> abilitySuppliers = new HashMap<>();

    public void register(String key, AbilityFactory abilityFactory) {
        abilitySuppliers.put(key, abilityFactory);
    }

    public @Nullable AbilityFactory getFactory(@NotNull String key) {
        return abilitySuppliers.get(key);
    }

    public @NotNull Set<String> getAllKeys() {
        return abilitySuppliers.keySet();
    }


}
