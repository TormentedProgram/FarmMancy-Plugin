package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.abilities.utils.headProviders.CustomHeadProvider;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IronGolemAbility extends MobunitionAbility<IronGolem> implements Hook.WandSelectable {
    @Override
    public Class<IronGolem> getEntityClass() {
        return IronGolem.class;
    }

    public IronGolemAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI3MTkxM2EzZmM4ZjU2YmRmNmI5MGE0YjRlZDZhMDVjNTYyY2UwMDc2YjUzNDRkNDQ0ZmIyYjA0MGFlNTdkIn19fQ==");

    @Override
    public @NotNull ItemStack getHeadItem(IronGolem entity) {
        return CUSTOM_HEAD_PROVIDER.getHeadItem();
    }

    @Override
    public void onSelected(@NotNull Wand wand) {
    }

    @Override
    public void onDeselected(@NotNull Wand wand) {
    }

    @Override
    public void onWandUse(@NotNull Wand wand, @NotNull PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && getOwnerPlayer() instanceof Player player && !headDisplays.isEmpty()) {
            if (event.getClickedBlock() != null) {
                if (pullMob() instanceof AbilityHeadDisplay headDisplay) {
                    headDisplay.remove();
                    createShockwave(player, event.getClickedBlock());
                }
            }
        }
    }

    public void createShockwave(Player player, Block clickedBlock) {
        Location location = clickedBlock.getLocation().subtract(0.0D, 0.5D, 0.0D);
        double distance = 10.0;
        double damage = 7.0;

        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();
        player.getWorld().playSound(clickedBlock.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 1.0f);

        new BukkitRunnable() {
            double traveled = 0;

            @Override
            public void run() {
                if (traveled >= distance) {
                    cancel();
                    return;
                }

                Location particleLocation = location.clone().add(direction.clone().multiply(traveled)).add(0.0, 1.5, 0.0);
                if (traveled % 3 == 0) {
                    player.getWorld().spawnParticle(Particle.BLOCK, particleLocation, 50, 1.0, 0.0, 1.0, 1, clickedBlock.getType().createBlockData());
                    player.getWorld().playSound(particleLocation, clickedBlock.getBlockSoundGroup().getBreakSound(), 1.0f, 0.5f);
                }

                for (Entity entity : player.getWorld().getEntities()) {
                    if (entity instanceof LivingEntity mob) {
                        if (mob.getLocation().distance(particleLocation) < 3.0 && !mob.equals(player)) {
                            mob.damage(damage);
                        }
                    }
                }

                traveled += 1;
            }
        }.runTaskTimer(FarmMancy.getInstance(), 0, 1);
    }
}
