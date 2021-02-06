package com.hardgforgif.dragonboatracing.core;

import java.util.ArrayList;

import com.hardgforgif.dragonboatracing.Game;
import com.badlogic.gdx.physics.box2d.Body;

public class PowerUpBomb extends PowerUp {

    public PowerUpBomb() {
        super("PowerUps/ObstacleClearer.png");
        setObstacleType(7);
    }

    @Override
    public void applyPowerUp(Boat user) {
        //Clear the nearest numToClear Obstacles in this lane.
        int numToClear = 2;
        //Sorting obstacles in lane by proximity.
        Lane thisLane = user.getLane();
        Obstacle[] laneObstacles = thisLane.getObstacles();
        ArrayList<Obstacle> sortedObstacles = new ArrayList<Obstacle>();
        for(int i = 0; i < laneObstacles.length; i++) {
            if(laneObstacles[i] != null && !laneObstacles[i].isPowerUp()) {
                if (sortedObstacles.isEmpty()) {
                    sortedObstacles.add(laneObstacles[i]);
                } else {
                    for (int j = 0; j < sortedObstacles.size(); j++) {
                        if (laneObstacles[i].getY() < sortedObstacles.get(j).getY()) {
                            if (laneObstacles[i].isPowerUp()) {
                                sortedObstacles.add(j, laneObstacles[i]);
                            }
                            break;
                        }
                    }
                    //if (laneObstacles[i].isPowerUp()) {
                        //sortedObstacles.add(laneObstacles[i]);
                    //}
                }
            }
        }
        ArrayList<Body> toClear = new ArrayList<Body>();
        for(int i = 0; i < sortedObstacles.size(); i++) {
            if(sortedObstacles.get(i).getY() > user.getBoatBody().getPosition().y) {
                //The current i is the first instance of an Obstacle at a greater Y than the Boat.
                for(int j = 0; j < numToClear; j++) {
                    //So clear (up to) numToClear many Obstacles from this point onwards.
                    if(i + j < sortedObstacles.size()) {
                        toClear.add(sortedObstacles.get(i + j).getObstacleBody());
                    }
                }
                break;
            }
        }
        toClear.addAll(Game.getToBeRemovedBodies());
        Game.setToBeRemovedBodies(toClear);
        //
        System.out.println(toClear);
    }

}
