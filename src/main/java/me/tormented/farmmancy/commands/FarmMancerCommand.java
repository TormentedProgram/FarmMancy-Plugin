package me.tormented.farmmancy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import me.tormented.farmmancy.FarmConfig;
import me.tormented.farmmancy.abilities.utils.WandUtils;
import me.tormented.farmmancy.farmmancer.FarmMancer;
import me.tormented.farmmancy.farmmancer.FarmMancerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class FarmMancerCommand {
    private static final int maxMobCap = FarmConfig.getInstance().getMaxMobCap();

    public FarmMancerCommand() {
        new CommandAPICommand("farmmancer")
                .withPermission("CommandPermission.OP")
                .withArguments(new MultiLiteralArgument("doing", "set", "remove"))
                .withArguments(new EntitySelectorArgument.ManyPlayers("Players", false))
                .withOptionalArguments(new IntegerArgument("amount", 0, maxMobCap))
                .withOptionalArguments(new BooleanArgument("isBaby"))
                .executes((sender, args) -> {
                    String doing = (String) args.get("doing");
                    boolean isBaby = (boolean) args.getOptional("isBaby").orElse(false);
                    int amountToSpawn = (int) args.getOptional("amount").orElse(8);

                    @SuppressWarnings("unchecked")
                    Collection<Player> Players = (Collection<Player>) args.get("Players");

                    if (Objects.equals(doing, "set")) {
                        if (Players != null) {
                            for (Player player : Players) {
                                FarmMancer theMancer = FarmMancerManager.getInstance().setFarmMancer(player);
                                WandUtils.giveWandIfMissing(player);
                                theMancer.activateAll(amountToSpawn, isBaby);
                            }
                            sender.sendMessage(Component.text("Granted FarmMancy to " + Players.size() + " player(s) successfully.", NamedTextColor.GREEN));
                        }
                    } else if (Objects.equals(doing, "remove")) {
                        if (Players != null) {
                            for (Player player : Players) {
                                FarmMancerManager.getInstance().removeFarmMancer(player);
                            }
                            sender.sendMessage(Component.text("Removed FarmMancy from " + Players.size() + " player(s) successfully.", NamedTextColor.GREEN));
                        }
                    } else {
                        sender.sendMessage(Component.text("You didn't provide a valid first argument.", NamedTextColor.RED));
                    }
                })
                .register();
    }
}
