package com.erick.notasytareas

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.erick.notasytareas.databinding.FragmentRecordatoriosBinding
import java.util.*

class Recordatorios : Fragment() {
    private lateinit var fecha: EditText
    private lateinit var hora: EditText
    private var idNote: Int = -1

    private var diaAux=0
    private var mesAux=0
    private var yearAux=0
    private var horaAux=0
    private var minuteAux=0
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializamos el bindig e inflamos el fragmento
        val binding = DataBindingUtil.inflate<FragmentRecordatoriosBinding>(inflater,
            R.layout.fragment_recordatorios,container,false)

        fecha= binding.txtDate
        hora=binding.txtHour
        binding.title.setText(arguments?.getString("tittle"))

        val bundle = Bundle()

        //Date and hour
        binding.date.setOnClickListener {
            showDatePickerDialog()
        }

        binding.hour.setOnClickListener {
            showTimePikerDialog()
        }

        binding.saveAlarma.setOnClickListener {

            scheduleNotification(binding.title.text.toString(),binding.description.text.toString())
            Toast.makeText(context, "Alarma guardada con éxito", Toast.LENGTH_SHORT).show()
            it.findNavController().navigate(R.id.action_recordatorios_to_listNote)
        }



        return binding.root
    }
    private fun showTimePikerDialog() {
        val newFragment = TimePicker { hour, minute -> onTimeSelected(hour, minute) }
        activity?.let { newFragment.show(it.supportFragmentManager, "timePicker") }
    }
    @SuppressLint("SetTextI18n")
    private fun onTimeSelected(hour:Int, minute:Int) {
        hora.setText("$hour:$minute")
        this.horaAux = hour
        this.minuteAux = minute
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePicker { day, month, year -> onDateSelected(day, month, year) }
        activity?.let { newFragment.show(it.supportFragmentManager, "datePicker") }
    }

    @SuppressLint("SetTextI18n")
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val aux=month+1
        fecha.setText("$day/$aux/$year")

        this.diaAux = day
        this.mesAux = month
        this.yearAux = year
    }

    //Notification
    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(titulo: String,descripcion: String) {
        getTime(titulo,descripcion)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun startAlarm(calendar: Calendar, titulo: String, descripcion:String) {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Receiver::class.java)
        intent.putExtra(tituloExtra2, titulo)
        intent.putExtra(mensajeExtra2, descripcion)
        //val pendingIntent = PendingIntent.getBroadcast(context, notificationID, intent, 0)

        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, notificationID, intent,PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(context,  notificationID, intent,PendingIntent.FLAG_ONE_SHOT)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getTime(titulo: String,descripcion: String){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,horaAux)
        calendar.set(Calendar.MINUTE,minuteAux)
        calendar.set(Calendar.SECOND,0)
        startAlarm(calendar, titulo,descripcion)
    }
}