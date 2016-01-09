package com.github.badoualy.badoualyve.model;

import java.util.Observable;

public class Player {

    private String name = "";
    private int power = 10;
    private int def = 10;
    private int speed = 30;
    private int magic = 40;
    private int stamina = 100;

    public Player(String name, int power, int def, int speed, int magic, int stamina) {
        this.name = name;
        this.power = power;
        this.def = def;
        this.speed = speed;
        this.magic = magic;
        this.stamina = stamina;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        if (power >= 100000)
            power = 100000;
        this.power = power;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        if (def >= 100000)
            def = 100000;
        this.def = def;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        if (speed >= 100000)
            speed = 100000;
        this.speed = speed;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        if (magic >= 100000)
            magic = 100000;
        this.magic = magic;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        if (stamina >= 100000)
            stamina = 100000;
        this.stamina = stamina;
    }
}
