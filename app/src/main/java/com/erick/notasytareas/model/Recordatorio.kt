package com.erick.notasytareas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recordatorio (
    @PrimaryKey val id: Int,
    var idNota: Int,
    var fecha: Long
){
    constructor(idNota: Int, fecha: Long) :
            this(0,
                idNota,
                fecha)
}