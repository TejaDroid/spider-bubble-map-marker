package com.tejadroid.spiderview.base

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tejadroid.spiderview.extension.toastShort

/**
 * Created by Tejas on 9/26/17.
 */

abstract class BaseFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(layoutResId(), container, false)
    }

    @LayoutRes
    protected abstract fun layoutResId(): Int

    abstract fun name(): String

    companion object {
        val KEY_USER = "USER"
        val KEY_USER_LIST = "USER_LIST"
        val KEY_LOG = "LOG"
    }

    fun openDialPad(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${phoneNumber}")
        startActivity(intent)
    }

    fun openMailComposer(email: String, s: String, s1: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf(email)
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.putExtra(Intent.EXTRA_SUBJECT, s)
            intent.putExtra(Intent.EXTRA_TEXT, s1)
            intent.type = "message/rfc822"
            activity.startActivity(Intent.createChooser(intent, "Send mail"))
        } catch (e: ActivityNotFoundException) {
            activity.toastShort("Your device could not send e-mail. Please check e-mail configuration and try again.")
        }
    }
}
