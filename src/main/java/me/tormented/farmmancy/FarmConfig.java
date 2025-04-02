package me.tormented.farmmancy;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FarmConfig {
    private final static FarmConfig instance = new FarmConfig();

    private File file;
    private YamlConfiguration config;

    private boolean allow_imbuement;
    private int max_mob_cap;

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
            error.printStackTrace();
        }

        //below is loading the config
        allow_imbuement = config.getBoolean("items.allow-imbuement");
        max_mob_cap = config.getInt("global.max-mob-cap");
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public boolean getAllowedImbuement() {
        return allow_imbuement;
    }
    public int getMaxMobCap() {
        return max_mob_cap;
    }

    public void setAllowedImbuement(boolean value) {
        this.allow_imbuement = value;
        set("items.allow-imbuement", value);
    }

    public void setMaxMobCap(int value) {
        this.max_mob_cap = value;
        set("global.max-mob-cap", value);
    }

    public static FarmConfig getInstance() {
        return instance;
    }
}
