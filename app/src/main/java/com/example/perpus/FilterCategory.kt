package com.example.perpus

import android.widget.Filter

class FilterCategory: Filter {

    //arraylist
    private var filterList: ArrayList<ModelCategory>

    //adapter
    private var adapterCategory: AdapterCategory

    //constructor
    constructor(filterlist: ArrayList<ModelCategory>, adapterCategory: AdapterCategory) : super() {
        this.filterList = filterlist
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()){

            //chane to uppercase or lowercase to avoid case sensitive
            constraint = constraint.toString().uppercase()

            val filteredModel:ArrayList<ModelCategory> = ArrayList()
            for (i in 0 until filterList.size){
                //validate
                if (filterList[i].category.uppercase().contains(constraint)){
                    //add to filtered list
                    filteredModel.add(filterList[i])
                }
            }
            results.count = filteredModel.size
            results.values = filteredModel
        }
        else{
            //search value is either null or empty
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filter changes
        adapterCategory.categoryArrayList = results.values as ArrayList<ModelCategory>
    }
}