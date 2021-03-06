package name.antonsmirnov.notes.app.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import name.antonsmirnov.notes.app.android.R
import name.antonsmirnov.notes.app.controller.rest.AddNoteController
import name.antonsmirnov.notes.app.controller.rest.RestApi
import name.antonsmirnov.notes.app.controller.rest.restApiInstance
import name.antonsmirnov.notes.presenter.Note
import name.antonsmirnov.notes.presenter.addnote.AddNoteModel
import name.antonsmirnov.notes.presenter.addnote.AddNotePresenter
import name.antonsmirnov.notes.presenter.addnote.AddNotePresenterImpl
import name.antonsmirnov.notes.presenter.addnote.AddNoteView

class AddNoteActivity : AppCompatActivity(), AddNoteView {

    override var presenter: AddNotePresenter? = null

    private lateinit var editTextTitle: EditText
    private lateinit var editTextBody: EditText
    private lateinit var buttonOk: Button
    private lateinit var indicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnote)
        bindControls()
        initControls()
        initMvp()
    }

    override fun onDestroy() {
        presenter?.onViewDetached()
        super.onDestroy()
    }

    private fun initControls() {
        buttonOk.setOnClickListener { presenter?.onViewChanged() }
        setIndicatorVisibility(false)
    }

    private fun bindControls() {
        editTextTitle = findViewById(R.id.activity_addnote_title)
        editTextBody = findViewById(R.id.activity_addnote_body)
        buttonOk = findViewById(R.id.activity_addnote_ok)
        indicator = findViewById(R.id.activity_addnote_indicator)
    }

    private fun initMvp() {
        if (lastCustomNonConfigurationInstance != null) {
            presenter = lastCustomNonConfigurationInstance as AddNotePresenter
        } else {
            val model = AddNoteModel(AddNoteController(restApiInstance), Note("", null))
            presenter = AddNotePresenterImpl(model)
        }
        presenter?.attachView(this)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return presenter!!
    }

    private fun setControlVisibility(view: android.view.View, visible: Boolean) {
        view.visibility = if (visible) android.view.View.VISIBLE else android.view.View.INVISIBLE
    }

    private fun setIndicatorVisibility(visible: Boolean) {
        setControlVisibility(indicator, visible)
    }

    override fun updateView(_model: AddNoteModel) {
        val model = _model.stateCopy()
        runOnUiThread {
            when (model.state) {
                is AddNoteModel.State.Executing -> setIndicatorVisibility(true)
                is AddNoteModel.State.ExecutionError -> {
                    setIndicatorVisibility(false)
                    showError((model.state as AddNoteModel.State.ExecutionError).error)
                }
                else -> {
                    setIndicatorVisibility(false)
                    editTextTitle.setText(model.note.title)
                    editTextBody.setText(model.note.body ?: "")
                }
            }
        }
    }

    private fun showError(error: Exception) {
        val errorMessage = getString(R.string.activity_addnote_load_error, error.message)
        Toast
            .makeText(this@AddNoteActivity, errorMessage, Toast.LENGTH_LONG)
            .show()
    }

    override fun updateModel(model: AddNoteModel) {
        model.note.title = editTextTitle.text.toString()
        model.note.body = if (editTextBody.text.isNotEmpty()) editTextBody.text.toString() else null
    }

    override fun showNotesList() = runOnUiThread {
        setResult(RESULT_OK)
        finish()
    }
}
