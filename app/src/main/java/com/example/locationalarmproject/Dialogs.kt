package com.example.locationalarmproject

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

class TimeAlertDialog : DialogFragment() {
    interface Listener {
    }
    private var listener : Listener? = null
    /**
     * リスナー用変数にアクティビティをセット
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context){
            is Listener -> listener = context
        }
    }



    class DatePickerFragment (): DialogFragment(),
        DatePickerDialog.OnDateSetListener, Parcelable {
        /**
         *   OnDateSelectedListener:日付が選択された時の処理
         */
        interface OnDateSelectedListener{
            fun onSelected(year: Int, month: Int, date: Int)
        }

        private var listener: OnDateSelectedListener? = null

        constructor(parcel: Parcel) : this() {

        }

        override fun onAttach(context: Context?) {
            super.onAttach(context)
            when (context) {
                is OnDateSelectedListener -> listener = context
            }
        }


        @RequiresApi(Build.VERSION_CODES.N)
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            /**
             * 現在の日付を初期値として設定
             */
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val date = c.get(Calendar.DAY_OF_MONTH)
            return DatePickerDialog(requireActivity(), this, year , month, date)
        }
        /**
         * thisを指定
         */
        override fun onDateSet(view: DatePicker?, year:Int,
                               month: Int, dayOfMonth: Int){
            listener?.onSelected(year, month, dayOfMonth)

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