package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.utils.HeadProvider;
import org.bukkit.entity.Chicken;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChickenAbility extends MobunitionAbility<Chicken> implements Hook.EntityInteractedByPlayer {

    @Override
    public Class<Chicken> getEntityClass() {
        return Chicken.class;
    }

    public ChickenAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
        modRingRadius = 3f;
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/42af6e5847eea099e1b0ab8c20a9e5f3c7190158bda54e28133d9b271ec0cb4b");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Chicken entity) {
        return headProvider.getHeadItem();
    }

    @Override
    public void processPlayerInteractEntity(PlayerInteractEntityEvent event, CallerSource callerSource) {
        super.processPlayerInteractEntity(event, callerSource);

        switch (callerSource) {
            case PLAYER -> {}
            case TRACKED_ENTITY -> {}
        }

        /* No longer implemented as the ring no longer uses tracked entities
        Player player = event.getPlayer();

        if (player != getOwnerPlayer()) return;

        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (heldItem.getType().isAir() ||
                !heldItem.hasItemMeta() ||
                !heldItem.getItemMeta().getPersistentDataContainer().has(FarmMancer.magic_hoe_key, PersistentDataType.BYTE)) {

            Chicken chicken = (Chicken) event.getRightClicked();
            headDisplays.remove(chicken);

            Location loc = player.getLocation();
            Vector direction = loc.getDirection();
            Location targetLoc = loc.add(direction.multiply(1));

            chicken.teleport(targetLoc);
            chicken.setHealth(0);

            Vector velocityVector = player.getVelocity();
            velocityVector.setY(1.1f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 80, 1));
            player.setVelocity(velocityVector);
        }
        */
    }
}
