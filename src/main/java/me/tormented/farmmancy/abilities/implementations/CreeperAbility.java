package me.tormented.farmmancy.abilities.implementations;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.FarmConfig;
import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.abilities.utils.headProviders.BlockProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CreeperAbility extends MobunitionAbility<Creeper> implements Hook.EntityMoved, Hook.WandSelectable {
    private final float CreeperExplosionRadius = FarmConfig.getInstance().getCreeperExplosionRadius();
    private final float CreeperShootVelocity = FarmConfig.getInstance().getCreeperShootVelocity();

    @Override
    public Class<Creeper> getEntityClass() {
        return Creeper.class;
    }

    public CreeperAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final BlockProvider BLOCK_PROVIDER = new BlockProvider(Material.CREEPER_HEAD);

    @Override
    public @NotNull ItemStack getHeadItem(Creeper entity) {
        return BLOCK_PROVIDER.getHeadItem();
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
                        explodeEntity(event.getEntity());
                        return;
                    }
                }
            }
        }
    }

    private void explodeEntity(@NotNull LivingEntity entity) {
        Location location = entity.getLocation();
        World world = location.getWorld();
        world.createExplosion(location, CreeperExplosionRadius);
        entity.setHealth(0);
        EventDistributor.getInstance().entityMobunitionAbilityMap.remove(entity);
    }

    @Override
    public void onSelected(@NotNull Wand wand) {
    }

    @Override
    public void onDeselected(@NotNull Wand wand) {
    }

    @Override
    public void onWandUse(@NotNull Wand wand, @NotNull PlayerInteractEvent event) {
        if (event.getAction().isLeftClick() && getOwnerPlayer() instanceof Player player && !headDisplays.isEmpty()) {
            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(slotRadii[slot]));

            Creeper flingCreeper = pullAndSummonMob(targetLoc);

            if (flingCreeper != null) {
                flingCreeper.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
                EventDistributor.getInstance().entityMobunitionAbilityMap.put(flingCreeper, this);

                Vector velocity = direction.multiply(CreeperShootVelocity);

                flingCreeper.setVelocity(velocity);
            }
        }
    }
}
