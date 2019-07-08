package name.antonsmirnov.notes.app.android.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import name.antonsmirnov.notes.domain.Note
import name.antonsmirnov.notes.app.android.R

class ListNotesAdapter(val notes: MutableList<Note>)
    : RecyclerView.Adapter<ListNotesAdapter.ViewHolder>() {

    class ViewHolder(
        rootView: View,
        val title: TextView,
        val body: TextView
    ) : RecyclerView.ViewHolder(rootView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.listnotes_item, parent, false)
        val titleView = rootView.findViewById(R.id.listnotes_title) as TextView
        val bodyView = rootView.findViewById(R.id.listnotes_body) as TextView
        return ViewHolder(rootView, titleView, bodyView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = notes[position].title
        holder.body.text = notes[position].body
    }

    override fun getItemCount() = notes.size
}