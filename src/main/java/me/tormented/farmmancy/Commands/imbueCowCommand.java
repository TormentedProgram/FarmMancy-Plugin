package me.tormented.farmmancy.Commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.tormented.farmmancy.FarmConfig;
import me.tormented.farmmancy.FarmMancer.FarmMancer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class imbueCowCommand {
    private final boolean enabled = FarmConfig.getInstance().getAllowedImbuement();

    public imbueCowCommand() {
        new CommandAPICommand("imbueCowMagic")
                .withPermission("CommandPermission.OP")
                .executesPlayer((player, args) -> {
                    if (enabled) {
                        FarmMancer.Cowification(player.getInventory().getItemInMainHand());
                    } else {
                        player.sendMessage(Component.text("Sorry, this has been disabled in the config.", NamedTextColor.RED));
                    }
                })
                .register();
    }
}
