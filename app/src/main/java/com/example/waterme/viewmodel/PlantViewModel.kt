/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.waterme.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.waterme.data.DataSource
import com.example.waterme.worker.WaterReminderWorker
import com.example.waterme.worker.WaterReminderWorker.Companion.nameKey
import java.util.concurrent.TimeUnit

class PlantViewModel(application: Application) : ViewModel() {

    val plants = DataSource.plants
    private val workManager = WorkManager.getInstance(application)

    @RequiresApi(Build.VERSION_CODES.N)
    internal fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
        plantName: String
    ) {
        val data = Data.Builder().putString(nameKey, plantName).build()


        val request =
            OneTimeWorkRequestBuilder<WaterReminderWorker>()
                .setInitialDelay(duration, unit)
                .setInputData(data).build()

        // TODO: Generate a OneTimeWorkRequest with the passed in duration, time unit, and data
        //  instance

        workManager
            .beginWith(request)
            .enqueue()
        // TODO: Enqueue the request as a unique work request
    }
}

class PlantViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PlantViewModel::class.java)) {
            PlantViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
