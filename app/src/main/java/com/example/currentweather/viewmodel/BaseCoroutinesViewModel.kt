package com.example.currentweather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.currentweather.helper.RecyclableCompositeDisposable
import com.example.currentweather.model.Resource
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Response

abstract class BaseCoroutinesViewModel(application: Application) : AndroidViewModel(application) {

    private val recyclableCompositeDisposable by lazy { RecyclableCompositeDisposable() }

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     * Since we pass viewModelJob, you can cancel all coroutines
     * launched by uiScope by calling viewModelJob.cancel()
     */
    val ioScope by lazy { CoroutineScope(IO + coroutineExceptionHandler) }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("on error: ","$throwable")
    }

    private var parentJob: CompletableJob = SupervisorJob()

    private fun <T> parseNetworkResult(response: Response<T>): Resource<T> {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.success(body)
        }
        return Resource.error(null, "")
    }

    private fun handleError(e: Exception): String {
        return try {
            if (e.message != null) e.message!! else ""
        } catch (e: Exception) {
            ""
        }
    }

    fun handler(e: Exception) = CancellationException(e.message)


    private fun getParentJob(): CompletableJob {
        // reset parentJob if canceled
        if (parentJob.isCancelled) {
            parentJob = SupervisorJob()
        }
        return parentJob
    }

    fun launchJob(func: suspend () -> Unit) {
        ioScope.launch(getParentJob()) {
            func()
        }
    }

    fun <T : Any?> launchDatabaseJob(
            databaseQuery: suspend () -> T,
            onResult: suspend (T?) -> Unit) {
        // launch Database function with parentJob
        ioScope.launch(getParentJob()) {
            val result = async { databaseQuery() }
            onResult(result.await())
        }
    }

    fun <T : Any?> launchNetworkJob(networkRequest: suspend () -> Response<T>, onResult: suspend (Resource<T>) -> Unit) {
        // launch Network Request with parentJob
        ioScope.launch(getParentJob()) {
            val result = async { parseNetworkResult(networkRequest()) }
            onResult(result.await())
        }
    }

    fun cancelParentJob() {
        // if parentJob is canceled all jobs launched within the parentJob will be canceled
        parentJob.cancel()
    }

    /**
     * Add the provided disposable to list to be disposed when view model is cleared
     */
    fun disposeOnClear(d: Disposable) {
        recyclableCompositeDisposable.add(d)
    }

    fun Disposable.disposeOnCleared() {
        recyclableCompositeDisposable.add(this)
    }

    override fun onCleared() {
        cancelParentJob()
        recyclableCompositeDisposable.disposeAndReset()
        super.onCleared()
    }

}