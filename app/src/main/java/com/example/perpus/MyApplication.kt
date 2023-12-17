package com.example.perpus

import android.app.Application
import android.icu.text.CaseMap.Title
import java.security.Timestamp
import android.text.format.DateFormat
import android.util.Log
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object{

        fun formatTimeStamp(timestamp: Long) : String{
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timestamp

            return  DateFormat.format("dd/MM/yyyy", cal).toString()
        }

        fun loadPdfSize(pdfUrl: String, pdfTitle: String, sizeTv: TextView){
            val TAG = "PDF_SIZE_TAG"

            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
            ref.metadata
                .addOnSuccessListener {

                }
                .addOnFailureListener{ e->
                    Log.d(TAG, "loadPdfSize: Failed to get metada due to ${e.message}")
                }

        }
    }
}