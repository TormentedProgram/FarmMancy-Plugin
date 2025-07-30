package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobunitionAbility;
import me.tormented.farmmancy.abilities.utils.headProviders.CustomHeadProvider;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Cow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CowAbility extends MobunitionAbility<Cow> implements Hook.PlayerInteraction {
    @Override
    public Class<Cow> getEntityClass() {
        return Cow.class;
    }

    public CowAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final CustomHeadProvider CUSTOM_HEAD_PROVIDER = new CustomHeadProvider("http://textures.minecraft.net/texture/63d621100fea5883922e78bb448056448c983e3f97841948a2da747d6b08b8ab");

    @Override
    public @NotNull ItemStack getHeadItem(Cow entity) {
        return CUSTOM_HEAD_PROVIDER.getHeadItem();
    }

    @Override
    public void processPlayerInteract(@NotNull PlayerInteractEvent event) {
        super.processPlayerInteract(event);

        if (event.getAction().isRightClick() && isBeingLookedAt()) {
            Player player = event.getPlayer();

            if (player.getAttribute(Attribute.MAX_HEALTH) instanceof AttributeInstance healthAttribute) {
                if (player.getHealth() < healthAttribute.getValue()) {

                    Location loc = player.getLocation();
                    Vector direction = loc.getDirection();
                    Location targetLoc = loc.add(direction.multiply(1));

                    if (pullAndSummonMob(targetLoc) instanceof LivingEntity livingEntity) {
                        EventDistributor.getInstance().entityMobunitionAbilityMap.put(livingEntity, this);
                        livingEntity.setMetadata("FarmMancy_Projectile", new FixedMetadataValue(FarmMancy.getInstance(), this));
                        livingEntity.setHealth(0f);

                        player.setFoodLevel(player.getFoodLevel() + 4);
                    }
                }
            }
        }
    }
}
