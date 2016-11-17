package com.example.peacemaker.oneplayer;

/**
 * Created by ouyan on 2016/10/15.
 */

public class EnvironmentReverbConfig {
    private int decayTime;
    private int decayHFTime;
    private short density;
    private short diffusion;
    private int reflectionsDelay;
    private short reflectionsLevel;
    private short reverbLevel;
    private int reverbDelay;
    private short roomHFLevel;
    private short roomLevel;

    public int getReverbDelay() {
        return reverbDelay;
    }

    public void setReverbDelay(int reverbDelay) {
        this.reverbDelay = reverbDelay;
    }

    public int getDecayTime() {
        return decayTime;
    }

    public void setDecayTime(int decayTime) {
        this.decayTime = decayTime;
    }

    public int getDecayHFTime() {
        return decayHFTime;
    }

    public void setDecayHFTime(int decayHFTime) {
        this.decayHFTime = decayHFTime;
    }

    public short getDensity() {
        return density;
    }

    public void setDensity(short density) {
        this.density = density;
    }

    public short getDiffusion() {
        return diffusion;
    }

    public void setDiffusion(short diffusion) {
        this.diffusion = diffusion;
    }

    public int getReflectionsDelay() {
        return reflectionsDelay;
    }

    public void setReflectionsDelay(int reflectionsDelay) {
        this.reflectionsDelay = reflectionsDelay;
    }

    public short getReflectionsLevel() {
        return reflectionsLevel;
    }

    public void setReflectionsLevel(short reflectionsLevel) {
        this.reflectionsLevel = reflectionsLevel;
    }

    public short getReverbLevel() {
        return reverbLevel;
    }

    public void setReverbLevel(short reverbLevel) {
        this.reverbLevel = reverbLevel;
    }

    public short getRoomHFLevel() {
        return roomHFLevel;
    }

    public void setRoomHFLevel(short roomHFLevel) {
        this.roomHFLevel = roomHFLevel;
    }

    public short getRoomLevel() {
        return roomLevel;
    }

    public void setRoomLevel(short roomLevel) {
        this.roomLevel = roomLevel;
    }
}
