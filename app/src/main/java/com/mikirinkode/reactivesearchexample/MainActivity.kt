package com.mikirinkode.reactivesearchexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mikirinkode.reactivesearchexample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edtPlace.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /*
                    lifecycleScope -> karena perlu melakukan proses dalam Coroutine
                                      untuk membuat scope coroutine yang aware terhadap lifecycle
                                      jadi saat lifecyle onPause/onStop, maka Coroutine juga terdampak
                                      sehingga bisa menghindari memory leak
                    .launch proses tanpa kembalian
                 */
                lifecycleScope.launch {
                    viewModel.queryChannel.send(s.toString())   // mengirim query
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // mengambil hasil pencarial dari model
        viewModel.searchResult.observe(this, Observer { placesItem ->
            val placesName = arrayListOf<String?>()
            placesItem.map {
                placesName.add(it.placeName)
            }

            // menampilkan data pada dropdown
            val adapter = ArrayAdapter(this@MainActivity, android.R.layout.select_dialog_item, placesName)
            adapter.notifyDataSetChanged()
            binding.edtPlace.setAdapter(adapter)
        })
    }
}