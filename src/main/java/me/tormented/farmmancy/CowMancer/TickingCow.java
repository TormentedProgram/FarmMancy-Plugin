package me.tormented.farmmancy.CowMancer;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class TickingCow implements Runnable {

    private static final TickingCow instance = new TickingCow();

    public static TickingCow getInstance() {
        return instance;
    }

    public Set<CowMancer> CowMancers = new HashSet<>();


    private TickingCow() {}

    public CowMancer setCowMancer(Player player) {
        for (CowMancer cowMancer : CowMancers) {
            if (cowMancer._player == player) {
                return cowMancer;
            }
        }
        CowMancer cowMancer = new CowMancer(player);
        CowMancers.add(cowMancer);
        return cowMancer;
    }

    public void removeCowMancer(Player player) {
        for (CowMancer cowMancer : CowMancers) {
            if (cowMancer._player == player) {
                cowMancer.cleanup(true);
                CowMancers.remove(cowMancer);
            }
        }
    }


    @Override
    public void run() {
        for (CowMancer cowMancer : CowMancers) {
            cowMancer.tick();
        }
    }
}
