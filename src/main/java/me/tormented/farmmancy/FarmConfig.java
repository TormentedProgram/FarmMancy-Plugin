package me.tormented.farmmancy;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FarmConfig {
    private final static FarmConfig instance = new FarmConfig();

    private File file;
    private YamlConfiguration config;

    private boolean allow_imbuement;
    private int max_mob_cap;

    private float cow_explosion_radius;
    private float cow_shoot_velocity;

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
        max_mob_cap = config.getInt("global.max-mob-cap");

        cow_explosion_radius = (float) config.getInt("mobs.cow.explosion-radius");
        cow_shoot_velocity = (float) config.getInt("mobs.cow.shoot-velocity");
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

    public float getCowExplosionRadius() {
        return cow_explosion_radius;
    }

    public float getCowShootVelocity() {
        return cow_shoot_velocity;
    }

    public boolean getAllowedImbuement() {
        return allow_imbuement;
    }

    public int getMaxMobCap() {
        return max_mob_cap;
    }

    @SuppressWarnings("unused")
    public void setAllowedImbuement(boolean value) {
        this.allow_imbuement = value;
        set("items.allow-imbuement", value);
    }

    @SuppressWarnings("unused")
    public void setMaxMobCap(int value) {
        this.max_mob_cap = value;
        set("global.max-mob-cap", value);
    }

    public static FarmConfig getInstance() {
        return instance;
    }
}
