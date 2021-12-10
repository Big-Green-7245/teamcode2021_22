package org.firstinspires.ftc.teamcode;

// Standard Lib
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import java.lang.reflect.Method;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// Sensor
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;    
import android.hardware.SensorManager;

// Telemetry Wrapper
import org.firstinspires.ftc.teamcode.util.*;

@TeleOp(name="TeleOpFailure", group="Opmode")
//@Disabled
public class TeleOpFailure extends LinearOpMode implements SensorEventListener {
    // Device | Component initialization
    DcMotor backLeft;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor frontRight;

    // Attribues
    String programVer = "0.1";
    
    // Context + Sensor manager
    Context context;
    SensorManager sensorManager;

    // LPF filter Alpha
    final float LPF_ALPHA = 0.25f;
    
    // System time
    long lastSensorUpdateSysTime = System.currentTimeMillis();
    long lastGyroChangeTime = System.currentTimeMillis();

    // Sensor data
    float[] magDataRaw = {0, 0, 0};
    float[] gyroDataRaw = {0, 0, 0};
    double[] magAng = {0, 0, 0};
    double[] gyroAng = {0, 0, 0};

    @Override
    public void runOpMode(){
        // setUpDevices();
        context = hardwareMap.appContext;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);
                
        TelemetryWrapper.init(telemetry, 12);
                
        waitForStart();

        TelemetryWrapper.setLine(1, "Press start button to start >");
        
        waitForStart();

        resetOrientationInfo();

        while(opModeIsActive()) {
            // Mecanum wheels
            // backLeft.setPower((gamepad1.left_stick_y + gamepad1.left_stick_x + (gamepad1.right_stick_x)));
            // backRight.setPower((gamepad1.left_stick_y - gamepad1.left_stick_x - (gamepad1.right_stick_x)));
            // frontLeft.setPower((gamepad1.left_stick_y + gamepad1.left_stick_x - (gamepad1.right_stick_x)));
            // frontRight.setPower((gamepad1.left_stick_y - gamepad1.left_stick_x + (gamepad1.right_stick_x)));

            // Add data for orientation
            TelemetryWrapper.setLine(1, "Autonomous v" + programVer);
            TelemetryWrapper.setLine(2, "Magnetic Fields [" + magAng[0] + ", " + magAng[1] + ", " + magAng[2] + "]");
            TelemetryWrapper.setLine(3, "Gyro Fields [" + gyroAng[0] + ", " + gyroAng[1] + ", " + gyroAng[2] + "]");
        }

    }

    public void setUpDevices() {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Listener for updating data of the sensors
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            long tDiff = System.currentTimeMillis() - lastGyroChangeTime;
            lastGyroChangeTime = System.currentTimeMillis();
            gyroDataRaw = lowPass(event.values.clone(), gyroDataRaw);
            gyroAng[0] += Math.toDegrees(tDiff / 1000.0 * gyroDataRaw[0]);
            gyroAng[1] += Math.toDegrees(tDiff / 1000.0 * gyroDataRaw[1]);
            gyroAng[2] += Math.toDegrees(tDiff / 1000.0 * gyroDataRaw[2]);
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magDataRaw = lowPass(event.values.clone(), magDataRaw);
            magAng[0]= Math.toDegrees(Math.atan2(magDataRaw[0], magDataRaw[1]));
            magAng[1] = Math.toDegrees(Math.atan2(magDataRaw[1], magDataRaw[2]));
            magAng[2] = Math.toDegrees(Math.atan2(magDataRaw[2], magDataRaw[0]));
        }
        lastSensorUpdateSysTime = System.currentTimeMillis();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Unused
    }

    // A simple implementation of the Low Pass Filter (LPF)
    public float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + LPF_ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    // Resets the current orientation for magnetic and gyroscope angles
    public void resetOrientationInfo() {
        magAng = new double[]{0, 0, 0};
        gyroAng = new double[]{0, 0, 0};
    }
}
