package com.brave.mvvm.mvvmhelper.http.download

import android.text.TextUtils

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/2 14:15
 *
 * ***desc***       ：资源下载
 * @param downloadPath 资源地址
 * @param saveCatalog 保存目录
 * @param saveFileName 保存文件名
 * @param saveFileSuffix 保存文件后缀
 * @param saveFileMark 保存临时文件标记
 * @param downloadState 下载状态（-1 下载失败 | 0 下载中 | 1 下载完成）
 * @param downloadProgress 下载进度
 */
data class DownloadMessage(
    var downloadPath: String? = "",
    var saveCatalog: String? = "",
    var saveFileName: String? = "",
    var saveFileSuffix: String? = "",
    var saveFileMark: String? = "",
    var downloadState: Int? = 0,
    var downloadProgress: Double? = 0.00,
) {
    /**
     * 是否下载成功
     */
    fun isSuccess() = downloadState == 1

    /**
     * 是否下载失败
     */
    fun isFailure() = downloadState == -1

    /**
     * 是否正在下载
     */
    fun isDownloading() = downloadState == 0

    /**
     * 是否使用文件名替换法
     * 检测文件下载完成
     */
    fun isUseFileNameReliefMethod() = !TextUtils.isEmpty(saveFileMark)

    /**
     * 临时文件路径
     */
    fun getTemporaryFilesPath() =
        "${saveCatalog}${saveFileName}${saveFileMark}${saveFileSuffix}"

    /**
     * 临时文件名
     */
    fun getTemporaryFilesName() =
        "${saveFileName}${saveFileMark}${saveFileSuffix}"

    /**
     * 完成文件路径
     */
    fun getCompleteFilesPath() =
        "${saveCatalog}${saveFileName}${saveFileSuffix}"

    /**
     * 完成文件名
     */
    fun getCompleteFilesName() =
        "${saveFileName}${saveFileSuffix}"
}