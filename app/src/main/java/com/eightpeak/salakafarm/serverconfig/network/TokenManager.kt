//package com.eightpeak.salakafarm.serverconfig.network
//
//import android.content.Context
//import android.content.SharedPreferences
//
//class TokenManager {
//    companion object{
//
//        private var prefs: SharedPreferences? = null
//        private var editor: SharedPreferences.Editor? = null
//        var _context: Context? = null
//
//        private var INSTANCE: TokenManager? = null
//
//        private fun TokenManager(prefs: SharedPreferences) {
//            this.prefs = prefs
//            editor = prefs.edit()
//        }
//
//        @Synchronized
//        fun getInstance(prefs: SharedPreferences?): TokenManager? {
//            if (INSTANCE == null) {
//                INSTANCE = TokenManager(prefs)
//            }
//            return INSTANCE
//        }
//
//        fun saveToken(token: AccessToken) {
//            editor!!.putString("ACCESS_TOKEN", token.getAccessToken()).commit()
//            editor!!.putString("REFRESH_TOKEN", token.getRefreshToken()).commit()
//        }
//
//        fun deleteToken() {
//            editor!!.remove("ACCESS_TOKEN").commit()
//            editor!!.remove("REFRESH_TOKEN").commit()
//            editor!!.clear()
//        }
//
//        fun getToken(): AccessToken? {
//            val token = AccessToken()
//            token.setAccessToken(prefs!!.getString("ACCESS_TOKEN", null))
//            token.setRefreshToken(prefs!!.getString("REFRESH_TOKEN", null))
//            return token
//        }
//
//
//    }
//}