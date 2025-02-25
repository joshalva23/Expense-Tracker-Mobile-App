package com.joshdev.expensetracker.data.local

import com.joshdev.expensetracker.data.entity.ExpenseEntity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.joshdev.expensetracker.data.entity.CategoryEntity
import com.joshdev.expensetracker.data.entity.IncomeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities= [
        ExpenseEntity::class,
        CategoryEntity::class,
        IncomeEntity::class
              ],
    version = 5
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun incomeDao(): IncomeDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getInstance(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                ).addCallback(DatabaseCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            addCategories()
        }

        override fun onCreate(db: SupportSQLiteDatabase){
            super.onCreate(db)
            addCategories()
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            addCategories()
        }

        fun addCategories(){
            CoroutineScope(Dispatchers.IO).launch {
                val dao = getInstance(context).categoryDao()
                dao.insertCategories(
                    listOf(
                        CategoryEntity(id = 1, name = "One-Time", icon = null),
                        CategoryEntity(id = 2, name = "Weekly", icon = null),
                        CategoryEntity(id = 3, name = "Monthly", icon = null),
                        CategoryEntity(id = 4, name = "Annually", icon = null)
                    )
                )
                println("DEBUG: Categories inserted successfully!")
            }
        }

    }
}