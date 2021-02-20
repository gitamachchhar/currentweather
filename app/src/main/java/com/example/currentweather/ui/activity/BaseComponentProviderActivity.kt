package com.example.currentweather.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.currentweather.di.component.MainAppComponent
import io.reactivex.disposables.CompositeDisposable


abstract class BaseComponentProviderActivity<Component : MainAppComponent> : AppCompatActivity() {

    var mComponent: Component? = null

    var compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mComponent = mComponent ?: getComponent()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    abstract fun getComponent(): Component?


    fun hideKeyboard() {
        val view = this.currentFocus
        view?.let {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}