package moe.shizuku.manager.app;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.TypedValue;
import androidx.annotation.StyleRes;
import com.google.android.material.snackbar.Snackbar;
import moe.shizuku.manager.R;
import moe.shizuku.manager.ShizukuSettings;
import moe.shizuku.manager.utils.EnvironmentUtils;
import moe.shizuku.manager.utils.ResourceUtils;


public class ThemeHelper {

    private static final String THEME_DEFAULT = "DEFAULT";
    private static final String THEME_BLACK = "BLACK";

    public static boolean isBlackNightTheme(Context context) {
        return ShizukuSettings.getPreferences().getBoolean(ShizukuSettings.Keys.KEY_BLACK_NIGHT_THEME, EnvironmentUtils.isWatch());
    }

    public static boolean isUsingSystemColor() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && ShizukuSettings.getPreferences().getBoolean(ShizukuSettings.Keys.KEY_USE_SYSTEM_COLOR, true);
    }

    public static String getTheme(Context context) {
        if (isBlackNightTheme(context)
                && ResourceUtils.isNightMode(context.getResources().getConfiguration()))
            return THEME_BLACK;

        return ShizukuSettings.getPreferences().getString(ShizukuSettings.Keys.KEY_LIGHT_THEME, THEME_DEFAULT);
    }



    public static void applySnackbarTheme(Context context, Snackbar snackbar) {
        snackbar.setBackgroundTint(resolveColor(context, R.attr.colorPrimaryContainer))
            .setTextColor(resolveColor(context, R.attr.colorOnSurface))
            .setActionTextColor(resolveColor(context, R.attr.colorPrimary));
    }

    private static int resolveColor(Context context, int color) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(color, typedValue, true);
        return typedValue.data;
    }
}
