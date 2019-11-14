package com.example.locationalarmproject

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment

class TimeAlertDialog2 : DialogFragment() {
    interface Listener {}
    private var listener : Listener? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when (context){
            is Listener -> listener = context
        }
    }



    class DatePickerFragment(): DialogFragment(),
        DatePickerDialog.OnDateSetListener, Parcelable {
        /**
         *   OnDateSelectedListener:日付が選択された時の処理
         */
        interface OnDateSelectedListener{
            fun onSelected2(year2: Int, month2: Int, date2: Int)
        }

        private var listener: OnDateSelectedListener? = null

        constructor(parcel: Parcel) : this() {

        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            when (context) {
                is OnDateSelectedListener -> listener = context
            }
        }


        @SuppressLint("WrongConstant")
        override  fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            /**
             * 現在の日付を初期値として設定
             */
            val d = java.util.Calendar.getInstance()
            val year2 = d.get(Calendar.YEAR)
            val month2 = d.get(Calendar.MONTH)
            val date2 = d.get(Calendar.DAY_OF_MONTH)
            return DatePickerDialog(requireActivity(), this, year2 , month2, date2)
        }
        /**
         * thisを指定
         */
        override fun onDateSet(view: DatePicker?, year2:Int,
                               month2: Int, dayOfMonth2: Int){
            listener?.onSelected2(year2, month2, dayOfMonth2)

        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {

        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<DatePickerFragment> {
            override fun createFromParcel(parcel: Parcel): DatePickerFragment {
                return DatePickerFragment(parcel)
            }

            override fun newArray(size: Int): Array<DatePickerFragment?> {
                return arrayOfNulls(size)
            }
        }
    }


}


