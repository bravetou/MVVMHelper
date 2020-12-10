package com.brave.mvvm.mvvmhelper.http.download

import android.text.TextUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.brave.mvvm.mvvmhelper.bus.RxBus
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import java.io.File

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/2 14:10
 *
 * ***desc***       ：文件下载
 */
class FileDownload {
    private object SingletonHolder {
        val sInstance = FileDownload()
    }

    companion object {
        @JvmStatic
        val instant = SingletonHolder.sInstance
    }

    private val defaultSavePath = PathUtils.getExternalDownloadsPath() // 默认保存目录
    private val defaultFileName = "my-file" // 默认文件名
    private val defaultFileSuffix = ".txt" // 默认后缀
    private val defaultFileMark = "temporary" // 默认临时文件标记

    /**
     * 根据下载文件地址生成[DownloadMessage]
     * @param downloadPath 资源文件路径
     * @param saveMutiCatalog 保存文件的多级目录，固定格式（eg:/app/debug）
     * @param fileName 自定义文件名
     * @param fileSuffix 自定义文件后缀，固定格式（eg:.txt）
     * @param fileMark 自定义临时文件标记
     */
    @JvmOverloads
    fun getDownloadMessage(
        downloadPath: String? = "",
        saveMutiCatalog: String? = "",
        fileName: String = "",
        fileSuffix: String = "",
        fileMark: String = "",
    ): DownloadMessage? {
        if (TextUtils.isEmpty(downloadPath)) return null
        var bean = DownloadMessage()
        // 下载资源路径
        bean.downloadPath = downloadPath!!
        // 获取下载网址最后分割位置
        val startIndex = downloadPath.lastIndexOf("/")
        // 获取文件全称
        val fileFullName = if (-1 == startIndex) {
            ""
        } else {
            downloadPath.substring(startIndex + 1)
        }
        // 获取文件后缀位置
        val endIndex = fileFullName.lastIndexOf(".")
        // 文件名
        bean.saveFileName = if (!TextUtils.isEmpty(fileName)) {
            fileName
        } else {
            if (-1 == endIndex) defaultFileName
            else fileFullName.substring(0, endIndex)
        }
        // 文件后缀
        bean.saveFileSuffix = if (!TextUtils.isEmpty(fileSuffix)) {
            fileSuffix
        } else {
            if (-1 == endIndex) defaultFileSuffix
            else fileFullName.substring(endIndex)
        }
        // 临时文件标记
        bean.saveFileMark = if (!TextUtils.isEmpty(fileMark)) {
            fileMark
        } else {
            defaultFileMark
        }
        // 保存文件目录
        bean.saveCatalog = if (TextUtils.isEmpty(saveMutiCatalog)) {
            "${defaultSavePath}/"
        } else {
            "${defaultSavePath}${saveMutiCatalog}/"
        }
        // 初始下载进度
        bean.downloadProgress = 0.00
        // 初始化下载状态
        bean.downloadState = 0
        return bean
    }

    /**
     * 开始下载资源
     */
    @JvmOverloads
    fun startDownload(message: DownloadMessage?, tag: String? = null) {
        if (null == message) return
        val mTag = if (TextUtils.isEmpty(tag)) {
            message.downloadPath
        } else {
            tag
        }
        OkGo.get<File>(message.downloadPath)
            .tag(mTag)
            .execute(object : FileCallback(
                message.saveCatalog,
                message.getTemporaryFilesName()
            ) {
                override fun onStart(request: Request<File, out Request<Any, Request<*, *>>>?) {
                    message.downloadState = 0
                    message.downloadProgress = 0.00
                    // 发送开始下载消息
                    RxBus.default.post(message)
                    // println("onStart => ${Gson().toJson(message)}")
                }

                override fun downloadProgress(progress: Progress?) {
                    message.downloadProgress =
                        progress!!.currentSize.toDouble() / progress.totalSize.toDouble()
                    // println("onProgress => ${Gson().toJson(message)}")
                }

                override fun onSuccess(response: Response<File>?) {
                    message.downloadState = 1
                    message.downloadProgress = 100.00
                    // 使用重命名法替换文件名
                    renameFile(
                        message.getTemporaryFilesPath(),
                        message.getCompleteFilesPath(),
                    )
                    // 发送下载成功消息
                    RxBus.default.post(message)
                    // println("onSuccess => ${Gson().toJson(message)}")
                }

                override fun onError(response: Response<File>?) {
                    message.downloadState = -1
                    // 发送下载失败消息
                    RxBus.default.post(message)
                    // println("onError => ${Gson().toJson(message)}")
                }
            })
    }

    /**
     * 重命名文件（使用重命名文件法防止文件损毁）
     * @param oldPath 旧文件的绝对路径
     * @param newPath 新文件的绝对路径
     */
    fun renameFile(oldPath: String, newPath: String): Boolean {
        if (TextUtils.isEmpty(oldPath)) {
            return false
        }
        if (TextUtils.isEmpty(newPath)) {
            return false
        }
        val oldFile = File(oldPath)
        if (!FileUtils.isFileExists(oldFile)) {
            return false
        }
        var newFile = File(newPath)
        if (FileUtils.isFileExists(newFile)) {
            return false
        }
        oldFile.renameTo(newFile)
        return true
    }
}