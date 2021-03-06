package com.example.ediya_kiosk.dialog

import android.app.Dialog
import android.util.Log
import android.view.WindowManager
import android.widget.*
import com.example.ediya_kiosk.activity.MainActivity
import com.example.ediya_kiosk.R
import android.content.Context as Context

class CouponOptionDialog(context: Context, CouponNum : Int?)
{
    private val dialog = Dialog(context)
    private var couponNum = CouponNum
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog(mainActivity: MainActivity) {
        dialog.setContentView(R.layout.coupon_dialog_layout)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val couponBtn1 = dialog.findViewById<Button>(R.id.couponBtn1)
        val couponBtn2 = dialog.findViewById<Button>(R.id.couponBtn2)
        val couponBtn3 = dialog.findViewById<Button>(R.id.couponBtn3)
        val couponBtn4 = dialog.findViewById<Button>(R.id.couponBtn4)
        val couponBtn5 = dialog.findViewById<Button>(R.id.couponBtn5)
        val couponBtnList = listOf<Button>(couponBtn1,couponBtn2,couponBtn3,couponBtn4,couponBtn5)

        if (couponNum != null) {
            Log.d("Message", "coupon num $couponNum")
            couponBtnList[couponNum!!].isSelected = true
        }


        dialog.show()

        for (i in 0..4) {
            couponBtnList[i]?.setOnClickListener {
                Toast.makeText(mainActivity, "쿠폰이 선택되었습니다.", Toast.LENGTH_SHORT).show()
                onClickListener.onClicked(i)
                Log.d("Message", "onClicked coupon num $i")
                dialog.dismiss()
            }
        }
    }



    interface OnDialogClickListener
    {
        fun onClicked(num: Int)
    }
}