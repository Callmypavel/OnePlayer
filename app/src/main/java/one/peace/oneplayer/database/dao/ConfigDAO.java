package one.peace.oneplayer.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import one.peace.oneplayer.global.config.Config;

/**
 * Created by pavel on 2019/11/19.
 */
@Dao
public interface ConfigDAO {

    @Query("SELECT * FROM config")
    Config getConfig();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Config oneConfig);

    @Update
    void update(Config oneConfig);

    @Delete
    void delete(Config oneConfig);
}
