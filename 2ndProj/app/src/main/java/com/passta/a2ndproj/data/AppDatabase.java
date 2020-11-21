package com.passta.a2ndproj.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {UserListDTO.class,FilterDTO.class}, version=1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserListDAO userListDAO();
    public abstract FilterDAO filterDAO();

    private static AppDatabase mAppDatabase;

    public static AppDatabase getInstance(Context context) {
        if(mAppDatabase==null){
            mAppDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "UserList-db").build();
        }
        return mAppDatabase;
    }
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
