package com.example.uslstudentattendancescanner

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "AttendanceDatabase"
        const val DATABASE_VERSION = 1
        const val ATTENDANCE_TABLE = "AttendanceTable"
        const val COLUMN_ID = "id"
        const val STUDENT_ID = "studentId"
        const val STUDENT_NAME = "name"
        const val STUDENT_PROGRAM = "program"
        const val STUDENT_YEAR = "year"
        const val DATE = "date"
        const val TIME = "time"
    }

    // Create the table
    private val CREATE_TABLE =
        "CREATE TABLE $ATTENDANCE_TABLE (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$STUDENT_ID TEXT, " +
                "$STUDENT_NAME TEXT, " +
                "$STUDENT_PROGRAM TEXT, " +
                "$STUDENT_YEAR TEXT," +
                "$DATE TEXT," +
                "$TIME TEXT)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
    }
}
