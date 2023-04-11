/*
 * Copyright (c) 2010-2023 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.openhab.habdroid.core.location

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import org.openhab.habdroid.R
import org.openhab.habdroid.background.NotificationUpdateObserver

/**
 *
 */
@RequiresApi(Build.VERSION_CODES.S)
class LocationService : Service() {
    // TODO: Localization Resources
    // TODO: Logging

    /** system's locationManager; allowing access to position information */
    private val locationManager: LocationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationRequest: LocationRequest = LocationRequest.Builder(0)
        .setQuality(LocationRequest.QUALITY_BALANCED_POWER_ACCURACY)
        .setMaxUpdateDelayMillis(0)
        .setMaxUpdates(Integer.MAX_VALUE)
        .setDurationMillis((Long.MAX_VALUE))
        .build()

    private fun getProviderCriteria(): Criteria {
        val criteria = Criteria()

        when (this.locationRequest.quality) {
            LocationRequest.QUALITY_LOW_POWER -> {
                criteria.accuracy = Criteria.ACCURACY_COARSE
                criteria.horizontalAccuracy = Criteria.ACCURACY_LOW
                criteria.verticalAccuracy = Criteria.ACCURACY_LOW
                criteria.powerRequirement = Criteria.POWER_LOW
            }
            LocationRequest.QUALITY_BALANCED_POWER_ACCURACY -> {
                criteria.accuracy = Criteria.ACCURACY_FINE
                criteria.horizontalAccuracy = Criteria.ACCURACY_MEDIUM
                criteria.verticalAccuracy = Criteria.ACCURACY_MEDIUM
                criteria.powerRequirement = Criteria.POWER_MEDIUM
            }
            LocationRequest.QUALITY_HIGH_ACCURACY -> {
                criteria.accuracy = Criteria.ACCURACY_FINE
                criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
                criteria.verticalAccuracy = Criteria.ACCURACY_HIGH
                criteria.powerRequirement = Criteria.NO_REQUIREMENT
            }
        }

        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isSpeedRequired = false
        criteria.isCostAllowed = false

        return criteria
    }

    override fun onCreate() {
        super.onCreate()

        NotificationUpdateObserver.createNotificationChannels(this)

        val notification: Notification = Notification.Builder(
            this,
            "openHAB Location Service - POC"
        )
            .setContentTitle("openHAB Location Service")
            .setTicker("openHAB Location Service")
            .setContentTitle("Proof of Concept")
            .setSmallIcon(R.drawable.ic_openhab_appicon_24dp)
            .setOngoing(true)
            .setShowWhen(false)
            .setColor(ContextCompat.getColor(applicationContext, R.color.openhab_orange))
            .setCategory(Notification.CATEGORY_LOCATION_SHARING)
            .build()

        startForeground(NotificationUpdateObserver.NOTIFICATION_ID_BROADCAST_RECEIVER, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO: Implementation
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: Implementation
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun start() {
        this.locationManager
            .getBestProvider(this.getProviderCriteria(), true)
    }

    private fun stop() {

    }
}
