package com.example.perpus.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.perpus.MyApplication
import com.example.perpus.R
import com.example.perpus.adapters.AdapterComment
import com.example.perpus.databinding.ActivityPdfDetailBinding
import com.example.perpus.databinding.DialogCommentAddBinding
import com.example.perpus.models.ModelComment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PdfDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfDetailBinding

    private lateinit var progressDialog: ProgressDialog

    val firebaseAuth = FirebaseAuth.getInstance()

    private  lateinit var commentArrayList: ArrayList<ModelComment>

    private  lateinit var  adapterComment: AdapterComment
    //book id
    private var bookId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get book id from intent
        bookId = intent.getStringExtra("bookId")!!

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...") // Atur pesan yang sesuai
        progressDialog.setCancelable(false) // Sesuaikan sesuai kebutuhan Anda

        //increment book view count
        MyApplication.incrementBookViewCount(bookId)

        loadBookDetails()
        showComments()

        //handle backbutton
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.readBookBtn.setOnClickListener {
            val intent = Intent(this, PdfViewActivity::class.java)
            intent.putExtra("bookId", bookId);
            startActivity(intent)
        }

        binding.addCommentBtn.setOnClickListener {
            if (firebaseAuth.currentUser == null){
                Toast.makeText( this,"Kamu Belum Log In brader", Toast.LENGTH_SHORT).show()
            }
            else{
                addCommentDialog()
            }
        }
    }

    private fun showComments() {
        commentArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId).child("Comments")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    commentArrayList.clear()
                    for (ds in snapshot.children){
                        val model = ds.getValue(ModelComment::class.java)
                        commentArrayList.add(model!!)
                    }
                    adapterComment = AdapterComment(this@PdfDetailActivity, commentArrayList)
                    binding.commentsRv.adapter = adapterComment
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private var comment = ""
    private fun addCommentDialog() {
        val commentAddBinding = DialogCommentAddBinding.inflate(LayoutInflater.from(this))

        val builder = AlertDialog.Builder(this, R.style.CostumDialog)
        builder.setView(commentAddBinding.root)

        val alertDialog = builder.create()
        alertDialog.show()

        commentAddBinding.backBtn.setOnClickListener { alertDialog.dismiss() }

        commentAddBinding.submitBtn.setOnClickListener {
            comment = commentAddBinding.commentEt.text.toString().trim()
            if (comment.isEmpty()){
                Toast.makeText( this,"Komennya mana?", Toast.LENGTH_SHORT).show()
            }
            else{
                alertDialog.dismiss()
                addComment()
            }
        }
    }

    private fun addComment() {
        progressDialog.setMessage("Nambahin Komentar Kamu")
        progressDialog.show()

        val timestamp = "${System.currentTimeMillis()}"

        val hashMap = HashMap <String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["bookId"] = "$bookId"
        hashMap["timestamp"] = "$timestamp"
        hashMap["comment"] = "$comment"
        hashMap["uid"] = "${firebaseAuth.uid}"

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId).child("Comments").child(timestamp)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText( this,"Nambahin Kommen Kamu", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText( this,"Gagal Nambahin Kommen kamu ke ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadBookDetails() {
        //books > bookid > details
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get data
                    val categoryId = "${snapshot.child("categoryId").value}"
                    val description = "${snapshot.child("description").value}"
                    val downloadsCount = "${snapshot.child("downloadsCount").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    val title = "${snapshot.child("title").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val url = "${snapshot.child("url").value}"
                    val viewsCount = "${snapshot.child("viewsCount").value}"

                    //format date
                    val date = MyApplication.formatTimeStamp(timestamp.toLong())

                    MyApplication.loadCategory(categoryId, binding.categoryTv)

                    MyApplication.loadPdfFromUrlSinglePage("$url", "$title", binding.pdfView, binding.progressBar,binding.pagesTv)

                    MyApplication.loadPdfSize("$url", "$title", binding.sizeTv)

                    binding.tittleTv.text = title
                    binding.descriptionTv.text = description
                    binding.viewsTv.text = viewsCount
                    binding.dateTv.text = date


                }

                override fun onCancelled(error: DatabaseError) {

                }
            } )
    }

}