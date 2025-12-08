package com.vishal2376.vortex.core.utils

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Author: Vishal Singh (vishal2376)
 */

@Composable
fun checkAudioPermission(onPermissionGranted: () -> Unit) {
	val context = LocalContext.current

	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission()
	) { wasGranted ->
		if (wasGranted) {
			onPermissionGranted()
		} else {
			Toast.makeText(
				context,
				"Audio permission is required",
				Toast.LENGTH_SHORT
			).show()
		}
	}

	LaunchedEffect(Unit) {
		val permissionCheck =
			ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
		if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
			onPermissionGranted()
		} else {
			launcher.launch(Manifest.permission.RECORD_AUDIO)
		}
	}
}