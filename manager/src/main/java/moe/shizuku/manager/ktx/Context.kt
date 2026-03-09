package moe.shizuku.manager.ktx

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.UserManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import moe.shizuku.manager.ShizukuApplication

val Context.application: ShizukuApplication
    get() {
        return applicationContext as ShizukuApplication
    }

fun Context.createDeviceProtectedStorageContextCompat(): Context {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        createDeviceProtectedStorageContext()
    } else {
        this
    }
}

fun Context.createDeviceProtectedStorageContextCompatWhenLocked(): Context {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && getSystemService(UserManager::class.java)?.isUserUnlocked != true) {
        createDeviceProtectedStorageContext()
    } else {
        this
    }
}

@ColorInt
fun Context.extGetThemeAttrColor(@AttrRes colorAttr: Int): Int {
    val array = obtainStyledAttributes(null, intArrayOf(colorAttr))
    return try {
        array.getColor(0, 0)
    } finally {
        array.recycle()
    }
}
fun Context.unwrap(): Context {
    if (this is ContextWrapper) {
        return this.baseContext.unwrap()
    }
    return this
}

inline fun <reified T : Activity> Context.asActivity(): T {
    if (this is T) {
        return this
    } else {
        var context = this
        while (true) {
            if (context is ContextWrapper) {
                context = context.baseContext
                if (context is T) {
                    return context
                }
            } else {
                throw ClassCastException("Context instance $this is not Activity")
            }
        }
    }

}