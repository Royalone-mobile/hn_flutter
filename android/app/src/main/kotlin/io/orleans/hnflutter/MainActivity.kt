package io.orleans.hnflutter

import android.content.Intent
import android.net.Uri
import android.os.Bundle

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

import io.orleans.hnflutter.constants.Channels

class MainActivity (): FlutterActivity() {
  private var deepLinkChannel: MethodChannel? = null

  override fun onCreate (savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)
    deepLinkChannel = MethodChannel(flutterView, Channels.DEEP_LINK_RECEIVED)
  }

  override fun onResume() {
    super.onResume()
    checkForLinkEvent(intent)
  }

  private fun checkForLinkEvent(intent: Intent) {
    Log.d(LOG_TAG, intent)
    if (intent.action == Intent.ACTION_VIEW && intent.data != null) {
      val path = intent.data.getQueryParameter("path")
      val query = intent.data.getQueryParameter("query")
      if (path != null) {
        val passedObjs = mutableMapOf<String, Any>("path" to path)
        if (query != null) {
          passedObjs["query"] = query
        }
        deepLinkChannel?.invokeMethod("linkReceived", passedObjs)
        Log.d(LOG_TAG, "Sent message to flutter: linkReceived=$path")
      }
    }
  }
}
