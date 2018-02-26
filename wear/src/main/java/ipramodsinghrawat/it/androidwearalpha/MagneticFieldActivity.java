package ipramodsinghrawat.it.androidwearalpha;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MagneticFieldActivity extends WearableActivity implements SensorEventListener {

    DecimalFormat decimalFormat;
    private SensorManager mSensorManager;
    private Sensor senMagneticField;
    private TextView sensorReadingTV,sensorDetailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Enables Always-on
        setAmbientEnabled();

        sensorReadingTV = findViewById(R.id.sensorReadingTV);
        sensorDetailTV = findViewById(R.id.sensorDetailTV);

        decimalFormat = new DecimalFormat("####.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        decimalFormat.setMaximumFractionDigits(1);
        decimalFormat.setMinimumFractionDigits(1);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magneticReading(sensorEvent);
        }
    }

    public void magneticReading(SensorEvent sensorEvent){
        Sensor mySensor = sensorEvent.sensor;
        sensorReadingTV.setText(
                "\n X= " + decimalFormat.format(sensorEvent.values[0]) +" micro-Tesla (μT) "+
                        "\n Y= " + decimalFormat.format(sensorEvent.values[1]) +" micro-Tesla (μT) "+
                        "\n Z= " + decimalFormat.format(sensorEvent.values[2]) +" micro-Tesla (μT) ");

        sensorDetailTV.setText(
                " MaximumRange: " +mySensor.getMaximumRange()+" micro-Tesla (μT) "+
                        "\n Vendor: " + mySensor.getVendor() +
                        "\n Version: " + mySensor.getVersion()+
                        "\n Resolution: " + mySensor.getResolution()+
                        "\n Power: " + mySensor.getPower());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, senMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister sensor listeners to prevent the activity from draining the device's battery.
        mSensorManager.unregisterListener(this);
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop(){
        super.onStop();
        //unregister the sensor listener
        mSensorManager.unregisterListener(this);
    }
}
