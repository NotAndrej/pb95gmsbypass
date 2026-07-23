package dev.na.pb95gmsbypass;

import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class GmsBypassHook implements IXposedHookLoadPackage {

    private static final String TARGET_PACKAGE = "com.spookyhousestudios.progressbar95";
    private static final String GMS_PACKAGE = "com.google.android.gms";

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) {
        if (!TARGET_PACKAGE.equals(lpparam.packageName)) {
            return;
        }

        hookPackageServicesState(lpparam);
        hookGoogleApiAvailability(lpparam);
    }

    /**
     * Forces the game's own package-state lookup for com.google.android.gms
     * to always report ENABLED, regardless of what's actually installed.
     */
    private void hookPackageServicesState(LoadPackageParam lpparam) {
        try {
            Class<?> packageStateClass = XposedHelpers.findClass(
                    "com.ansca.corona.storage.PackageState",
                    lpparam.classLoader
            );

            final Object enabledState =
                    XposedHelpers.getStaticObjectField(packageStateClass, "ENABLED");

            XposedHelpers.findAndHookMethod(
                    "com.ansca.corona.storage.PackageServices",
                    lpparam.classLoader,
                    "getPackageState",
                    String.class,
                    int.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            String pkg = (String) param.args[0];

                            if (GMS_PACKAGE.equals(pkg)) {
                                param.setResult(enabledState);
                            }
                        }
                    }
            );
        } catch (Throwable ignored) {
        }
    }

    /**
     * Forces the real GoogleApiAvailability self-check to report SUCCESS.
     */
    private void hookGoogleApiAvailability(LoadPackageParam lpparam) {
        try {
            XposedHelpers.findAndHookMethod(
                    "com.google.android.gms.common.GoogleApiAvailability",
                    lpparam.classLoader,
                    "isGooglePlayServicesAvailable",
                    Context.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            param.setResult(0); // ConnectionResult.SUCCESS
                        }
                    }
            );
        } catch (Throwable ignored) {
        }
    }
}
