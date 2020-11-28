package com.passta.a2ndproj.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserListDAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserListDTO userListDTO);

    @Query("SELECT * FROM UserList")
    List<UserListDTO> loadUserList();

    @Query("DELETE FROM UserList WHERE tag = :tag")
    void delete(String tag);

    @Query("UPDATE UserList SET is_hastag_ckecked = :isChecked WHERE tag = :tag")
    void updateHastagChecked(boolean isChecked,String tag);

}
