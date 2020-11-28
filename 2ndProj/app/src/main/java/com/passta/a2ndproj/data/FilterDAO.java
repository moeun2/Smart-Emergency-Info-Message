package com.passta.a2ndproj.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FilterDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FilterDTO filterDTO);

    @Query("SELECT * FROM Filter")
    List<FilterDTO> loadFilterList();


    @Update
    void update(FilterDTO FilterDTO);
}
