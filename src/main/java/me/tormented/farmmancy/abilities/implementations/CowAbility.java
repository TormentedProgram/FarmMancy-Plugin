package me.tormented.farmmancy.abilities.implementations;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.FarmMancer.FarmMancer;
import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.utils.HeadProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Cow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CowAbility extends MobunitionAbility<Cow> implements Hook.PlayerInteraction, Hook.EntityMoved {


    @Override
    public Class<Cow> getEntityClass() {
        return Cow.class;
    }

    public CowAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        modRingRadius = 3f;
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/63d621100fea5883922e78bb448056448c983e3f97841948a2da747d6b08b8ab");

    @Override
    public @NotNull ItemStack getHeadItem(Cow entity) {
        return headProvider.getHeadItem();
    }

    @Override
    public void processPlayerInteract(PlayerInteractEvent event) {
        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (heldItem.getItemMeta() instanceof ItemMeta itemMeta && itemMeta.getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE)) {
            Player player = event.getPlayer();

            if (player != getOwnerPlayer()) return;

            if (headDisplays.isEmpty()) {
                return;
            }

            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(modRingRadius));

            Cow flingingCow = pullAndSummonMob(targetLoc);

            if (flingingCow != null) {

                flingingCow.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
                EventDistributor.getInstance().entityMobunitionAbilityMap.put(flingingCow, this);

                Vector velocity = direction.multiply(1.0);

                flingingCow.setVelocity(velocity);
            } else {
                // Not enough ammo
            }

        }
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
                    if (block.getType() != Material.AIR) {
                        explodeEntity(event.getEntity());
                    }
                }
            }
        }
    }

    private void explodeEntity(LivingEntity entity) {
        Location location = entity.getLocation();
        World world = location.getWorld();
        world.createExplosion(location, 4.0F);
        entity.setHealth(0);
        EventDistributor.getInstance().entityMobunitionAbilityMap.remove(entity);
    }
}
