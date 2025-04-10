package me.tormented.farmmancy.abilities.implementations;

import me.tormented.farmmancy.abilities.AbilityFactory;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobuvertAbility;
import me.tormented.farmmancy.utils.HeadProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BeeAbility extends MobuvertAbility<Bee> implements Hook.PlayerInteraction {

    @Override
    public Class<Bee> getEntityClass() {
        return Bee.class;
    }

    public BeeAbility(@NotNull AbilityFactory abilityFactory, @NotNull UUID id, @NotNull UUID owner) {
        super(abilityFactory, id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/59ac16f296b461d05ea0785d477033e527358b4f30c266aa02f020157ffca736");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Bee entity) {
        return headProvider.getHeadItem();
    }


    @Override
    public void processPlayerInteract(@NotNull PlayerInteractEvent event) {
        super.processPlayerInteract(event);

        if (event.getAction().isRightClick() && isBeingLookedAt()) {
            Player player = event.getPlayer();

            if (player != getOwnerPlayer()) return;

            if (player.getHealth() <= 8f) {
                player.sendMessage(Component.text("You do not have enough health to use this ability!", NamedTextColor.RED));
                return;
            }
            player.setHealth(player.getHealth() - 8f);

            Vector velocityVector = player.getVelocity();
            velocityVector.setY(2f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 240, 1));
            player.setVelocity(velocityVector);
        }
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Bee Ability", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false);
    }
}

