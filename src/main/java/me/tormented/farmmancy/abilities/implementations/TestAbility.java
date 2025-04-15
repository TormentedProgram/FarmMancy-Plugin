package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.AbilityHeadDisplay;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.ItemunitionAbility;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.abilities.utils.headProviders.BlockProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class TestAbility extends ItemunitionAbility<Entity> implements Hook.WandSelectable {
    public HashMap<Arrow, AbilityHeadDisplay> abilityProjectiles = new HashMap<>();

    @Override
    public Class<Entity> getEntityClass() {
        return null;
    }

    public TestAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final BlockProvider CUSTOM_HEAD_PROVIDER = new BlockProvider(Material.TNT);

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Entity entity) {
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
        if (event.getAction().isLeftClick() && getOwnerPlayer() instanceof Player player && !headDisplays.isEmpty()) {
            if (pullMob() instanceof AbilityHeadDisplay headDisplay) {
                headDisplay.remove();
                Arrow arrow = player.launchProjectile(Arrow.class);
                AbilityHeadDisplay hedDisplay = new AbilityHeadDisplay(getHeadItem(null));
                arrow.setVelocity(player.getLocation().getDirection().multiply(1));
                arrow.setVisibleByDefault(false);
                arrow.setDamage(0f);
                hedDisplay.spawn(arrow.getLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 1.0f);
                arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                arrow.setHitSound(Sound.ENTITY_IRON_GOLEM_ATTACK);
                abilityProjectiles.put(arrow, hedDisplay);
            }
        }
    }

    @Override
    public void onTick(@NotNull CallerSource callerSource) {
        super.onTick(callerSource);

        if (callerSource == CallerSource.PLAYER && getOwnerPlayer() != null) {
            Iterator<Map.Entry<Arrow, AbilityHeadDisplay>> iterator = abilityProjectiles.entrySet().iterator();

            float lifetime = FarmMancy.getInstance().getServer().getCurrentTick() - startTick;
            while (iterator.hasNext()) {
                Map.Entry<Arrow, AbilityHeadDisplay> entry = iterator.next();
                Arrow arrow = entry.getKey();
                AbilityHeadDisplay headDisplay = entry.getValue();

                float rotationAngle = lifetime * 30;
                headDisplay.setLocation(arrow.getLocation().setRotation(rotationAngle, 0f));
                if (arrow.isOnGround()) {
                    Location hit = arrow.getAttachedBlocks().getFirst().getLocation();
                    hit.createExplosion(4f, true, true);
                    arrow.remove();
                    headDisplay.remove();
                    iterator.remove();
                }
            }
        }
    }
}
