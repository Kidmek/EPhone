package com.example.ephone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ephone.R
import com.example.ephone.adapter.RecentAdapter
import com.example.ephone.model.Bank
import com.example.ephone.model.Type
import com.example.ephone.viewModel.SharedViewModel


class HomeFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel

    private lateinit var debitedTv: AppCompatTextView
    private lateinit var creditedTv:AppCompatTextView
    private lateinit var debitedLL: LinearLayout
    private lateinit var creditedLL:LinearLayout

    private lateinit var allTv:AppCompatTextView
    private lateinit var spinner:Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.fragment_home, container, false)

        val customAdapter = RecentAdapter(viewModel.messages.value ?: emptyList())

        val recyclerView: RecyclerView = root.findViewById(R.id.home_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = customAdapter
        allTv =root.findViewById(R.id.txtAll)
        debitedTv =root.findViewById(R.id.txtDebited)
        creditedTv =root.findViewById(R.id.txtCredited)

        debitedLL =root.findViewById(R.id.debited)
        creditedLL =root.findViewById(R.id.credited)


        viewModel.messages.observe(viewLifecycleOwner){
            customAdapter.updateMessages(it)
        }

        spinner= root.findViewById(R.id.spinner)

        // Create an ArrayAdapter using a simple spinner layout and an array
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.dropdown_items, // This is a string array resource, defined in res/values/strings.xml
            android.R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_drop_down_item)
        // Apply the adapter to the spinner
        spinner.adapter = adapter

        // Set an item selected listener if needed
        // Drop down filter for different banks
        // banks strings found in res/values/strings.xml should be the same as the
        // sms originating address and the full name can be found in Message data class with colors
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                if(item == "All"){
                    customAdapter.updateMessages( viewModel.messages.value ?: emptyList())
                }else {
                    val newMessages = viewModel.messages.value?.filter {
                        it.bank_name === Bank.valueOf(item).fullName
                    }
                    customAdapter.updateMessages( newMessages ?: emptyList())

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        // tabs to filter by transaction type if its debited or credited or all
        allTv.setOnClickListener{ view: View ->
            if(checkIfActive(view)){
                changeActive(allTv,null)
                if(viewModel.messages.value != null){
                    customAdapter.updateMessages(viewModel.messages.value!!)
                }
            }
        }
        creditedLL.setOnClickListener{ view: View ->
            if(checkIfActive(view)){
                changeActive(creditedTv,creditedLL)
                val newMessages = viewModel.messages.value?.filter {
                    it.debit_or_credit === Type.Credited
                }
                customAdapter.updateMessages(newMessages ?: emptyList())
            }
        }
        debitedLL.setOnClickListener{ view: View ->
            if(checkIfActive(view)){
                changeActive(debitedTv,debitedLL)
                val newMessages = viewModel.messages.value?.filter {
                    it.debit_or_credit === Type.Debited
                }
                customAdapter.updateMessages(newMessages ?: emptyList())
            }
        }

        return root
    }


    private fun checkIfActive(view: View):Boolean {
        return view.background != ResourcesCompat.getDrawable(
            resources, R.drawable.primary_rounded_box_12dp, null
        )
    }

    private fun changeActive(textView: AppCompatTextView, linearLayout: LinearLayout?){
        if(linearLayout === null){
            textView.setBackgroundResource(R.drawable.primary_rounded_box_12dp)
        }else{
            linearLayout.setBackgroundResource(R.drawable.primary_rounded_box_12dp)
        }
        textView.setTextColor(
            ResourcesCompat.getColor(resources,R.color.white,null))
        listOf(debitedTv,creditedTv,allTv).forEach {
            if(it!= textView){
                if(it === allTv){
                    it.setBackgroundResource(R.drawable.grey_rounded_box_12dp)
                }
                it.setTextColor(
                    ResourcesCompat.getColor(resources,R.color.black,null))
            }
        }
        listOf(debitedLL,creditedLL).forEach {
            if(it!= linearLayout){
                it.setBackgroundResource(R.drawable.grey_rounded_box_12dp)
            }
        }
    }
}
