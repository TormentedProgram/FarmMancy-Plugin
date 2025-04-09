package me.tormented.farmmancy;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.tormented.farmmancy.abilities.EventDistributor;
import me.tormented.farmmancy.abilities.TickingAbilities;
import me.tormented.farmmancy.abilities.TickingTask;
import me.tormented.farmmancy.commands.Commands;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;
import me.tormented.farmmancy.inventoryMenu.GuiListener;
import me.tormented.farmmancy.inventoryMenu.Ticking;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public final class FarmMancy extends JavaPlugin {
    private BukkitTask taskMenuTick;
    private BukkitTask CowTick;
    private BukkitTask abilityTick;
    private BukkitTask DatabaseTick;

    public static @NotNull FarmMancy getInstance() {
        return getPlugin(FarmMancy.class);
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onEnable() {
        getLogger().info("--= FarmMancy Enabled =-- ");
        CommandAPI.onEnable();
        getLogger().info("--= FarmMancy CommandAPI Enabled =-- ");

        FarmConfig.getInstance().load();
        Commands.getInstance().load();

        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(EventDistributor.getInstance(), this);

        taskMenuTick = getServer().getScheduler().runTaskTimer(this, Ticking.getInstance(), 0, 1);
        CowTick = getServer().getScheduler().runTaskTimer(this, TickingAbilities.getInstance(), 0, 1);
        DatabaseTick = getServer().getScheduler().runTaskTimer(this, DatabaseAutosave.getInstance(), 0, 600);
        abilityTick = getServer().getScheduler().runTaskTimer(this, TickingTask.getInstance(), 0, 1);
    }

    @Override
    public void onDisable() {
        getLogger().info("--= FarmMancy Disabled =-- ");
        CommandAPI.onDisable();
        getLogger().info("--= FarmMancy CommandAPI Disabled =-- ");

        if (CowTick != null && !CowTick.isCancelled())
            CowTick.cancel();

        if (taskMenuTick != null && !taskMenuTick.isCancelled())
            taskMenuTick.cancel();

        if (abilityTick != null && !abilityTick.isCancelled())
            abilityTick.cancel();

        if (DatabaseTick != null && !DatabaseTick.isCancelled())
            DatabaseTick.cancel();

        for (FarmMancer farmMancer : FarmMancerManager.getInstance().farmMancers) {
            farmMancer.deactivateAll(false);
            FarmMancerManager.getInstance().farmMancerMap.remove(farmMancer._player);
            FarmMancerManager.getInstance().farmMancers.remove(farmMancer);
            EventDistributor.getInstance().playerAbilityMap.remove(farmMancer._player.getUniqueId());
        }
    }
}
