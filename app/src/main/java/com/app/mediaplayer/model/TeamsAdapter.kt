package com.app.mediaplayer.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.mediaplayer.R
import com.app.mediaplayer.data.PlayerModel
import kotlin.collections.ArrayList

class TeamsAdapter : RecyclerView.Adapter<TeamsAdapter.TeamViewHolder>() {

    private var mList: ArrayList<PlayerModel.Player> = ArrayList()

    fun updateAdapter(list: List<PlayerModel.Player>?) {
        this.mList!!.clear()
        this.mList!!.addAll(list!!)
        notifyDataSetChanged()
    }

    class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: TextView? = null

        init {
            message = itemView.findViewById(R.id.tv_player_name) as TextView?
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.player_row, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.message!!.text = mList!![position].name +"   ( "+ mList!![position].role+" )"
    }
}