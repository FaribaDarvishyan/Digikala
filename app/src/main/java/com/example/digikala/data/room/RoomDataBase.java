package com.example.digikala.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.digikala.data.room.dao.AddressDAO;
import com.example.digikala.data.room.dao.CartDAO;
import com.example.digikala.data.room.entities.Cart;
import com.example.digikala.data.room.entities.MyAddress;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Cart.class, MyAddress.class},
        version = 1,
        exportSchema = false)
public abstract class RoomDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "online_market.db";
    private static final int NUMBER_OF_THREADS = 4;

    public static ExecutorService dataBaseWriteExecutor = Executors.newFixedThreadPool(4);

    public abstract CartDAO getCardDAO();

    public abstract AddressDAO getAddressDAO();

    public static RoomDataBase getDataBase(Context context) {
        return Room.databaseBuilder(context, RoomDataBase.class, DATABASE_NAME).build();
    }

}
