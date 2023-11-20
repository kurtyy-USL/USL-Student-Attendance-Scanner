package com.example.uslstudentattendancescanner

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CreationHelper
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    private val requestCodeCameraPermission = 1001
    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    val databaseManager = DatabaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scanButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity, android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askForCameraPermission()
            } else {
                val intent = Intent(this, Scanner::class.java)
                startActivity(intent)
            }
        }

        exportExcelReportButton.setOnClickListener {
            // Check if the permission is not granted
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the permission
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            } else {
                // Permission has already been granted, proceed with your code
                databaseManager.open()
                println(databaseManager.getData())
                var currentDateTime = getCurrentDate() + " " + getCurrentTime()
                val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val fileName = "student_data$currentDateTime.xlsx"
                val outputPath = File(directory, fileName).absolutePath
                if (outputPath != null) {
                    exportToExcel(databaseManager.getData(), outputPath)
                }
                Toast.makeText(
                    this@MainActivity,
                    "Excel file exported successfully to: $outputPath",
                    Toast.LENGTH_LONG
                ).show()
                databaseManager.close()
            }
        }
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCodeCameraPermission -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with your camera-related functionality
                    // Call the function or code that should run after obtaining camera permission
                    val intent = Intent(this, Scanner::class.java)
                    startActivity(intent)
                } else {
                    // Permission denied, you may want to inform the user or handle it accordingly
                    Toast.makeText(
                        this@MainActivity,
                        "Camera permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Handle other permissions if needed
            // ...
        }
    }

    fun exportToExcel(studentDataList: List<StudentData>, outputPath: String) {
        val workbook = WorkbookFactory.create(true)
        val creationHelper = workbook.creationHelper
        val sheet = workbook.createSheet("Student Data")
        val headerRow = sheet.createRow(0)
        val headers = arrayOf("Student ID", "Student Name", "Student Course", "Student Year", "Date", "Time")
        for ((index, header) in headers.withIndex()) {
            val cell = headerRow.createCell(index)
            cell.setCellValue(header)
        }
        for ((rowIndex, studentData) in studentDataList.withIndex()) {
            val row = sheet.createRow(rowIndex + 1)
            row.createCell(0).setCellValue(studentData.studentId.toDouble())
            row.createCell(1).setCellValue(studentData.studentName)
            row.createCell(2).setCellValue(studentData.studentCourse)
            row.createCell(3).setCellValue(studentData.studentYear.toDouble())
            // Format date and time
            val dateFormat = creationHelper.createDataFormat().getFormat("yyyy-MM-dd")
            val timeFormat = creationHelper.createDataFormat().getFormat("HH:mm:ss")

            val dateCell = row.createCell(4)
            dateCell.setCellValue(creationHelper.createRichTextString(studentData.date))
            dateCell.cellStyle.dataFormat = dateFormat
            val timeCell = row.createCell(5)
            timeCell.setCellValue(creationHelper.createRichTextString(studentData.time))
            timeCell.cellStyle.dataFormat = timeFormat
        }
        val fileOut = FileOutputStream(outputPath)
        workbook.write(fileOut)
        fileOut.close()
        workbook.close()
    }

    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(dateFormatter)
    }

    fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return currentTime.format(timeFormatter)
    }

    fun onRequestStoragePermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // Check if the permission is granted
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with your code
                    // ...
                } else {
                    // Permission denied, handle accordingly
                    // ...
                }
            }
        }
    }


}
