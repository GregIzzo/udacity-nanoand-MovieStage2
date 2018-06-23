package com.example.android.udacity_nanoand_moviestage2.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Greg on 6/22/2018.
 */

@Dao
public interface FavoriteDao {

    @Query("Select * From favorite ORDER BY movieId")
    List<FavoriteEntry> loadAllFavorites();

    @Query("Select * From favorite where movieId = :whatId")
    FavoriteEntry loadFavoriteById(String whatId);

    @Insert
    void insertFavorite(FavoriteEntry favoriteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(FavoriteEntry favoriteEntry);

    @Query("DELETE FROM favorite WHERE movieId = :whatId")
    void deleteFavoriteById(String whatId);

    @Delete
    void deleteFavorite(FavoriteEntry favoriteEntry);

    @Query("DELETE FROM favorite")
    void deleteAllFavorite();


}
