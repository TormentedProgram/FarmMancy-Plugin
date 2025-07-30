package me.tormented.farmmancy;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FarmConfig {
    private final static FarmConfig instance = new FarmConfig();

    private File file;
    private YamlConfiguration config;

    private boolean allow_imbuement;
    private int max_mob_cap;

    //creeper
    private float creeper_explosion_radius;
    private float creeper_shoot_velocity;

    //pig
    private float pig_healing_amount;

    //database
    private int database_interval;

    private FarmConfig() {
    }

    public void load() {
        file = new File(FarmMancy.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            FarmMancy.getInstance().saveResource("config.yml", false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        } catch (Exception error) {
            FarmMancy.getInstance().getLogger().warning("An error occurred: " + error);
        }

        //below is loading the config
        allow_imbuement = config.getBoolean("items.allow-imbuement");
        max_mob_cap = config.getInt("mobs.max-mob-cap");

        creeper_explosion_radius = (float) config.getInt("mobs.creeper.explosion-radius");
        creeper_shoot_velocity = (float) config.getInt("mobs.creeper.shoot-velocity");
        pig_healing_amount = (float) config.getInt("mobs.pig.healing-amount");

        database_interval = config.getInt("database.interval");
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception error) {
            FarmMancy.getInstance().getLogger().warning("An error occurred: " + error);
        }
    }

    @SuppressWarnings("unused")
    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public float getCreeperExplosionRadius() {
        return creeper_explosion_radius;
    }

    public float getCreeperShootVelocity() {
        return creeper_shoot_velocity;
    }

    public boolean getAllowedImbuement() {
        return allow_imbuement;
    }

    public int getMaxMobCap() {
        return max_mob_cap;
    }

    public float getPigHealingAmount() {
        return pig_healing_amount;
    }

    public int getDatabaseInterval() {
        return database_interval;
    }

    public static FarmConfig getInstance() {
        return instance;
    }
}
