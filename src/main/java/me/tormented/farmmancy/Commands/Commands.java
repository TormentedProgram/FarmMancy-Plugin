package me.tormented.farmmancy.Commands;

public class Commands {
    private final static Commands instance = new Commands();

    public static Commands getInstance() {
        return instance;
    }

    public MenuCommand menuCommand;
    public imbueCowCommand imbueCowCommand;
    public SetFarmMancerCommand setFarmMancerCommand;

    private Commands() {}

    public void load() {
        menuCommand = new MenuCommand();
        imbueCowCommand = new imbueCowCommand();
        setFarmMancerCommand = new SetFarmMancerCommand();
    }
}
