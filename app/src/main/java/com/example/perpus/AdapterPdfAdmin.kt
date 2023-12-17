package com.example.perpus

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.perpus.databinding.RowPdfAdminBinding

class AdapterPdfAdmin : RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin>, Filterable {

    private var context: Context

    public var pdfArrayList: ArrayList<ModelPdf>
    private val filterList:ArrayList<ModelPdf>

    private lateinit var binding:RowPdfAdminBinding

    var filter: FilterPdfAdmin? = null

    constructor(context: Context, pdfArrayList: ArrayList<ModelPdf>) : super() {
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterList = pdfArrayList
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfAdmin {
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderPdfAdmin, position: Int) {
        val model = pdfArrayList[position]
        val pdfId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val descrption = model.description
        val pdfUrl = model.url
        val timestamp = model.timestamp

        //convert timestamp to dd/MM/yyyy format
        val formattedDate = MyApplication.formatTimeStamp(timestamp)

        //set data
        holder.titleTv.text = title
        holder.descriptionTv.text = descrption
        holder.dateTv.text = formattedDate

        //category id
        MyApplication.loadCategory(categoryId, holder.categoryTv)

        MyApplication.loadPdfFromUrlSinglePage(pdfUrl, title, holder.pdfView, holder.progressBar, null)

        MyApplication.loadPdfSize(pdfUrl, title, holder.sizeTv)

        holder.moreBtn.setOnClickListener {
            moreOptionsDialog(model, holder)
        }

    }

    private fun moreOptionsDialog(model: ModelPdf, holder: AdapterPdfAdmin.HolderPdfAdmin) {
        val bookId = model.id
        val bookUrl = model.url
        val bookTitle = model.title

        val options = arrayOf("Edit", "Delete")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option")
            .setItems(options){dialog, position->
                if (position==0){
                    val intent = Intent(context, PdfEditActivity::class.java)
                    intent.putExtra("bookId", bookId)
                    context.startActivity(intent)
                }
                else if (position == 1){
                    MyApplication.deleteBook(context, bookId, bookUrl, bookTitle)
                }
            }
            .show()
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }


    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterPdfAdmin(filterList, this)
        }
        return  filter as FilterPdfAdmin
    }

    inner class HolderPdfAdmin(itemView: View) : RecyclerView.ViewHolder(itemView){
        //ui views row_pdf_admin.xml
        val pdfView = binding.pdfView
        val progressBar = binding.progressBar
        val titleTv = binding.tittleTv
        val descriptionTv = binding.descriptionTv
        val categoryTv = binding.categoryTv
        val sizeTv = binding.sizeTv
        val dateTv = binding.dateTv
        val moreBtn = binding.moreBtn
    }
}