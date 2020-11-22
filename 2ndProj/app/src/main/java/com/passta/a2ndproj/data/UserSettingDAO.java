package com.passta.a2ndproj.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserSettingDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserSettingDTO userSettingDTO);

    @Query("SELECT * FROM UserSetting")
    UserSettingDTO loadUserSetting();

    @Update
    void update(UserSettingDTO userSettingDTO);

}
