package com.erick.notasytareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.erick.notasytareas.data.NoteDatabase
import com.erick.notasytareas.databinding.FragmentCreateNoteBinding
import com.erick.notasytareas.model.Note
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CreateNote : Fragment() {

    private var idNote: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializamos el bindig e inflamos el fragmento
        val binding = DataBindingUtil.inflate<FragmentCreateNoteBinding>(inflater,
            R.layout.fragment_create_note,container,false)
        val bundle = Bundle()
        //Control para insertar notas o tareas / comportamiento logico de la interfaz
        val toggle: ToggleButton = binding.toggleButton
        val toggleCompletada: ToggleButton= binding.toggleCompleted
        //  Listener
        var typeNote = arguments?.getString("type").toBoolean()
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                typeNote=true
                toggleCompletada.visibility=View.VISIBLE
            }else{
                typeNote=false
                toggleCompletada.visibility=View.INVISIBLE
            }
        }
        //   Se ejecuta al cargar el fragment
        if (typeNote){
            toggle.isChecked = true
            toggleCompletada.visibility=View.VISIBLE
        }else{
            toggle.isChecked = false
            toggleCompletada.visibility=View.INVISIBLE
        }

        //Marcar completa o incompleta una tarea
        //  Listener
        var completada = arguments?.getString("completed").toBoolean()
        toggleCompletada.setOnCheckedChangeListener{ _, isChecked ->
            completada = isChecked
        }
        //  Se ejecuta al cargar el fragment
        toggleCompletada.isChecked = completada



        //Funcion que permite obtener la fecha actual
        fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(this)
        }
        fun getCurrentDateTime(): Date { return Calendar.getInstance().time }


        if(arguments?.getString("title") != null) {
            binding.title.setText(arguments?.getString("title"))
            binding.description.setText(arguments?.getString("description"))
            idNote = arguments?.getString("id")!!.toInt()
        }
        if(idNote!=-1){
            toggle.visibility = View.INVISIBLE
            bundle.putString("idNota",idNote.toString())
        }

        //Listeners para imgs y botones
        binding.save.setOnClickListener {

            bundle.putString("title", binding.title.text.toString())
            bundle.putString("description", binding.description.text.toString())
            bundle.putString("idNota",id.toString())
            parentFragmentManager.setFragmentResult("key",bundle)

            //Insert
            lifecycleScope.launch{
                if (idNote == -1){
                    if(typeNote){
                        //Insert new task
                        val newNote = Note( binding.title.text.toString(),
                            binding.description.text.toString(),
                            typeNote,
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"),
                            completada)
                        NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().insert(newNote)

                    }else{
                        //Insert new note
                        val newNote = Note( binding.title.text.toString(),
                            binding.description.text.toString(),
                            typeNote,
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"),
                            completada)
                        NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().insert(newNote)
                    }

                } else {

                    if(typeNote) {
                        //update task
                        NoteDatabase.getDatabase(requireActivity().applicationContext)
                            .noteDao().updateTask(
                                binding.title.text.toString(),
                                binding.description.text.toString(),
                                getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"),
                                completada,
                                idNote
                            )
                    }else{
                        //update note
                        NoteDatabase.getDatabase(requireActivity().applicationContext)
                            .noteDao().updateNote(
                                binding.title.text.toString(),
                                binding.description.text.toString(),
                                getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"),
                                idNote
                            )
                    }
                }

            }
            //Preguntamos por la variable y navega a notas o tareas
            //Navigation
            it.findNavController().navigate(R.id.action_createNote_to_listNote)
        }

        binding.btnCancel.setOnClickListener {
            //Navigation
            it.findNavController().navigate(R.id.action_createNote_to_listNote)
        }

        binding.floatingActionButton.setOnClickListener{
            if(arguments?.getString("id") != null){
                it.findNavController().navigate(R.id.action_createNote_to_listFotos,bundle)
            }else{
                var idAutoInsert=0
                lifecycleScope.launch{
                    if(typeNote){
                        //Insert new task
                        val newNote = Note( binding.title.text.toString(),
                            binding.description.text.toString(),
                            typeNote,
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"),
                            completada)
                        idAutoInsert= NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().insert(newNote).toInt()

                    }else{
                        //Insert new note
                        val newNote = Note( binding.title.text.toString(),
                            binding.description.text.toString(),
                            typeNote,
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"),
                            completada)
                        idAutoInsert= NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().insert(newNote).toInt()
                    }

                    bundle.putString("idNota",idAutoInsert.toString())
                    it.findNavController().navigate(R.id.action_createNote_to_listFotos,bundle)
                }

            }
        }

        binding.btnReminders.setOnClickListener{
            if(arguments?.getString("id") != null){

                bundle.putString("tittle",binding.title.text.toString())
                it.findNavController().navigate(R.id.action_createNote_to_recordatorios,bundle)
            }else{
                var idAutoInsert=0
                lifecycleScope.launch{
                    if(typeNote){
                        //Insert new task
                        val newNote = Note( binding.title.text.toString(),
                            binding.description.text.toString(),
                            typeNote,
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"),
                            completada)
                        idAutoInsert= NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().insert(newNote).toInt()

                    }else{
                        //Insert new note
                        val newNote = Note( binding.title.text.toString(),
                            binding.description.text.toString(),
                            typeNote,
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"),
                            completada)
                        idAutoInsert= NoteDatabase.getDatabase(requireActivity().applicationContext).noteDao().insert(newNote).toInt()
                    }

                    bundle.putString("tittle",binding.title.text.toString())
                    it.findNavController().navigate(R.id.action_createNote_to_recordatorios,bundle)
                }

            }
        }


        return binding.root
    }
}