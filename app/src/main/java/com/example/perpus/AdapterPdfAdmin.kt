package com.example.perpus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.perpus.databinding.RowPdfAdminBinding

class AdapterPdfAdmin : RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin> {

    private var context: Context

    private  var pdfArrayList: Array<ModelPdf>

    private lateinit var binding:RowPdfAdminBinding

    constructor(context: Context, pdfArrayList: Array<ModelPdf>) : super() {
        this.context = context
        this.pdfArrayList = pdfArrayList
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfAdmin {
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderPdfAdmin, position: Int) {
        val model = pdfArrayList[position]
        val pdfId = model.id
        val category = model.categoryId
        val title = model.title
        val descrption = model.description
        val pdfUrl = model.url
        val timestamp = model.timestamp


    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }
    inner class HolderPdfAdmin(itemView: View) : RecyclerView.ViewHolder(itemView){
        //ui views row_pdf_admin.xml
        val pdfView = binding.pdfView
        val progressBar = binding.progressBar
        val titleTv = binding.tittleTv
        val descriptionTv = binding.descriptionTv
        val category = binding.categoryTv
        val sizeTv = binding.sizeTv
        val dateTv = binding.dateTv
        val moreBtn = binding.moreBtn
    }
}