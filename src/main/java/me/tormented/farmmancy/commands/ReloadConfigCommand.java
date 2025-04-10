package me.tormented.farmmancy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.tormented.farmmancy.FarmConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ReloadConfigCommand {
    @SuppressWarnings("SpellCheckingInspection")
    public ReloadConfigCommand() {
        new CommandAPICommand("farmmancy")
                .withSubcommand(
                    new CommandAPICommand("reload")
                            .withSubcommand(
                                new CommandAPICommand("config")
                                    .withPermission("farmmancy.reload.config")
                                    .executes((player, args) -> {
                                        FarmConfig.getInstance().load();
                                        player.sendMessage(Component.text("You successfully reloaded the Farmmancy Configuration.", NamedTextColor.GREEN));
                                    })))
                                    .register();
    }
}
