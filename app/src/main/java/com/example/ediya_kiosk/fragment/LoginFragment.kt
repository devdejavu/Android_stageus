package com.example.ediya_kiosk.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ediya_kiosk.*
import com.example.ediya_kiosk.activity.LoginActivity
import com.example.ediya_kiosk.database.Database
import com.example.ediya_kiosk.database.DatabaseControl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var loginActivity: LoginActivity
    private var isExistBlank = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = activity as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.login_layout, container, false)

        val pref = loginActivity.getPreferences(0)
        var prefStep1 = pref.getBoolean("step2",false)
        var prefStep2 = pref.getBoolean("step3",false)
        Log.d("TAG","$prefStep1,$prefStep2")
        if (prefStep1) {
            val moveToMiddleDialog = AlertDialog.Builder(loginActivity)
            moveToMiddleDialog.setMessage("회원가입을 이어서 진행하시겠습니까?")
            moveToMiddleDialog.setPositiveButton("예", DialogInterface.OnClickListener { dialog, id ->
                if (prefStep2) loginActivity.register(3)
                else loginActivity.register(2)
            })
            moveToMiddleDialog.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, id ->
                removePrefData()
            })
            moveToMiddleDialog.show()
        }

        var loginBtn = view?.findViewById<Button>(R.id.loginBtn)
        var registerBtn = view?.findViewById<Button>(R.id.registerBtn)

        loginBtn!!.setOnClickListener {
            Log.d("TAG","login btn click")
            loginEvent()
        }

        registerBtn!!.setOnClickListener {
            loginActivity!!.register(1)
        }

        return view
    }

    fun loginEvent() {
        var loginId = view?.findViewById<EditText>(R.id.idET)?.text.toString()
        var loginPw = view?.findViewById<EditText>(R.id.pwET)?.text.toString()

        var columnArray = arrayOf("id","password")
        var selectionList = arrayListOf("id","password")
        var selectionArgsArray = arrayOf(loginId, loginPw)


        if (loginId.isEmpty() || loginPw.isEmpty()) {
            isExistBlank = true
            Toast.makeText(loginActivity,"아이디 또는 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
        }
        else {
            val db = Database(loginActivity, "ediya.db",null,1)
            val readableDb = db.readableDatabase
            val writableDb = db.writableDatabase
            val dbControl = DatabaseControl()

            var dataList = dbControl.readData(readableDb,"account",columnArray,selectionList,selectionArgsArray)
            if (dataList.size != 0) {
                connectLoginApi(loginId,loginPw,dataList)
            } else {
                Toast.makeText(loginActivity,"아이디 또는 비밀번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removePrefData() {
        val pref = loginActivity.getPreferences(0)
        val editor = pref.edit()
        editor.remove("step2")
        editor.remove("frontBirth")
        editor.remove("backBirth")
        editor.remove("mobileIndex")
        editor.remove("phoneNum")
        editor.apply()
    }

    private fun connectLoginApi(idValue : String, pwValue : String, dataList: ArrayList<ArrayList<String>>) {
        val retrofit = RetrofitClient.initRetrofit()

        val requestLoginApi = retrofit.create(LoginApi::class.java)
        requestLoginApi.getLogin(idValue, pwValue).enqueue(object : Callback<LoginData> {
            override fun onFailure(call: Call<LoginData>, t: Throwable) {
            }

            override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                Log.d("result", response.body()!!.message)
                Log.d("result", response.body()!!.success.toString())
                if (response.body()!!.success) {
                    loginActivity.login(dataList[0][0])
                } else {
                    Toast.makeText(loginActivity, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}