package com.example.memory_game

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var mGravity = FloatArray(3)
    private var mGeomagnetic = FloatArray(3)
    private var azimuth = 0f
    private var sensorManager: SensorManager? = null

    private var array = MutableList(24) {0}
    private var arrayNorth: ArrayList<Int>? = null
    private var arrayEast: ArrayList<Int>? = null
    private var arraySouth: ArrayList<Int>? = null
    private var arrayWest: ArrayList<Int>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?

        array[0] = R.drawable.circle
        array[1] = R.drawable.circle
        array[2] = R.drawable.circle_red
        array[3] = R.drawable.circle_red
        array[4] = R.drawable.heart
        array[5] = R.drawable.heart
        array[6] = R.drawable.heart_red
        array[7] = R.drawable.heart_red
        array[8] = R.drawable.snowflake
        array[9] = R.drawable.snowflake
        array[10] = R.drawable.snowflake_purple
        array[11] = R.drawable.snowflake_purple
        array[12] = R.drawable.square
        array[13] = R.drawable.square
        array[14] = R.drawable.square_blue
        array[15] = R.drawable.square_blue
        array[16] = R.drawable.star
        array[17] = R.drawable.star
        array[18] = R.drawable.star_yellow
        array[19] = R.drawable.star_yellow
        array[20] = R.drawable.triangle
        array[21] = R.drawable.triangle
        array[22] = R.drawable.triangle_green
        array[23] = R.drawable.triangle_green

        array.shuffle()

        val temp = array.chunked(6)

        arrayNorth = temp[0] as ArrayList<Int>
        arrayEast = temp[1] as ArrayList<Int>
        arraySouth = temp[2] as ArrayList<Int>
        arrayWest = temp[3] as ArrayList<Int>

        val transaction =  supportFragmentManager.beginTransaction()
        val fragment = North()
        val bundle = Bundle()

        bundle.putIntegerArrayList("array", arrayNorth)
        fragment.arguments = bundle

        direction_TV.text  = resources.getString(R.string.direction_north)
        transaction.replace(R.id.holder, fragment, "NORTH")
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(this, sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
        sensorManager?.registerListener(this, sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME)
        sensorManager?.registerListener(this, sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        val alpha = 0.97f
        synchronized(this) {
            if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                if (event.values[0] <= 100) {
                    val intent = Intent(this, ToDarkActivity::class.java)
                    startActivity(intent)
                }
            }
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha*mGravity[0]+(1-alpha)*event.values[0]
                mGravity[1] = alpha*mGravity[1]+(1-alpha)*event.values[1]
                mGravity[2] = alpha*mGravity[2]+(1-alpha)*event.values[2]
            }
            if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha*mGeomagnetic[0]+(1-alpha)*event.values[0]
                mGeomagnetic[1] = alpha*mGeomagnetic[1]+(1-alpha)*event.values[1]
                mGeomagnetic[2] = alpha*mGeomagnetic[2]+(1-alpha)*event.values[2]
            }


            val X = FloatArray(9)
            val Y = FloatArray(9)

            val success = SensorManager.getRotationMatrix(X, Y, mGravity, mGeomagnetic)

            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(X, orientation)
                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth+360)%360

                val transaction =  supportFragmentManager.beginTransaction()

                if ((azimuth in 0f..45f) || (315f < azimuth && azimuth <360f)) {
                    val fragment = North()
                    if (supportFragmentManager.fragments[0]?.tag != "NORTH") {
                        val bundle = Bundle()
                        bundle.putIntegerArrayList("array", arrayNorth)
                        fragment.arguments = bundle

                        direction_TV.text = resources.getString(R.string.direction_north)
                        transaction.replace(R.id.holder, fragment, "NORTH")
                        transaction.commit()
                    }
                } else if (45f < azimuth && azimuth <= 135f) {
                    val fragment = East()
                    if (supportFragmentManager.fragments[0]?.tag != "EAST") {
                        val bundle = Bundle()
                        bundle.putIntegerArrayList("array", arrayEast)
                        fragment.arguments = bundle

                        direction_TV.text  = resources.getString(R.string.direction_east)
                        transaction.replace(R.id.holder, fragment, "EAST")
                        transaction.commit()
                    }
                } else if (135f < azimuth && azimuth <= 225f) {
                    val fragment = South()
                    if (supportFragmentManager.fragments[0]?.tag != "SOUTH") {
                        val bundle = Bundle()
                        bundle.putIntegerArrayList("array", arraySouth)
                        fragment.arguments = bundle

                        direction_TV.text  = resources.getString(R.string.direction_south)
                        transaction.replace(R.id.holder, fragment, "SOUTH")
                        transaction.commit()
                    }
                } else {
                    val fragment = West()
                    if (supportFragmentManager.fragments[0]?.tag != "WEST") {
                        val bundle = Bundle()
                        bundle.putIntegerArrayList("array", arrayWest)
                        fragment.arguments = bundle

                        direction_TV.text  = resources.getString(R.string.direction_west)
                        transaction.replace(R.id.holder, fragment, "WEST")
                        transaction.commit()
                    }
                }
            }
        }
    }
}