package br.com.nareba.nspawn.core;

import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;

public class Spawn {
    private double posX;
    private double posY;
    private double posZ;
    private String levelName;
    public Spawn (Position position, String levelName)   {
        this.posX = position.getX();
        this.posY = position.getY();
        this.posZ = position.getZ();
        this.levelName = levelName;
    }
    public void setSpawnPosition(Position position)   {
        this.posX = position.getX();
        this.posY = position.getY();
        this.posZ = position.getZ();
    }
    public Vector3 getSpawnPosition()   {
        return new Vector3(this.posX, this.posY, this.posZ);
    }
    public String getLevelName()   {
        return this.levelName;
    }
    public void setLevelName(String levelName)   {
        this.levelName = levelName;
    }
}
