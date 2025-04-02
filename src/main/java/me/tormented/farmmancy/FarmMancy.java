package me.tormented.farmmancy;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.tormented.farmmancy.Commands.Commands;
import me.tormented.farmmancy.CowMancer.CowMancer;
import me.tormented.farmmancy.CowMancer.TickingCow;
import me.tormented.farmmancy.inventoryMenu.GuiListener;
import me.tormented.farmmancy.inventoryMenu.Ticking;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public final class FarmMancy extends JavaPlugin {

    private BukkitTask taskMenuTick;
    private BukkitTask CowTick;

    public static @NotNull FarmMancy getInstance() {
        return getPlugin(FarmMancy.class);
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true));
    }

    @Override
    public void onEnable() {
        getLogger().info("@ FarmMancy Enabled");
        CommandAPI.onEnable();

        FarmConfig.getInstance().load();
        Commands.getInstance().load();

        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new EntityListener(), this);

        taskMenuTick = getServer().getScheduler().runTaskTimer(this, Ticking.getInstance(), 0, 1);
        CowTick = getServer().getScheduler().runTaskTimer(this, TickingCow.getInstance(), 0, 1);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

        if (CowTick != null && !CowTick.isCancelled())
            CowTick.cancel();

        if (taskMenuTick != null && !taskMenuTick.isCancelled())
            taskMenuTick.cancel();

        for (CowMancer cowMancer : TickingCow.getInstance().CowMancers) {
            cowMancer.cleanup(false);
            TickingCow.getInstance().CowMancers.remove(cowMancer);
        }
    }
}
