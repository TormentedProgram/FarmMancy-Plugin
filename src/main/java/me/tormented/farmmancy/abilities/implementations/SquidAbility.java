package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobuvertAbility;
import me.tormented.farmmancy.utils.HeadProvider;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SquidAbility extends MobuvertAbility<Squid> implements Hook.PlayerInteraction {

    @Override
    public Class<Squid> getEntityClass() {
        return Squid.class;
    }

    public SquidAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/49c2c9ce67eb5971cc5958463e6c9abab8e599adc295f4d4249936b0095769dd");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Squid entity) {
        return headProvider.getHeadItem();
    }


    @Override
    public void processPlayerInteract(PlayerInteractEvent event) {
        super.processPlayerInteract(event);

        if (event.getAction().isRightClick() && isBeingLookedAt()) {
            Player player = event.getPlayer();

            if (player != getOwnerPlayer()) return;

            player.setHealth(player.getHealth() - 8f);

            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 500, 1));
        }
    }
}

