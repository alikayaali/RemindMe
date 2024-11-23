package com.example.remindme.RemindMe.view.View

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.remindme.R
import com.example.remindme.RemindMe.view.Model.HatirlaticiModel
import com.example.remindme.RemindMe.view.viewModel.HatirlaticiViewModel
import com.example.remindme.databinding.FragmentHatirlaticiEkleBinding
import kotlinx.coroutines.launch
import java.util.Calendar


class HatirlaticiEkleFragment : Fragment() {
    private var _binding: FragmentHatirlaticiEkleBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: HatirlaticiViewModel
   private  var hatirlat:HatirlaticiModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHatirlaticiEkleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(this).get(HatirlaticiViewModel::class.java)
        binding.editTexttarih.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Seçilen tarihi alıyoruz
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }

                // Bugünün tarihi
                val currentCalendar = Calendar.getInstance()

                // Seçilen tarih ile bugünün arasındaki farkı hesaplıyoruz
                val differenceInMillis = selectedCalendar.timeInMillis - currentCalendar.timeInMillis

                // Eğer fark negatifse (yani geçmiş bir tarihse), kullanıcının yanlış bir tarih seçtiği mesajını gösterelim
                if (differenceInMillis < 0) {
                    Toast.makeText(requireContext(), "Lütfen gelecekte bir tarih seçin.", Toast.LENGTH_SHORT).show()
                } else {
                    // Alarmı kurmak için setNotificationForFutureTime fonksiyonunu çağırıyoruz
                    setNotificationForFutureTime(requireContext(), differenceInMillis)
                }

                // Seçilen tarihi ekranda gösteriyoruz
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.editTexttarih.setText(selectedDate)

            }, year, month, day)

            datePicker.show()
        }



        arguments?.let {
            val bilgi=HatirlaticiEkleFragmentArgs.fromBundle(it).bilgi
            if(bilgi=="yeni"){
                binding.buttonSil.isEnabled=false
                binding.buttonGNcelle.isEnabled=false
                binding.buttonKaydet.isEnabled=true
                hatirlat=null
            }
            else{
                binding.buttonSil.isEnabled=true
                binding.buttonGNcelle.isEnabled=true
                binding.buttonKaydet.isEnabled=false
                val id=HatirlaticiEkleFragmentArgs.fromBundle(it).id
                lifecycleScope.launch {
                    val hatirlatici = viewModel.getHatirlaticiById(id)
                    if (hatirlatici != null) {
                        veriGoster(hatirlatici)
                    } else {
                        Toast.makeText(requireContext(), "Veri bulunamadı", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }
        binding.buttonGNcelle.setOnClickListener{
            guncelle(view)
        }

        binding.buttonKaydet.setOnClickListener{
            kaydet(view)
        }
        binding.buttonSil.setOnClickListener{
            sil(view)
        }

    }
    fun setNotificationForFutureTime(context: Context, delayInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Bildirimi tetikleyecek olan Intent
        val intent = Intent(context, NotificationReceiver::class.java)

        // FLAG_IMMUTABLE bayrağını ekliyoruz
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // AlarmManager'a zamanlayıcıyı ayarlıyoruz
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + delayInMillis, // Şu an + seçilen tarih farkı
            pendingIntent
        )
    }




    fun veriGoster(Hatirlatici:HatirlaticiModel){
        binding.editTextTextKonu.setText(Hatirlatici.Konu)
        binding.editTextaciklama.setText(Hatirlatici.Aciklama)
        binding.editTexttarih.setText(Hatirlatici.Tarih)
        hatirlat=Hatirlatici
    }
    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            // API 26 ve sonrasında NotificationChannel gereklidir.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendNotification(context)
            } else {
                // API 24 ve önceki sürümler için kanal gerekmiyor
                sendNotificationLegacy(context)
            }
        }

        // API 26 ve sonrasında NotificationChannel oluşturularak bildirim gönderilir
        @RequiresApi(Build.VERSION_CODES.O)
        private fun sendNotification(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                "reminder_channel",  // Kanal adı
                "Hatırlatıcılar",     // Kanal açıklaması
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            val notificationBuilder = NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)  // Varsayılan simge
                .setContentTitle("Hatırlatıcı!")
                .setContentText("Zamanınız geldi!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            notificationManager.notify(1, notificationBuilder.build())  // 1, bildirim id'si
        }

        // API 24 ve öncesi için NotificationChannel kullanmadan bildirim gönderilir
        private fun sendNotificationLegacy(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)  // Varsayılan simge
                .setContentTitle("Hatırlatıcı!")
                .setContentText("Zamanınız geldi!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            notificationManager.notify(1, notificationBuilder.build())  // 1, bildirim id'si
        }
    }

    fun guncelle(view:View){
        val konu = binding.editTextTextKonu.text.toString()
        val aciklama = binding.editTextaciklama.text.toString()
        val tarih = binding.editTexttarih.text.toString()
        if (konu.isNotEmpty() && aciklama.isNotEmpty() && tarih.isNotEmpty() && hatirlat != null) {
            // Mevcut hatırlatıcıyı güncelleyerek yeni verileri ayarla
            val updatedHatirlatici = hatirlat!!.copy(Konu = konu, Aciklama = aciklama, Tarih = tarih)

            // Güncellenmiş veriyi viewModel aracılığıyla veritabanına kaydet
            viewModel.hatilaticiGuncelle(updatedHatirlatici)

            Toast.makeText(requireContext(), "Hatırlatıcı Güncellendi", Toast.LENGTH_LONG).show()
            Navigation.findNavController(view).popBackStack()  // Geriy dönüş
        }
        else {
            Toast.makeText(requireContext(), "Lütfen Boş Alanları Doldurunuz", Toast.LENGTH_LONG).show()
        }



    }
    fun kaydet(view:View){
        val konu=binding.editTextTextKonu.text.toString()
        val aciklama=binding.editTextaciklama.text.toString()
        val tarih=binding.editTexttarih.text.toString()
        val hatirlatici=HatirlaticiModel(konu,aciklama,tarih)
        if (konu.isNotEmpty() && aciklama.isNotEmpty()&&tarih.isNotEmpty()){
            viewModel.roomaKaydet(hatirlatici)

            Toast.makeText(requireContext(),"Hatırlatıcı Kaydedildi",Toast.LENGTH_LONG).show()
            Navigation.findNavController(view).popBackStack()

        }

        else{
            Toast.makeText(requireContext(),"Lütfen Boş Alanları Doldurunuz",Toast.LENGTH_LONG).show()

        }


    }
    fun sil(view: View){
        val hatirlatici=hatirlat
        if (hatirlatici!=null){
            viewModel.hatirlaticiSil(hatirlatici)
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}