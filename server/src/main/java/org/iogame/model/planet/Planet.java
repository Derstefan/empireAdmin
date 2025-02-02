package org.iogame.model.planet;

import org.iogame.core.GameObject;
import org.iogame.exceptions.NotBuildableException;
import org.iogame.model.battle.Battle;
import org.iogame.model.fleet.Fleet;
import org.iogame.model.planet.buildings.EBuilding;
import org.iogame.model.player.Team;
import org.iogame.model.research.ETech;

import java.util.ArrayList;
import java.util.List;

public class Planet extends GameObject {

    private ResourceDeposit resourceDeposit;
    private Storage storage;
    private BuildingManager buildingManager;
    private MiningManager miningManager;


    private Battle battle = null;

    // fleets on/around this planet
    private final List<Fleet> fleets = new ArrayList<>();

    public Planet(double x, double y) {
        super(x, y);
        this.resourceDeposit = new ResourceDeposit();
        this.storage = new Storage();
        this.buildingManager = new BuildingManager(storage, this);
        this.miningManager = new MiningManager(resourceDeposit, storage);
        if(checkPeace()){
            battle = null;
        } else {
            battle = new Battle(this);
        }
    }

    @Override
    public void update(long delta) {
        buildingManager.update(delta);
        miningManager.update(delta);
        if (getBattle() != null) {
            //Battles
            getBattle().update(delta);
        }
    }
    // Player Actions -----------------------------------------------------------------
    public void build(EBuilding BuildingType) throws NotBuildableException{
        buildingManager.startBuild(BuildingType);
    }

    public void cancelBuilding(int queueIndex){
        buildingManager.cancel(queueIndex);
    }




    /*
    Sets all available buildings/ships, building boni, etc
     */
    public void updateBuildingStats(){
        buildingManager.resetBuildingStats();
        miningManager.resetBuildingStats();
        storage.resetBuildingStats();

        for(EBuilding b: buildingManager.getBuildings().keySet()){
            buildingManager.getBuildings().get(b).activate(this);
        }
        buildingManager.updateAvailableBuildings(getTechs());
    }

    public void resetTechStats(){
        getBuildingManager().resetTechStats();
        getMiningManager().resetTechStats();
        getStorage().resetTechStats();
    }


    private List<ETech> getTechs(){
        if(!getOwner().isEmpty()){
            return getOwner().get().getTechManager().getdevelopedTech();
        }
        return new ArrayList<ETech>();
    }

    public boolean checkPeace() {
        if (!fleets.isEmpty()) {
            Team t = fleets.get(0).getPlayer().getTeam();
            for (Fleet f : fleets) {

                if (!f.getPlayer().getTeam().equals(t)) {
                    return false;
                }
            }
        }
        return true;
    }





    /*public boolean destroyBuilding(EBuilding BuildingType) {
        return buildingManager.destroy(BuildingType);
    }*/



    // Getter Setter

    public List<Fleet> getFleets() {
        return fleets;
    }

    public ResourceDeposit getResource() {
        return resourceDeposit;
    }

    public Storage getStorage() {
        return storage;
    }

    public ResourceDeposit getResourceDeposit() {
        return resourceDeposit;
    }

    public void setResourceDeposit(ResourceDeposit resourceDeposit) {
        this.resourceDeposit = resourceDeposit;
    }

    public BuildingManager getBuildingManager() {
        return buildingManager;
    }

    public void setBuildingManager(BuildingManager buildingManager) {
        this.buildingManager = buildingManager;
    }

    public MiningManager getMiningManager() {
        return miningManager;
    }

    public void setMiningManager(MiningManager miningManager) {
        this.miningManager = miningManager;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }
}
