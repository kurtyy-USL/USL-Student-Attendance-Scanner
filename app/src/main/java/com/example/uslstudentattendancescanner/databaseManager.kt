package com.example.uslstudentattendancescanner

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class DatabaseManager(context: Context) {

    private val dbHelper: DBHelper = DBHelper(context)
    private var database: SQLiteDatabase? = null

    // Open the database
    @Throws(SQLException::class)
    fun open() {
        database = dbHelper.writableDatabase
    }

    // Close the database
    fun close() {
        dbHelper.close()
    }

    // Insert data into the table
    fun insertData(
        studentId: String,
        studentName: String,
        studentProgram: String,
        studentYear: String,
        date: String,
        time: String
    ) {
        val contentValues = ContentValues()
        contentValues.put(DBHelper.STUDENT_ID, studentId)
        contentValues.put(DBHelper.STUDENT_NAME, studentName)
        contentValues.put(DBHelper.STUDENT_PROGRAM, studentProgram)
        contentValues.put(DBHelper.STUDENT_YEAR, studentYear)
        contentValues.put(DBHelper.DATE, date)
        contentValues.put(DBHelper.TIME, time)
        database?.insert(DBHelper.ATTENDANCE_TABLE, null, contentValues)
    }

    // Retrieve data from the table
    fun getData(): List<StudentData> {
        val columns = arrayOf(
            DBHelper.STUDENT_ID,
            DBHelper.STUDENT_NAME,
            DBHelper.STUDENT_PROGRAM,
            DBHelper.STUDENT_YEAR,
            DBHelper.DATE,
            DBHelper.TIME
        )
        val cursor = database?.query(
            DBHelper.ATTENDANCE_TABLE,
            columns,
            null,
            null,
            null,
            null,
            null
        )
        val dataList = mutableListOf<StudentData>()
        cursor?.use { c ->
            while (c.moveToNext()) {
                val studentId = c.getString(c.getColumnIndexOrThrow(DBHelper.STUDENT_ID))
                val studentName = c.getString(c.getColumnIndexOrThrow(DBHelper.STUDENT_NAME))
                val studentCourse = c.getString(c.getColumnIndexOrThrow(DBHelper.STUDENT_PROGRAM))
                val studentYear = c.getString(c.getColumnIndexOrThrow(DBHelper.STUDENT_YEAR))
                val date = c.getString(c.getColumnIndexOrThrow(DBHelper.DATE))
                val time = c.getString(c.getColumnIndexOrThrow(DBHelper.TIME))

                val studentData = StudentData(
                    studentId,
                    studentName,
                    studentCourse,
                    studentYear,
                    date,
                    time
                )

                dataList.add(studentData)
            }
        }
        cursor?.close()
        return dataList
    }


    // Update data in the table
    fun updateData(id: Long, newName: String) {
        val contentValues = ContentValues()
        contentValues.put(DBHelper.STUDENT_NAME, newName)
        database?.update(
            DBHelper.ATTENDANCE_TABLE,
            contentValues,
            "${DBHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // Delete data from the table
    fun deleteData(id: Long) {
        database?.delete(
            DBHelper.ATTENDANCE_TABLE,
            "${DBHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }
}

data class StudentData(
    val studentId: String,
    val studentName: String,
    val studentCourse: String,
    val studentYear: String,
    val date: String,
    val time: String
)

