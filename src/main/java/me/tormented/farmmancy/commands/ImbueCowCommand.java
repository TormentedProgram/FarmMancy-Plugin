package me.tormented.farmmancy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.tormented.farmmancy.FarmConfig;
import me.tormented.farmmancy.abilities.utils.Wand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ImbueCowCommand {
    private final boolean enabled = FarmConfig.getInstance().getAllowedImbuement();

    public ImbueCowCommand() {
        new CommandAPICommand("imbueCowMagic")
                .withPermission("CommandPermission.OP")
                .executesPlayer((player, args) -> {
                    if (enabled) {
                        Wand wand = new Wand(player.getInventory().getItemInMainHand());
                        wand.convert();
                    } else {
                        player.sendMessage(Component.text("Sorry, this has been disabled in the config.", NamedTextColor.RED));
                    }
                })
                .register();
    }
}
