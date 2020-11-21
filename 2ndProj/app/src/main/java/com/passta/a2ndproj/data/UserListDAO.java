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

}
