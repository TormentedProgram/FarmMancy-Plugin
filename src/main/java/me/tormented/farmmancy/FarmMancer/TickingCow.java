package me.tormented.farmmancy.FarmMancer;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class TickingCow implements Runnable {

    private static final TickingCow instance = new TickingCow();

    public static TickingCow getInstance() {
        return instance;
    }

    public Set<FarmMancer> CowMancers = new HashSet<>();


    private TickingCow() {
    }

    public FarmMancer setCowMancer(Player player) {
        for (FarmMancer cowMancer : CowMancers) {
            if (cowMancer._player == player) {
                return cowMancer;
            }
        }
        FarmMancer cowMancer = new FarmMancer(player);
        CowMancers.add(cowMancer);
        return cowMancer;
    }

    public void removeCowMancer(Player player) {
        for (FarmMancer cowMancer : CowMancers) {
            if (cowMancer._player == player) {
                cowMancer.cleanup(true);
                CowMancers.remove(cowMancer);
            }
        }
    }


    @Override
    public void run() {
        for (FarmMancer cowMancer : CowMancers) {
            cowMancer.tick();
        }
    }
}
