package me.tormented.farmmancy.farmmancer;

import me.tormented.farmmancy.FarmMancy;
import me.tormented.farmmancy.abilities.EventDistributor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class HealthBossBar {
    private static final Map<Player, BossBar> DamagedBossBars = new HashMap<>();
    private static final Map<BossBar, BukkitTask> DamagedBossBarTimer = new HashMap<>();

    public static void showMobHealthBar(Player player, LivingEntity mob) {
        if (!EventDistributor.getInstance().playerAbilityMap.containsKey(player.getUniqueId())) {
            return;
        }
        if (DamagedBossBars.containsKey(player)) {
            BossBar bossBarInstance = DamagedBossBars.get(player);
            if (DamagedBossBarTimer.containsKey(bossBarInstance)) {
                BukkitTask task = DamagedBossBarTimer.get(bossBarInstance);
                if (task != null) {
                    task.cancel();
                }
            }
            bossBarInstance.removeAll();
            DamagedBossBarTimer.remove(bossBarInstance);
            DamagedBossBars.remove(player);
        }

        double health = mob.getHealth();
        double maxHealth = 0.0;
        AttributeInstance maxHPAttr = mob.getAttribute(Attribute.MAX_HEALTH);
        if (maxHPAttr != null) {
            maxHealth = maxHPAttr.getValue();
        }
        double progress = Math.max(0.0, Math.min(1.0, health / maxHealth));

        BarColor barColor = BarColor.RED;
        if (health <= (maxHealth / 2)) {
            barColor = BarColor.GREEN;
        }

        BossBar bossBar = Bukkit.createBossBar("Mob HP: " + (int) health + " / " + (int) maxHealth,
                barColor, BarStyle.SOLID);
        DamagedBossBars.put(player, bossBar);
        bossBar.setProgress(progress);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);

        DamagedBossBarTimer.put(bossBar, new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.removeAll();
                DamagedBossBars.remove(player);
            }
        }.runTaskLater(FarmMancy.getInstance(), 40L));
    }
}
