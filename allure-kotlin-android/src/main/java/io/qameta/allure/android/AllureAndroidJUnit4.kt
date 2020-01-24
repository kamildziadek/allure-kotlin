package io.qameta.allure.android

import android.annotation.SuppressLint
import android.os.Build
import android.os.Environment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.permission.PermissionRequester
import io.qameta.allure.Allure
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.FileSystemResultsWriter
import io.qameta.allure.junit4.AllureJunit4
import io.qameta.allure.util.PropertiesUtils
import org.junit.runner.*
import org.junit.runner.manipulation.*
import org.junit.runner.notification.*
import java.io.File

//TODO add kdoc
class AllureAndroidJUnit4(clazz: Class<*>) : Runner(), Filterable, Sortable {

    private val delegate = AndroidJUnit4(clazz)

    override fun run(notifier: RunNotifier?) {
        notifier?.addListener(createAllureListener())
        delegate.run(notifier)
    }

    private fun createAllureListener(): AllureJunit4 {
        val allurePath = resolvePath()

        return with(AllureLifecycle(writer = FileSystemResultsWriter(allurePath))) {
            Allure.lifecycle = this
            AllureJunit4(this)
        }
    }

    override fun getDescription(): Description = delegate.description

    override fun filter(filter: Filter?) = delegate.filter(filter)

    override fun sort(sorter: Sorter?) = delegate.sort(sorter)

    @SuppressLint("DefaultLocale")
    private fun isDeviceTest() = System.getProperty("java.runtime.name")?.toLowerCase()?.contains("android") ?: false

    private fun resolvePath(): File {
        val defaultAllurePath = PropertiesUtils.resultsDirectoryPath
        return when {
            isDeviceTest() -> {
                requestExternalStoragePermissions()
                File(Environment.getExternalStorageDirectory(), defaultAllurePath)
            }
            else -> File(defaultAllurePath)
        }
    }

    private fun requestExternalStoragePermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        with(PermissionRequester()) {
            addPermissions("android.permission.WRITE_EXTERNAL_STORAGE")
            addPermissions("android.permission.READ_EXTERNAL_STORAGE")
            requestPermissions()
        }
    }

}