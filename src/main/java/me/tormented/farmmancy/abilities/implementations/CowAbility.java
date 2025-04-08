package me.tormented.farmmancy.abilities.implementations;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.FarmConfig;
import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.utils.HeadProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Cow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CowAbility extends MobunitionAbility<Cow> implements Hook.EntityMoved, Hook.WandSelectable {
    private final float CowExplosionRadius = FarmConfig.getInstance().getCowExplosionRadius();
    private final float CowShootVelocity = FarmConfig.getInstance().getCowShootVelocity();

    @Override
    public Class<Cow> getEntityClass() {
        return Cow.class;
    }

    public CowAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/63d621100fea5883922e78bb448056448c983e3f97841948a2da747d6b08b8ab");

    @Override
    public @NotNull ItemStack getHeadItem(Cow entity) {
        return headProvider.getHeadItem();
    }

    @Override
    public void processEntityMove(EntityMoveEvent event) {
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

    private void explodeEntity(LivingEntity entity) {
        Location location = entity.getLocation();
        World world = location.getWorld();
        world.createExplosion(location, CowExplosionRadius);
        entity.setHealth(0);
        EventDistributor.getInstance().entityMobunitionAbilityMap.remove(entity);
    }

    @Override
    public void onSelected(Wand wand) {
    }

    @Override
    public void onDeselected(Wand wand) {
    }

    @Override
    public void onWandUse(Wand wand, PlayerInteractEvent event) {
        if (event.getAction().isLeftClick() && getOwnerPlayer() instanceof Player player && !headDisplays.isEmpty()) {
            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(slotRadii[slot]));

            Cow flingingCow = pullAndSummonMob(targetLoc);

            if (flingingCow != null) {

                flingingCow.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
                EventDistributor.getInstance().entityMobunitionAbilityMap.put(flingingCow, this);

                Vector velocity = direction.multiply(CowShootVelocity);

                flingingCow.setVelocity(velocity);
            } else {
                // Not enough ammo
            }
        }


    }
}
