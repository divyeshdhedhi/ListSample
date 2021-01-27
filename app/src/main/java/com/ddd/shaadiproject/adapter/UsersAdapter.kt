package com.ddd.shaadiproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ddd.shaadiproject.R
import com.ddd.shaadiproject.data.User
import io.realm.Realm

class UsersAdapter(private val context: Context, private var list: MutableList<User>) : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.user_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = list[position]
        holder.tv_username?.text = user.name
        holder.tv_info1?.text = user?.userName + " | " + user?.email
        holder.tv_info2?.text = user?.phone + " | " + user?.website

        if(user.status != null){
            if(user.status!!){
                holder.tv_status?.text = "You have accepted this user's request"
                holder.tv_status?.visibility = View.VISIBLE
                holder.btn_accept?.visibility = View.GONE
                holder.btn_reject?.visibility = View.GONE
            } else{
                holder.tv_status?.text = "You have rejected this user's request"
                holder.tv_status?.visibility = View.VISIBLE
                holder.btn_accept?.visibility = View.GONE
                holder.btn_reject?.visibility = View.GONE
            }
        } else{
            holder.tv_status?.visibility = View.GONE
            holder.btn_accept?.visibility = View.VISIBLE
            holder.btn_reject?.visibility = View.VISIBLE
        }

        holder.btn_reject?.setOnClickListener {
            changeStatus(holder,false, position, user.id)
        }

        holder.btn_accept?.setOnClickListener {
            changeStatus(holder, true, position, user.id)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_username: TextView? = null
        var tv_info1: TextView? = null
        var tv_info2: TextView? = null
        var tv_address: TextView? = null

        var tv_status: TextView? = null
        var btn_accept: Button? = null
        var btn_reject: Button? = null

        init {
            tv_username = view.findViewById(R.id.tv_username)
            tv_info1 = view.findViewById(R.id.tv_info1)
            tv_info2 = view.findViewById(R.id.tv_info2)
            tv_address = view.findViewById(R.id.tv_address)
            tv_status = view.findViewById(R.id.tv_status)
            btn_reject = view.findViewById(R.id.btn_reject)
            btn_accept = view.findViewById(R.id.btn_accept)
        }
    }

    private fun changeStatus(holder: MyViewHolder, status: Boolean, position: Int, id: Int?) {
        Realm.getDefaultInstance().use { r ->
            r.executeTransaction { realm: Realm ->
                val user: User? =
                    realm.where(
                        User::class.java
                    ).equalTo("id", id).findFirst()
                if (user != null) {
                    user.status = status
                }
            }
        }

        if(status){
            holder.tv_status?.text = "You have accepted this user's request"
            holder.tv_status?.visibility = View.VISIBLE
            holder.btn_accept?.visibility = View.GONE
            holder.btn_reject?.visibility = View.GONE
            notifyItemChanged(position)
        } else{
            holder.tv_status?.text = "You have rejected this user's request"
            holder.tv_status?.visibility = View.VISIBLE
            holder.btn_accept?.visibility = View.GONE
            holder.btn_reject?.visibility = View.GONE
            notifyItemChanged(position)
        }

        Toast.makeText(context, "Status changed", Toast.LENGTH_SHORT).show()
    }
}