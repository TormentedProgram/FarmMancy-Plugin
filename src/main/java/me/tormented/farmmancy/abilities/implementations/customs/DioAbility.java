package me.tormented.farmmancy.abilities.implementations.customs;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.Hook;
import me.tormented.farmmancy.abilities.MobuvertAbility;
import me.tormented.farmmancy.utils.HeadProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DioAbility extends MobuvertAbility<Bee> implements Hook.PlayerInteraction {

    @Override
    public Class<Bee> getEntityClass() {
        return Bee.class;
    }

    public DioAbility(@NotNull UUID id, @NotNull UUID owner) {
        super(id, owner);
    }

    public static final HeadProvider headProvider = new HeadProvider("http://textures.minecraft.net/texture/dbca394b91aae7960a3e5ebb121dcb88ab1058b5518000988801756c2b2e091c");

    @Override
    public @NotNull ItemStack getHeadItem(@Nullable Bee entity) {
        return headProvider.getHeadItem();
    }

    private boolean isTimeStopped = false;
    private Player timeStopper;

    @Override
    public void processPlayerInteract(PlayerInteractEvent event) {
        if (isTimeStopped && event.getPlayer() != timeStopper) {
            event.setCancelled(true);
        }
        if (event.getAction().isRightClick() && isBeingLookedAt()) {
            Player god = event.getPlayer();
            timeStopper = god;

            if (god != getOwnerPlayer()) return;

            Bukkit.broadcast(Component.text("ZA WARUDO!!!", NamedTextColor.YELLOW, TextDecoration.BOLD));
            Bukkit.broadcast(Component.text("A player cast TIME-STOP globally...", NamedTextColor.GREEN));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tick freeze");

            isTimeStopped = true;

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1, true, true)); // 20 ticks = 1 second
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 220, 255, true, false)); // 200 ticks = 10 seconds
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 220, -255, true, false)); // Prevent jumping
                player.playSound(player, Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 3f);
            }


            god.removePotionEffect(PotionEffectType.SLOWNESS);
            god.removePotionEffect(PotionEffectType.JUMP_BOOST);

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.removePotionEffect(PotionEffectType.SLOWNESS);
                        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                        player.playSound(player, Sound.ENTITY_ALLAY_DEATH, 1f, 0.8f);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1, true, true)); // 20 ticks = 1 second
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tick unfreeze");
                    Bukkit.broadcast(Component.text("Time resumes...", NamedTextColor.GREEN));
                    isTimeStopped = false;
                }
            }.runTaskLater(FarmMancy.getInstance(), 220); //11 seconds
        }
    }
}

