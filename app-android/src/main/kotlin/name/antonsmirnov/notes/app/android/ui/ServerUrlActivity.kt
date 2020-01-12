package name.antonsmirnov.notes.app.android.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.widget.Toast
import name.antonsmirnov.notes.app.android.R
import name.antonsmirnov.notes.app.controller.rest.RestApi
import name.antonsmirnov.notes.app.controller.rest.buildRestApiImpl
import name.antonsmirnov.notes.app.controller.rest.restApiInstance
import name.antonsmirnov.notes.presenter.serverurl.ServerUrlModel
import name.antonsmirnov.notes.presenter.serverurl.ServerUrlPresenter
import name.antonsmirnov.notes.presenter.serverurl.ServerUrlPresenterImpl
import name.antonsmirnov.notes.presenter.serverurl.ServerUrlView

class ServerUrlActivity : AppCompatActivity(), ServerUrlView {

    override var presenter: ServerUrlPresenter? = null

    private lateinit var editTextHost: AppCompatEditText
    private lateinit var editTextPort: AppCompatEditText
    private lateinit var buttonOk: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serverurl)
        bindControls()
        initControls()
        initMvp()
    }

    override fun onDestroy() {
        presenter?.onViewDetached()
        super.onDestroy()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return presenter!!
    }

    private fun initControls() {
        buttonOk.setOnClickListener { presenter?.onViewChanged() }
    }

    private fun initMvp() {
        if (lastCustomNonConfigurationInstance != null) {
            presenter = lastCustomNonConfigurationInstance as ServerUrlPresenter
        } else {
            // we want to add app-specific logics: init App-global object that uses updated model data
            // TODO: what's the best way to do it (subclass model/presenter/add listeners/etc)?
            val model = object : ServerUrlModel("localhost", 8080U) {
                override fun update(host: String, port: UInt) {
                    super.update(host, port)
                    initRestApi(host, port)
                }
            }
            presenter = ServerUrlPresenterImpl(model)
        }
        presenter?.attachView(this)
    }

    private fun initRestApi(host: String, port: UInt) {
        restApiInstance = buildRestApiImpl("http", host, port)
    }

    private fun bindControls() {
        editTextHost = findViewById(R.id.activity_serverurl_host)
        editTextPort = findViewById(R.id.activity_serverurl_port)
        buttonOk = findViewById(R.id.activity_serverurl_ok)
    }

    // as a View

    override var host: String
        get() = editTextHost.text.toString()
        set(newValue) { editTextHost.setText(newValue) }

    override var port: String
        get() = editTextPort.text.toString()
        set(newValue) { editTextPort.setText(newValue) }

    override fun showValidationError(error: Exception) = runOnUiThread {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun showNotesList() = runOnUiThread {
        finish()
        startActivity(Intent(this, ListNotesActivity::class.java))
    }
}