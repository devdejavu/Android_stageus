package com.example.ediya_kiosk.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.ediya_kiosk.fragment.basket_fragment

class BasketService : Service() {

    val binder = BasketServiceBinder()

    // Menu Data
    lateinit var menuNameList : ArrayList<String>
    lateinit var menuTempList : ArrayList<String>
    lateinit var menuSizeList : ArrayList<String>
    lateinit var menuPriceList : ArrayList<Int>
    lateinit var menuTotalPriceList : ArrayList<Int>
    lateinit var menuImgList : ArrayList<String>


    inner class BasketServiceBinder : Binder() {
        fun getService() : BasketService {
            return this@BasketService
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    fun getMenuData(menuName : String, menuTemp: String, menuSize : String,
                    menuPrice : Int, menuTotalPrice : Int, menuImg : String) {

        menuNameList.add(menuName)
        Log.d("Message","$menuName add in BasketService")
        menuTempList.add(menuTemp)
        menuSizeList.add(menuSize)
        menuPriceList.add(menuPrice)
        menuTotalPriceList.add(menuTotalPrice)
        menuImgList.add(menuImg)
    }

    fun putMenuData(frag : Fragment) : Fragment {
        var bundle = Bundle()

        bundle.putStringArrayList("nameList",menuNameList)
        Log.d("Message","$menuNameList add in BasketService")
        bundle.putStringArrayList("tempList",menuTempList)
        bundle.putStringArrayList("sizeList",menuSizeList)
        bundle.putIntegerArrayList("priceList",menuPriceList)
        bundle.putIntegerArrayList("totalPriceList",menuTotalPriceList)
        bundle.putStringArrayList("imgList",menuImgList)

        frag.arguments = bundle

        return frag
    }

    fun getNotificationInformation(): ArrayList<Int> {
        var totalPriceTxt = 0
        var menuNum = 0
        for (price in menuTotalPriceList!!) {
            menuNum++
            totalPriceTxt += price
        }
        Log.d("Message", "1) $menuNum, $totalPriceTxt")
        return arrayListOf(menuNum, totalPriceTxt)
    }

    fun initializeList() {
        menuNameList.clear()
        menuTempList.clear()
        menuSizeList.clear()
        menuPriceList.clear()
        menuImgList.clear()
        menuTotalPriceList.clear()
    }

    override fun onCreate() {
        super.onCreate()
        menuNameList = arrayListOf()
        menuTempList = arrayListOf()
        menuSizeList = arrayListOf()
        menuPriceList = arrayListOf()
        menuTotalPriceList = arrayListOf()
        menuImgList = arrayListOf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int { //서비스가 실행될 때
        return super.onStartCommand(intent, flags, startId)
    }
}