package me.tormented.farmmancy.Commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.tormented.farmmancy.FarmConfig;
import me.tormented.farmmancy.FarmMancer.FarmMancer;
import me.tormented.farmmancy.FarmMancer.TickingCow;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class SetFarmMancerCommand {
    private static final int maxMobCap = FarmConfig.getInstance().getMaxMobCap();

    public SetFarmMancerCommand() {
        new CommandAPICommand("farmmancer")
                .withPermission("CommandPermission.OP")
                .withArguments(new StringArgument("set/remove"))
                .withArguments(new EntitySelectorArgument.ManyPlayers("Players", false))
                .withOptionalArguments(new IntegerArgument("amount", 0, maxMobCap))
                .withOptionalArguments(new BooleanArgument("isBaby"))
                .executes((sender, args) -> {
                    String doing = (String) args.get("set/remove");
                    boolean isBaby = (boolean) args.getOptional("isBaby").orElse(false);
                    int amountToSpawn = (int) args.getOptional("amount").orElse(8);

                    @SuppressWarnings("unchecked")
                    Collection<Player> Players = (Collection<Player>) args.get("Players");

                    if (Objects.equals(doing, "set")) {
                        if (Players != null) {
                            for (Player player : Players) {
                                FarmMancer cowmancer = TickingCow.getInstance().setCowMancer(player);
                                cowmancer.createAll(amountToSpawn, isBaby);
                            }
                            sender.sendMessage(Component.text("Granted FarmMancy to " + Players.size() + " player(s) successfully.", NamedTextColor.GREEN));
                        }
                    } else if (Objects.equals(doing, "remove")) {
                        if (Players != null) {
                            for (Player player : Players) {
                                TickingCow.getInstance().removeCowMancer(player);
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
