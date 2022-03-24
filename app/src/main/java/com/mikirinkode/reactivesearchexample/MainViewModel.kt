package com.mikirinkode.reactivesearchexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mikirinkode.reactivesearchexample.api.ApiConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi  // diperlukan karena fitur BroadcastChannel belum stable
class MainViewModel: ViewModel() {

    // untuk token bisa daftar akun dulu di https://www.mapbox.com/
    private val accessToken = ""

    /*
        BroadcastChannel digunakan untuk membuat channel yang bertujuan untuk
        berkomunikasi antar coroutine, contohnya untuk menerima dan mengirim data

        *CONFLATED, untuk menyimpan nilai terakhir saja
        dan membiarkan nilai yang sebelumnya
    */
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    /*
        .asFlow() -> mengubah channel menjadi flow
        .debounce() -> memastikan agar eksekusi selanjutnya berjalan jika ada jeda 300ms

        debounce mencegah aplikasi melakukan request API setiap user mengetikkan huruf
     */
    val searchResult = queryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            ApiConfig.provideApiService().getCountry(it, accessToken).features
        }
        .asLiveData()
}