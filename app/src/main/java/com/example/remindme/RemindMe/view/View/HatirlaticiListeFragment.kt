package com.example.remindme.RemindMe.view.View

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.remindme.RemindMe.view.Adapter.HatirlatAdapter
import com.example.remindme.RemindMe.view.Model.HatirlaticiModel
import com.example.remindme.RemindMe.view.db.HatirlaticiDao
import com.example.remindme.RemindMe.view.db.HatirlaticiDatabase
import com.example.remindme.RemindMe.view.viewModel.HatirlaticiViewModel
import com.example.remindme.databinding.FragmentHatirlaticiListeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HatirlaticiListeFragment : Fragment() {

    private var _binding: FragmentHatirlaticiListeBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var viewModel:HatirlaticiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHatirlaticiListeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener { ekle(view) }
        //View Model İnitalize edildi.
        viewModel=ViewModelProvider(this)[HatirlaticiViewModel::class.java]

        val adapter=HatirlatAdapter()
        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        binding.recyclerView.adapter=adapter
        viewModel.hatirlaticalar.observe(viewLifecycleOwner){hatirlatcilar->
            adapter.submitList(hatirlatcilar)
        }
        izinKontrolu()
    }

    private fun ekle(view: View) {
        val action = HatirlaticiListeFragmentDirections.actionHatirlaticiListeFragmentToHatirlaticiEkleFragment("yeni", 0)
        Navigation.findNavController(view).navigate(action)
    }

    private fun izinKontrolu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ için bildirim izni kontrolü
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Daha önce izin istenmiş ama verilmemişse
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.POST_NOTIFICATIONS)) {
                    // Snackbar ile kullanıcıyı bilgilendiriyoruz
                    Snackbar.make(requireView(), "Bildirim Göndermek İçin İzin Gerekli!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("İzin Ver") {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }.show()
                } else {
                    // İzin isteniyor
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                // İzin verilmişse
                Snackbar.make(requireView(), "Bildirim İzni Zaten Verilmiş", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            // Eski Android sürümleri için izin kontrolü
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Daha önce izin istenmiş ama verilmemişse
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.POST_NOTIFICATIONS)) {
                    // Snackbar ile kullanıcıyı bilgilendiriyoruz
                    Snackbar.make(requireView(), "Bildirim Göndermek İçin İzin Gerekli!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("İzin Ver") {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }.show()
                } else {
                    // İzin isteniyor
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                // İzin verilmişse
                Snackbar.make(requireView(), "Bildirim İzni Zaten Verilmiş", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                // İzin verildiğinde
                Toast.makeText(requireContext(), "Bildirim İzni Verildi", Toast.LENGTH_LONG).show()
            } else {
                // İzin verilmediğinde
                Toast.makeText(requireContext(), "Bildirim İzni Verilmedi", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
