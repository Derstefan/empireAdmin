package org.iogame.model.player;

import org.iogame.core.Id;
import org.iogame.model.planet.Planet;
import org.iogame.model.research.ETech;
import org.iogame.model.research.TechManager;

import java.util.ArrayList;
import java.util.List;


public class Player {

    private final Id id;

    private final String name;
    private Team team;
    private TechManager techManager;
    private List<Planet> controlledPlanets = new ArrayList<Planet>();

    public Player(String name, Team team) {
        this.id = Id.generateForClass(Player.class);

        this.name = name;
        this.team = team;
        this.techManager = new TechManager();
    }


    // Add tech to developped Techs
    public void develop(ETech tech){

        if(techManager.develop(tech)) {
            // execute techfunctions
            tech.activate(this);
            //update buildingStats
            for (Planet p : controlledPlanets) {
                p.updateBuildingStats();
            }
        }
    }


    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public TechManager getTechManager() {
        return techManager;
    }

    public void setTechManager(TechManager techManager) {
        this.techManager = techManager;
    }

    public List<Planet> getControlledPlanets() {
        return controlledPlanets;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Player)) return false;
        return this.id.equals(((Player) obj).id);
    }
}
