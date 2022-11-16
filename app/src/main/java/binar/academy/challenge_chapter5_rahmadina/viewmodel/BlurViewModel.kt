package binar.academy.challenge_chapter5_rahmadina.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import binar.academy.challenge_chapter5_rahmadina.R
import binar.academy.challenge_chapter5_rahmadina.workers.BlurWorker
import binar.academy.challenge_chapter5_rahmadina.workers.KEY_IMAGE_URI

class BlurViewModel(application: Application) : ViewModel() {

    private val workManager = WorkManager.getInstance(application)
    private var imageUri: Uri? = null

    fun setImgUri(uri: Uri){
        imageUri = uri
    }

    internal fun applyBlur() {
        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
            .setInputData(createInputDataForUri())
            .build()
        workManager.enqueue(blurRequest)
    }

    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    @Suppress("unused")
    private fun getImageUri(context: Context): Uri {
        val resources = context.resources

        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourceTypeName(R.drawable.dummy_profile))
            .appendPath(resources.getResourceTypeName(R.drawable.dummy_profile))
            .appendPath(resources.getResourceEntryName(R.drawable.dummy_profile))
            .build()
    }

    class BlurViewModelFactory(private val application: Application) :
        ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(BlurViewModel::class.java)) {
                BlurViewModel(application) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}