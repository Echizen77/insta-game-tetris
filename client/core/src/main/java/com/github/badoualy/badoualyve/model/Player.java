package com.github.badoualy.badoualyve.model;

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
        this.power = power;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }
}
