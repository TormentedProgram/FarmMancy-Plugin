package me.tormented.farmmancy.abilities;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.Registries;
import me.tormented.farmmancy.abilities.utils.Wand;
import me.tormented.farmmancy.abilities.utils.WandUtils;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static me.tormented.farmmancy.abilities.utils.WandUtils.isHoldingWand;

public class EventDistributor implements Listener {

    private static final EventDistributor instance = new EventDistributor();

    public static EventDistributor getInstance() {
        return instance;
    }

    private Map<Player, BossBar> DamagedBossbars = new HashMap<>();
    private Map<BossBar, BukkitTask> DamagedBossbarTimer = new HashMap<>();

    private EventDistributor() {
    }

    public final Map<Entity, MobAbility<? extends Entity>> entityMobunitionAbilityMap = new HashMap<>();
    public final Map<UUID, FarmMancer> playerAbilityMap = new HashMap<>();

    @EventHandler
    public void onEntityDamage(@NotNull EntityDamageEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDamaged entityDamaged) entityDamaged.processEntityDamage(event);
        }
    }

    @EventHandler
    public void playerMove(@NotNull PlayerMoveEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.playerMove playerSneak) playerSneak.processPlayerMove(event);
            }
        }
    }

    @EventHandler
    public void onEntityDamagedByEntity(@NotNull EntityDamageByEntityEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (event.getDamager() instanceof Player player && event.getEntity() instanceof LivingEntity livingEntity) {
            showMobHealthBar(player, livingEntity);
        }
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDamagedByEntity entityDamaged)
                entityDamaged.processEntityDamagedByEntity(event);
        }
    }

    @EventHandler
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityDeath entityDeath) entityDeath.processEntityDeath(event);
        }
    }

    @EventHandler
    public void onSneak(@NotNull PlayerToggleSneakEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerSneak playerSneak) playerSneak.processSneakToggle(event);
            }
        }
    }

    @EventHandler
    public void onSwapItem(@NotNull PlayerItemHeldEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerSwapItem playerSwap) playerSwap.processSwapItem(event);
            }
        }
    }

    @EventHandler
    public void onEntityMove(@NotNull EntityMoveEvent event) {
        MobAbility<? extends Entity> mobAbility = entityMobunitionAbilityMap.get(event.getEntity());
        if (mobAbility != null) {
            if (mobAbility instanceof Hook.EntityMoved entityMoved) entityMoved.processEntityMove(event);
        }
    }

    @EventHandler
    public void onPlayerInteraction(@NotNull PlayerInteractEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerInteraction playerInteraction)
                    playerInteraction.processPlayerInteract(event);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        FarmMancer theMancer = FarmMancerManager.getInstance().setFarmMancer(event.getPlayer());
        WandUtils.giveWandIfMissing(event.getPlayer());
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer == null) return;
        FarmMancerManager.getInstance().FarmMancerToUnload.remove(farmMancer);
        for (Ability ability : farmMancer.getEquippedAbilities()) {
            if (ability != null) {
                if (ability instanceof Hook.PlayerJoining playerJoining) playerJoining.processPlayerJoin(event);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        FarmMancer farmMancer = playerAbilityMap.get(event.getPlayer().getUniqueId());
        if (farmMancer != null) {
            FarmMancerManager.getInstance().FarmMancerToUnload.add(farmMancer);
            for (Ability ability : farmMancer.getEquippedAbilities()) {
                if (ability != null) {
                    if (ability instanceof Hook.PlayerJoining playerJoining) playerJoining.processPlayerQuit(event);
                }
            }
        }
    }

    public void showMobHealthBar(Player player, LivingEntity mob) {
        if (DamagedBossbars.containsKey(player)) {
            BossBar bossBarInstance = DamagedBossbars.get(player);
            if (DamagedBossbarTimer.containsKey(bossBarInstance)) {
                BukkitTask task = DamagedBossbarTimer.get(bossBarInstance);
                if (task != null) {
                    task.cancel();
                }
            }
            bossBarInstance.removeAll();
            DamagedBossbarTimer.remove(bossBarInstance);
            DamagedBossbars.remove(player);
        }

        double health = mob.getHealth();
        double maxHealth = mob.getAttribute(Attribute.MAX_HEALTH).getValue();
        double progress = Math.max(0.0, Math.min(1.0, health / maxHealth));

        BossBar bossBar = Bukkit.createBossBar("Mob HP: " + (int) health + " / " + (int) maxHealth,
                BarColor.RED, BarStyle.SOLID);
        DamagedBossbars.put(player, bossBar);
        bossBar.setProgress(progress);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);

        // Remove after 2 seconds (40 ticks)
        DamagedBossbarTimer.put(bossBar, new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.removeAll();
                DamagedBossbars.remove(player);
            }
        }.runTaskLater(FarmMancy.getInstance(), 40L));
    }

    @EventHandler
    public void onPlayerInteractWithEntity(@NotNull PlayerInteractEntityEvent event) {
        if (event.isCancelled()) return;

        if (entityMobunitionAbilityMap.get(event.getRightClicked()) instanceof MobAbility<? extends Entity> mobAbility && mobAbility instanceof Hook.EntityInteractedByPlayer entityInteractedByPlayer) {
            entityInteractedByPlayer.processPlayerInteractEntity(event, Hook.CallerSource.TRACKED_ENTITY);
        }

        if (playerAbilityMap.get(event.getPlayer().getUniqueId()) instanceof FarmMancer farmMancer) {
            Entity entity = event.getRightClicked();
            if (!isHoldingWand(event.getPlayer()) && Registries.abilityRegistry.getFactory(entity.getType().getKey().asString()) instanceof AbilityFactory abilityFactory) {
                if (entity instanceof LivingEntity livingEntity && livingEntity.getAttribute(Attribute.MAX_HEALTH) != null) {
                    AttributeInstance maxHealthAttribute = livingEntity.getAttribute(Attribute.MAX_HEALTH);
                    if (maxHealthAttribute != null) {
                        if (livingEntity.getHealth() < (maxHealthAttribute.getValue() / 3)) {
                            if (!farmMancer.isAbilityUnlocked(abilityFactory)) {
                                switch (farmMancer.unlockAbility(abilityFactory)) {
                                    case MobunitionAbility<?> mobunitionAbility -> {
                                        mobunitionAbility.addMob(livingEntity);
                                    }
                                    case MobuvertAbility<?> mobuvertAbility -> {
                                        mobuvertAbility.setMob(livingEntity);
                                    }
                                    case null, default -> {
                                    }
                                }
                                livingEntity.remove();
                                return;
                            }
                        } else {
                            event.getPlayer().sendMessage(Component.text("This mob is too powerful to be captured, try weakening it!").color(NamedTextColor.RED));
                            //being called twice????
                            return;
                        }
                    }
                }
            }

            Iterator<Ability> unlockedAbilities = farmMancer.getUnlockedAbilities();
            while (unlockedAbilities.hasNext()) {
                Ability ability = unlockedAbilities.next();
                if (ability instanceof Hook.EntityInteractedByPlayer entityInteractedByPlayer) {
                    entityInteractedByPlayer.processPlayerInteractEntity(event, Hook.CallerSource.PLAYER);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
        if (playerAbilityMap.get(event.getPlayer().getUniqueId()) instanceof FarmMancer farmMancer) {
            for (Ability ability : farmMancer.getEquippedAbilities()) {
                if (ability instanceof Hook.PlayerDroppingItem playerDroppingItem) {
                    playerDroppingItem.processPlayerDropItem(event);
                }
            }
        }

        Wand checkingWand = new Wand(event.getItemDrop().getItemStack());
        if (checkingWand.isWand() && checkingWand.clearBoundAbility()) {
            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_ANVIL_LAND, 1.0f, 2.0f);
            event.getPlayer().sendMessage(Component.text("Wand ability unbounded", NamedTextColor.YELLOW));
            event.setCancelled(true);
        }
    }


}
