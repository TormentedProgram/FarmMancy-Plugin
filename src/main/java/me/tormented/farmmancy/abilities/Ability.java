package me.tormented.farmmancy.abilities;

import me.tormented.farmmancy.FarmMancy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public abstract class Ability {

    private static final Map<UUID, Ability> abilities = new HashMap<>();

    public static @Nullable Ability getAbilityInstance(@NotNull UUID id) {
        return abilities.get(id);
    }

    public static @NotNull Iterator<Ability> getAllAbilityInstances() {
        return abilities.values().iterator();
    }

    public static void unloadAbility(@NotNull UUID uuid) {
        if (getAbilityInstance(uuid) instanceof Hook.MemoryRegister memoryLoad) memoryLoad.onDeregister();
        abilities.remove(uuid);
    }

    public final @NotNull UUID id;
    public final @NotNull UUID owner;

    public int slot;

    protected Ability(@NotNull UUID id, @NotNull UUID owner) {
        this.owner = owner;
        this.id = id;

        abilities.put(id, this);
        if (this instanceof Hook.MemoryRegister memoryLoad) memoryLoad.onRegister();
    }

    private @Nullable Player cachedOwnerPlayer = null;

    public @Nullable Player getOwnerPlayer(boolean updateCache) {
        if (cachedOwnerPlayer == null || updateCache)
            cachedOwnerPlayer = FarmMancy.getInstance().getServer().getPlayer(owner);
        return cachedOwnerPlayer;
    }

    public @Nullable Player getOwnerPlayer() {
        return getOwnerPlayer(false);
    }

}
