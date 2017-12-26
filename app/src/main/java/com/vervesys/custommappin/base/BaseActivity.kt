package com.vervesys.custommappin.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.tejadroid.spiderview.base.BaseFragment

/**
 * Created by Tejas on 9/20/17.
 */

abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    protected abstract fun layoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
    }

    fun showFragment(@IdRes containId: Int, fragment: BaseFragment) {
        supportFragmentManager.beginTransaction().addToBackStack(fragment.name())
                .replace(containId, fragment, fragment.name()).commitAllowingStateLoss()
    }

    fun showFragment(@IdRes containId: Int, fragment: Fragment, name: String) {
        supportFragmentManager.beginTransaction().addToBackStack(name)
                .replace(containId, fragment, name).commitAllowingStateLoss()
    }

    fun getCurrentFragment(@IdRes containId: Int): Fragment? {
        return supportFragmentManager.findFragmentById(containId)
    }

    fun clearBackStack() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStackImmediate(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }
}