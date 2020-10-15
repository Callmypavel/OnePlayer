package one.peace.oneplayer.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import one.peace.oneplayer.global.config.Config;
import one.peace.oneplayer.global.config.SoundEffectConfig;

/**
 * Created by pavel on 2019/11/19.
 */
@Dao
public interface SoundEffectConfigDAO {

    @Query("SELECT * FROM SoundEffectConfig")
    SoundEffectConfig getSoundEffectConfig();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SoundEffectConfig oneConfig);

    @Update
    void update(SoundEffectConfig oneConfig);

    @Delete
    void delete(SoundEffectConfig oneConfig);
}
