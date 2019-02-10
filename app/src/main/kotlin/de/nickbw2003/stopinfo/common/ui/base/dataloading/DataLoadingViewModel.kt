package de.nickbw2003.stopinfo.common.ui.base.dataloading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.nickbw2003.stopinfo.common.data.WebException
import de.nickbw2003.stopinfo.common.data.models.Error
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.util.SingleLiveEvent
import kotlinx.coroutines.*

@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
abstract class DataLoadingViewModel<T> : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    protected val _isLoading = MutableLiveData<Boolean>()
    protected val _error = SingleLiveEvent<Error>()
    protected val _info = SingleLiveEvent<Info>()
    protected val _data = MutableLiveData<T>()
    protected val _noDataMessageVisible = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val error: LiveData<Error>
        get() = _error

    val info: LiveData<Info>
        get() = _info

    val data: LiveData<T>
        get() = _data

    val noDataMessageVisible: LiveData<Boolean>
        get() = _noDataMessageVisible

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    protected abstract fun hasData(data: T): Boolean

    protected fun launchDataLoad(block: suspend () -> T): Job {
        return uiScope.launch {
            try {
                _isLoading.value = true

                withContext(Dispatchers.IO) {
                    val hasData = hasData(block())
                    _noDataMessageVisible.postValue(!hasData)
                }
            } catch (webException: WebException) {
                _error.setValue(webException.error)
                _noDataMessageVisible.value = _data.value?.let { !hasData(it) } ?: true
            } finally {
                _isLoading.value = false
            }
        }
    }
}