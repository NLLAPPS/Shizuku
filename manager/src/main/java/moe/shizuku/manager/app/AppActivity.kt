package moe.shizuku.manager.app

import android.graphics.Color
import android.os.Build
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import moe.shizuku.manager.ktx.extGetThemeAttrColor
import com.google.android.material.R as MaterialThemeResources

abstract class AppActivity : AppCompatActivity(){
    fun setupEdgeToEdge() {
        /**
         * Status bar style can be default because we draw under it which is transparent
         */
        val statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)

        /**
         * Status bar style can be default because we draw under it which is transparent
         * We don't like scrim used by enableEdgeToEdge() by default.
         * It makes navigation bar prominent. We line our navigation bar to same as surface colour
         */
        val themeSurfaceColor = extGetThemeAttrColor(MaterialThemeResources.attr.colorSurface)
        val navigationBarStyle = SystemBarStyle.auto(themeSurfaceColor, themeSurfaceColor)
        /**
         * enableEdgeToEdge() must be called before on create
         */
        enableEdgeToEdge(statusBarStyle = statusBarStyle, navigationBarStyle = navigationBarStyle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

    }
}