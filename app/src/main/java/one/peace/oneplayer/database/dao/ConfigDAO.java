package one.peace.oneplayer.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import one.peace.oneplayer.global.config.OneConfig;

/**
 * Created by pavel on 2019/11/19.
 */
@Dao
public interface ConfigDAO {

    @Query("SELECT * FROM config")
    OneConfig getConfig();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OneConfig oneConfig);

    @Update
    void update(OneConfig oneConfig);

    @Delete
    void delete(OneConfig oneConfig);
}
