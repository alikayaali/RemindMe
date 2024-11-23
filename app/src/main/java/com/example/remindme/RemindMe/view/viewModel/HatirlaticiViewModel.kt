package com.example.remindme.RemindMe.view.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindme.RemindMe.view.Model.HatirlaticiModel
import com.example.remindme.RemindMe.view.db.HatirlaticiDao
import com.example.remindme.RemindMe.view.db.HatirlaticiDatabase
import kotlinx.coroutines.launch

class HatirlaticiViewModel(application: Application) : AndroidViewModel(application) {
    private val _hatirlaticilar = MutableLiveData<List<HatirlaticiModel>>()
    val hatirlaticalar: LiveData<List<HatirlaticiModel>> get() = _hatirlaticilar

    private val hatirlaticiDao: HatirlaticiDao = HatirlaticiDatabase.getDatabase(application).hatirlaticiDao()

    init {
        // Başlangıçta verileri alabiliriz
        verileriAl()
    }

    // Veritabanına veri ekleme
    fun roomaKaydet(hatirlat: HatirlaticiModel) {
        viewModelScope.launch {
            hatirlaticiDao.insert(hatirlat) // Veriyi kaydet
            _hatirlaticilar.value = hatirlaticiDao.getAllHatirlat() // Veritabanından güncel veriyi al ve UI'yi güncelle
        }
    }

    // Veritabanından tüm hatırlatıcıları alma
    private fun verileriAl() {
        viewModelScope.launch {
            _hatirlaticilar.value = hatirlaticiDao.getAllHatirlat()
        }
    }

    // Veritabanından hatırlatıcı silme
     fun hatirlaticiSil(hatirlat: HatirlaticiModel) {
        viewModelScope.launch {
            hatirlaticiDao.delete(hatirlat)
            verileriAl()  // Silme işleminden sonra verileri yeniden alıyoruz
        }
    }

    // Veritabanındaki hatırlatıcıyı güncelleme
     fun hatilaticiGuncelle(hatirlat: HatirlaticiModel) {
        viewModelScope.launch {
            hatirlaticiDao.update(hatirlat)
            verileriAl()  // Güncelleme işleminden sonra verileri yeniden alıyoruz
        }
    }

    // Belirli bir ID'ye göre hatırlatıcıyı alma
   suspend fun getHatirlaticiById(id: Int): HatirlaticiModel? {  // Nullable tür
    return hatirlaticiDao.getHatilarById(id)

   }

}
