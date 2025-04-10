package me.tormented.farmmancy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.command.CommandSender;

public class FarmMancyCommand {
    public void displayHelp(CommandSender sender) {
        sender.sendMessage("/farmmancy - Show this help");
        sender.sendMessage("/farmmancy ability set <player> <slot> <unlocked_ability> - Sets the ability based on unlocked abilities for the specified player");
        sender.sendMessage("/farmmancy unlock <players> <ability> - Unlocks specific ability for the specified players");
        sender.sendMessage("/farmmancy farmmancer <add/remove> <players> <amount> <isBaby> - Sets the specified players as farmmancers with the specified amount and if they are babies");
    }

    public FarmMancyCommand() {
        new CommandAPICommand("farmmancy")
                .withPermission("farmmancy.help")
                .executes((sender, args) -> {
                    displayHelp(sender);
                })
                .register();

        new CommandAPICommand("farmmancy")
                .withSubcommand(
                    new CommandAPICommand("help")
                            .withPermission("farmmancy.help")
                            .executes((sender, args) -> {
                                displayHelp(sender);
                            }))
                            .register();
    }
}
