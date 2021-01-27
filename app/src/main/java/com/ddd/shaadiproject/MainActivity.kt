package com.ddd.shaadiproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddd.shaadiproject.adapter.UsersAdapter
import com.ddd.shaadiproject.api.ApiClient
import com.ddd.shaadiproject.data.User
import com.ddd.shaadiproject.util.Utility.hideProgressBar
import com.ddd.shaadiproject.util.Utility.isInternetAvailable
import com.ddd.shaadiproject.util.Utility.showProgressBar
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmQuery
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {
    private var listUsers: MutableList<User> = mutableListOf<User>()
    private var adapter: UsersAdapter? = null

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)

        realm = Realm.getDefaultInstance()

        listUsers = mutableListOf()

        recycler_main.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = UsersAdapter(
                this,
                listUsers
        )
        recycler_main.adapter = adapter

        if (isInternetAvailable()) {
            getUsersData()
        } else{
            val users: Collection<User> = getData()
            listUsers.clear()
            listUsers.addAll(users)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun getUsersData() {
        showProgressBar()

        ApiClient.apiService.getUsers().enqueue(object : Callback<MutableList<User>> {
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                hideProgressBar()
                Log.e("error", t.localizedMessage)
            }

            override fun onResponse(
                    call: Call<MutableList<User>>,
                    response: Response<MutableList<User>>
            ) {
                try{
                    val list: MutableList<User> = mutableListOf()
                    val usersResponse = response.body()
                    usersResponse?.let { list.addAll(it) }

                    if(!usersResponse.isNullOrEmpty()){
                        if(list.size > 0){
                            for (user: User in list) {
                                saveData(user)
                            }
                        }
                    }

                    val users: Collection<User> = getData()
                    listUsers.clear()
                    listUsers.addAll(users)
                    adapter?.notifyDataSetChanged()

                    hideProgressBar()
                } catch (e: Exception){
                    hideProgressBar()
                }
            }
        })
    }

    fun saveData(singleUser: User) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                val userObj = User()
                userObj.id = singleUser.id
                userObj.name = singleUser.name
                userObj.userName = singleUser.userName
                userObj.email = singleUser.email
                userObj.phone = singleUser.phone
                userObj.website = singleUser.website
                userObj.status = singleUser.status
                realm.insertOrUpdate(userObj)
            }
        }
    }

    private fun getData(): ArrayList<User> {
        val searchQuery: RealmQuery<User> = realm.where(User::class.java)
        return ArrayList(searchQuery.findAll())
    }
}