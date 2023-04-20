package com.raj.sample.repository

import com.raj.sample.util.Resource
import java.io.File

interface MainRepository {

    suspend fun uploadImage(file: File): Resource<Unit>
}