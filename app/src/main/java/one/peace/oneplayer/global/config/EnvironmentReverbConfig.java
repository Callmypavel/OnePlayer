package one.peace.oneplayer.global.config;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import one.peace.oneplayer.BR;
/**
 * Created by pavel on 2019/11/19.
 */
@Entity
public class EnvironmentReverbConfig extends BaseObservable {
    @PrimaryKey
    private int reverbConfigId = 1;
    @ColumnInfo(name="decay_time")
    private int decayTime;
    @ColumnInfo(name="decay_hf_time")
    private int decayHFTime;
    @ColumnInfo(name="density")
    private short density;
    @ColumnInfo(name="diffusion")
    private short diffusion;
    @ColumnInfo(name="reflections_delay")
    private int reflectionsDelay;
    @ColumnInfo(name="reflections_level")
    private short reflectionsLevel;
    @ColumnInfo(name="reverb_level")
    private short reverbLevel;
    @ColumnInfo(name="reverb_delay")
    private int reverbDelay;
    @ColumnInfo(name="room_hf_level")
    private short roomHFLevel;
    @ColumnInfo(name="room_level")
    private short roomLevel;

    @Bindable
    public int getReverbDelay() {
        return reverbDelay;
    }

    public void setReverbDelay(int reverbDelay) {
        this.reverbDelay = reverbDelay;
        notifyPropertyChanged(BR.reverbDelay);
    }

    @Bindable
    public int getDecayTime() {
        return decayTime;
    }

    public void setDecayTime(int decayTime) {
        this.decayTime = decayTime;
        notifyPropertyChanged(BR.decayTime);
    }

    @Bindable
    public int getDecayHFTime() {
        return decayHFTime;
    }

    public void setDecayHFTime(int decayHFTime) {
        this.decayHFTime = decayHFTime;
        notifyPropertyChanged(BR.decayHFTime);
    }

    @Bindable
    public short getDensity() {
        return density;
    }

    public void setDensity(short density) {
        this.density = density;
        notifyPropertyChanged(BR.density);
    }

    @Bindable
    public short getDiffusion() {
        return diffusion;
    }

    public void setDiffusion(short diffusion) {
        this.diffusion = diffusion;
        notifyPropertyChanged(BR.diffusion);
    }

    @Bindable
    public int getReflectionsDelay() {
        return reflectionsDelay;
    }

    public void setReflectionsDelay(int reflectionsDelay) {
        this.reflectionsDelay = reflectionsDelay;
        notifyPropertyChanged(BR.reflectionsDelay);
    }

    @Bindable
    public short getReflectionsLevel() {
        return reflectionsLevel;
    }

    public void setReflectionsLevel(short reflectionsLevel) {
        this.reflectionsLevel = reflectionsLevel;
        notifyPropertyChanged(BR.reflectionsLevel);
    }

    @Bindable
    public short getReverbLevel() {
        return reverbLevel;
    }

    public void setReverbLevel(short reverbLevel) {
        this.reverbLevel = reverbLevel;
        notifyPropertyChanged(BR.reverbLevel);
    }

    @Bindable
    public short getRoomHFLevel() {
        return roomHFLevel;
    }

    public void setRoomHFLevel(short roomHFLevel) {
        this.roomHFLevel = roomHFLevel;
        notifyPropertyChanged(BR.roomHFLevel);
    }

    @Bindable
    public short getRoomLevel() {
        return roomLevel;
    }

    public void setRoomLevel(short roomLevel) {
        this.roomLevel = roomLevel;
        notifyPropertyChanged(BR.roomLevel);
    }
}
