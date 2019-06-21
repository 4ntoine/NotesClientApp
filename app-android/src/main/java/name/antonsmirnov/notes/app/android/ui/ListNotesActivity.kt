package name.antonsmirnov.notes.app.android.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import name.antonsmirnov.notes.app.android.R
import name.antonsmirnov.notes.app.android.adapter.ListNotesAdapter
import name.antonsmirnov.notes.app.android.controller.rest.ListNotesController
import name.antonsmirnov.notes.app.android.controller.rest.RestApi
import name.antonsmirnov.notes.presenter.listnotes.Model
import name.antonsmirnov.notes.presenter.listnotes.Presenter
import name.antonsmirnov.notes.presenter.listnotes.PresenterImpl
import name.antonsmirnov.notes.presenter.listnotes.View
import name.antonsmirnov.notes.presenter.thread.BackgroundThreadManager
import android.view.View as AndroidView

class ListNotesActivity : AppCompatActivity(), View {

    private lateinit var viewAdapter: ListNotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var list: RecyclerView
    private lateinit var indicator: ProgressBar

    override var presenter: Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listnotes)
        bindControls()
        initMvp()
    }

    override fun onDestroy() {
        presenter?.onViewDetached()
        super.onDestroy()
    }

    private fun initMvp() {
        if (lastCustomNonConfigurationInstance != null) {
            presenter = lastCustomNonConfigurationInstance as Presenter
        } else {
            val model = Model(ListNotesController(RestApi.instance))
            presenter = PresenterImpl(model, BackgroundThreadManager())
        }
        presenter?.attachView(this)
    }

    private fun bindControls() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = ListNotesAdapter(arrayListOf())
        list = findViewById<RecyclerView>(R.id.activity_listnotes_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        indicator = findViewById(R.id.activity_listnotes_indicator)
    }

    override fun onStart() {
        super.onStart()
        presenter?.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_listnotes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.activity_listnotes_load -> requestLoad()
            R.id.activity_listnotes_add -> startAddNote()
        }
        return true
    }

    companion object {
        private const val ACTION_ADD_NOTE = 1
    }

    private fun requestLoad() {
        presenter?.onLoadRequest()
    }

    private fun startAddNote() {
        startActivityForResult(Intent(this@ListNotesActivity, AddNoteActivity::class.java), ACTION_ADD_NOTE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // if a new note was added we need to reload the list
        if (requestCode == ACTION_ADD_NOTE && resultCode == RESULT_OK) {
            requestLoad()
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return presenter!!
    }

    private fun setControlVisibility(view: AndroidView, visible: Boolean) {
        view.visibility = if (visible) AndroidView.VISIBLE else AndroidView.INVISIBLE
    }

    private fun setIndicatorVisibility(visible: Boolean) {
        setControlVisibility(indicator, visible)
    }

    private fun setListVisibility(visible: Boolean) {
        setControlVisibility(list, visible)
    }

    override fun updateView(model: Model) = runOnUiThread {
        when (val state = model.state) {
            is Model.State.Initial -> {
                setIndicatorVisibility(false)
                setListVisibility(false)
            }

            is Model.State.Loading -> {
                setIndicatorVisibility(true)
                setListVisibility(false)
            }

            is Model.State.Loaded -> viewAdapter.apply {
                setIndicatorVisibility(false)
                setListVisibility(true)

                notes.clear()
                notes.addAll((model.state as Model.State.Loaded).notes)
                notifyDataSetChanged()
            }

            is Model.State.LoadError -> viewAdapter.apply {
                setIndicatorVisibility(false)
                setListVisibility(false)

                notes.clear()
                notifyDataSetChanged()

                showError(state.error)
            }
        }
    }

    private fun showError(error: Exception) {
        val errorMessage = getString(R.string.activity_listnotes_load_error, error.message)
        Toast
            .makeText(this@ListNotesActivity, errorMessage, Toast.LENGTH_LONG)
            .show()
    }
}
