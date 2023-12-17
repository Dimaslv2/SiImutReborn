package com.example.perpus

import android.widget.Filter

//used to filter data from recyclerview
class FilterPdfAdmin : Filter {
    var filterList: ArrayList<ModelPdf>
    var adapterPdfAdmin: AdapterPdfAdmin

    constructor(filterList: ArrayList<ModelPdf>, adapterPdfAdmin: AdapterPdfAdmin) {
        this.filterList = filterList
        this.adapterPdfAdmin = adapterPdfAdmin
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint:CharSequence? = constraint
        val results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()){
            //change to upper case
            constraint = constraint.toString().lowercase()
            val filteredModels = ArrayList<ModelPdf>()
            for (i in filterList.indices){
                //validate  if match
                if (filterList[i].title.lowercase().contains(constraint)){
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else{
            results.count = filterList.size
            results.values = filterList
        }

        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterPdfAdmin.pdfArrayList = results.values as ArrayList<ModelPdf>

        adapterPdfAdmin.notifyDataSetChanged()
    }
}