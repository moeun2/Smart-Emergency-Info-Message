package com.passta.a2ndproj.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MsgDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MsgDTO msgDTO);

    @Query("SELECT * FROM Msg")
    List<MsgDTO> loadMsgList();

    @Update
    void update(MsgDTO msgDTO);

    @Query("DELETE FROM Msg WHERE location = :location")
    void delete(String location);



}
