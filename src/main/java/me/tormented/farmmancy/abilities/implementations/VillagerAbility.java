package me.tormented.farmmancy.abilities.implementations;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.abilities.utils.headProviders.CustomHeadProvider;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VillagerAbility extends MobunitionAbility<Villager> implements Hook.EntityMoved, Hook.WandSelectable {
    @Override
    public Class<Villager> getEntityClass() {
        return Villager.class;
    }

    public VillagerAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ViZDdiYWRkMzFkOTJhMmJkZTgxOWQ1MmYzY2YwNDM5ZjYzYWIxMThkNjk2MDc3MjI1MTUxMTk3YmI1ZWIxNyJ9fX0=");

    @Override
    public @NotNull ItemStack getHeadItem(Villager entity) {
        return CUSTOM_HEAD_PROVIDER.getHeadItem();
    }

    @Override
    public void processEntityMove(@NotNull EntityMoveEvent event) {
        if (!event.getEntity().hasMetadata("FarmMancy_Projectile")) return;
        if (event.getEntity().isDead()) return;
        Block currentBlock = event.getEntity().getLocation().getBlock();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    Block block = currentBlock.getRelative(x, y, z);
                    if (block.isSolid() || block.isLiquid()) {
                        Villager child = block.getWorld().spawn(block.getLocation().add(0f, 2f, 0f), Villager.class);
                        child.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
                        child.setBaby();
                        child.setProfession(Villager.Profession.NITWIT);
                        child.setAgeLock(true);
                        event.getEntity().setHealth(0f);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onSelected(@NotNull Wand wand) {
        if (getOwnerPlayer() instanceof Player player) {
            player.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);
        }
    }

    @Override
    public void onDeselected(@NotNull Wand wand) {
        if (getOwnerPlayer() instanceof Player player) {
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
        }
    }

    @Override
    public void onWandUse(@NotNull Wand wand, @NotNull PlayerInteractEvent event) {
        if (event.getAction().isLeftClick() && getOwnerPlayer() instanceof Player player && !headDisplays.isEmpty()) {
            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(slotRadii[slot]));

            Villager flingCreeper = pullAndSummonMob(targetLoc);

            player.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.5f);

            if (flingCreeper != null) {
                flingCreeper.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
                EventDistributor.getInstance().entityMobunitionAbilityMap.put(flingCreeper, this);

                Vector velocity = direction.multiply(1f);

                flingCreeper.setVelocity(velocity);
            }
        }
    }
}
