package com.example.locationalarmproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class ScheduleAdapter (data: OrderedRealmCollection<Schedule>) :
    RealmRecyclerViewAdapter<Schedule, ScheduleAdapter.ViewHolder>(data,true){

    private var listener: ((Long?)-> Unit)? = null
    /**
     * 引数として関数型を受け取る
     */
    fun setOnItemClickListener(listener:(Long?)-> Unit) {
        this.listener = listener
    }

    init{
        setHasStableIds(true)
    }
    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell){
        val date: TextView = cell.findViewById(android.R.id.text1)
        var title: TextView = cell.findViewById(android.R.id.text2)
    }

    /**
     * ViewHolderのインスタンスを生成して返す
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_2,
            parent, false)
        return ViewHolder(view)
    }

    /**
     * ViewHolderで保持しているビューに対して実際に表示するコンテンツの設定
     */
    override fun onBindViewHolder(
        holder: ScheduleAdapter.ViewHolder,
        position: Int) {
        val schedule: Schedule? = getItem(position)
        holder.title.text = schedule?.title
        holder.itemView.setOnClickListener{
            listener?.invoke(schedule?.id)
        }
    }

    /**
     * getItemIdをオーバーライド
     */
    override fun getItemId(position: Int): Long {
        return super.getItem(position)?.id ?: 0
    }



}