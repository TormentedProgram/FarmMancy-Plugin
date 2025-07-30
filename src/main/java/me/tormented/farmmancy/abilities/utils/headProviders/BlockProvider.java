package me.tormented.farmmancy.abilities.utils.headProviders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record BlockProvider(@NotNull Material block) {
    public @NotNull ItemStack getHeadItem() {
        return new ItemStack(block);
    }
}
