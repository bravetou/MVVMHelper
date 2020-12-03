package com.brave.mvvm.mvvmhelper.http.cookie

import com.brave.mvvm.mvvmhelper.http.cookie.store.CookieStore
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/2 11:04
 *
 * ***desc***       ：可以从传入HTTP响应接受cookie
 * 并向传出HTTP请求提供cookie的处理程序
 */
class CookieJarImpl(cookieStore: CookieStore?) : CookieJar {
    private val cookieStore: CookieStore

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.saveCookie(url, cookies)
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return (cookieStore.loadCookie(url) ?: return emptyList()) as List<Cookie>
    }

    fun getCookieStore(): CookieStore {
        return cookieStore
    }

    init {
        requireNotNull(cookieStore) { "cookieStore can not be null!" }
        this.cookieStore = cookieStore
    }
}