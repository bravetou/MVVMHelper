package com.brave.mvvm.mvvmhelper.http.download

import android.content.Intent
import android.text.TextUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.Utils
import com.brave.mvvm.mvvmhelper.base.CommonService
import com.brave.mvvm.mvvmhelper.bus.RxBus
import com.brave.mvvm.mvvmhelper.manager.ServiceManager
import com.lzy.okgo.OkGo

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/2 15:40
 *
 * ***desc***       ：资源下载服务(如需使用请在AndroidManifest文件中注册该Service)
 */
class DownloadService : CommonService() {
    private val mDownloadMap =
        mutableMapOf<String, DownloadMessage>()

    override fun onDestroy() {
        super.onDestroy()
        mDownloadMap.forEach {
            OkGo.getInstance().cancelTag(it.key)
        }
        mDownloadMap.clear()
    }

    companion object {
        private const val DOWNLOAD_PATH = "DOWNLOAD_PATH" // 下载路径
        private const val SAVE_FILE_NAME = "SAVE_FILE_NAME" // 保存文件名
        private const val SAVE_FILE_SUFFIX = "SAVE_FILE_NAME_SUFFIX" // 保存文件格式（后缀名）
        private const val SAVE_FILE_MARK = "SAVE_FILE_NAME_SUFFIX" // 保存临时文件标记

        @JvmStatic
        @JvmOverloads
        fun startSelf(
            downloadPath: String? = null,
            saveFileName: String? = null,
            saveFileSuffix: String? = null,
            saveFileMark: String? = null,
        ) {
            var intent = Intent(Utils.getApp(), DownloadService::class.java)
            if (!TextUtils.isEmpty(downloadPath)) {
                intent.putExtra(DOWNLOAD_PATH, downloadPath)
            }
            if (!TextUtils.isEmpty(saveFileName)) {
                intent.putExtra(SAVE_FILE_NAME, saveFileName)
            }
            if (!TextUtils.isEmpty(saveFileSuffix)) {
                intent.putExtra(SAVE_FILE_SUFFIX, saveFileSuffix)
            }
            if (!TextUtils.isEmpty(saveFileMark)) {
                intent.putExtra(SAVE_FILE_MARK, saveFileMark)
            }
            ServiceManager.instance.start("DownloadService", intent)
        }
    }

    override fun startCommand(intent: Intent?, flags: Int, startId: Int) {
        // 获取其下载地址
        var downloadPath = intent?.getStringExtra(DOWNLOAD_PATH) ?: ""
        // 获取保存文件名
        var saveFileName = intent?.getStringExtra(SAVE_FILE_NAME) ?: ""
        // 获取保存文件后缀
        var saveFileSuffix = intent?.getStringExtra(SAVE_FILE_SUFFIX) ?: ""
        // 获取临时文件标记
        var saveFileMark = intent?.getStringExtra(SAVE_FILE_MARK) ?: ""

        // 获取下载信息
        var message = mDownloadMap[downloadPath]
        // 如果下载载信息为空，则赋值为-1
        when (message?.downloadState ?: -1) {
            -1 -> {
                // 重新赋值
                message = FileDownload.instant.getDownloadMessage(
                    downloadPath,
                    "/hykd",
                    saveFileName,
                    saveFileSuffix,
                    saveFileMark
                )
            }
        }

        // 判断下载文件是否存在
        if (FileUtils.isFileExists(message!!.getCompleteFilesPath())) {
            message.downloadState = 1
            message.downloadProgress = 100.00
            // 发送下载成功消息
            RxBus.default.post(message)
            return
        }

        message.downloadPath?.let {
            // 把下载消息存入下载Map中
            mDownloadMap[it] = message

            // 开始下载资源
            FileDownload.instant.startDownload(message, it)
        }
    }
}