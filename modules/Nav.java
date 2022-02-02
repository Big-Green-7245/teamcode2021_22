package org.firstinspires.ftc.teamcode.modules;


import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import org.firstinspires.ftc.robotcore.external.navigation.*;


public class Nav {
    HardwareMap hwMap = null;

    BNO055IMU imu;

    Orientation angles;
    Acceleration gravity;
    MagneticFlux magneticField;

    double[] magFieldAngle = {0, 0, 0};
    double[] referenceOrientation = {0, 0, 0};


    /**
     *
     * Initializes module with a hardwareMap from robot
     * @param map hardware map of current robot
     *
     */
    public void init(HardwareMap map) {
        hwMap = map;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";

        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    /**
     *
     * Updates the sensors for information from the imu
     * including angles, gravity, and magnetic field
     *
     */
    private void updateSensorInfo() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity = imu.getGravity();
        magneticField = imu.getMagneticFieldStrength();
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        magFieldAngle[0]= Math.toDegrees(Math.atan2(magneticField.x, magneticField.y));
        magFieldAngle[1] = Math.toDegrees(Math.atan2(magneticField.y, magneticField.z));
        magFieldAngle[2] = Math.toDegrees(Math.atan2(magneticField.z, magneticField.x));
    }


    /**
     *
     * Turns the robot a certain number of degrees (± 3.6 deg)
     *
     * (CCW:+, CW:-)
     *
     */
    public void turnDeg(int deg) {

    }

    /**
     *
     * Return the translated (from magnetic flux) angle
     * of the magnetic field as seen
     *
     * [XY, YZ, ZX]
     *
     */
    public double[] getMagneticFieldDirection() {
        updateSensorInfo();
        return magFieldAngle;
    }

    /**
     *
     * Returns the current orientation of the robot as
     * per the gyro sensors
     *
     * [Yaw/Heading, Roll/Attitude, Pitch/Bank]
     *
     */
    private double[] getRawOrientation() {
        updateSensorInfo();
        return new double[]{formatAngle(angles.angleUnit, angles.firstAngle),
                formatAngle(angles.angleUnit, angles.secondAngle),
                formatAngle(angles.angleUnit, angles.thirdAngle)};
    }

    /**
     *
     * Returns the current orientation of the robot as
     * per the gyro sensors, with respect to the initial reference position
     *
     * [Yaw/Heading, Roll/Attitude, Pitch/Bank]
     *
     */
    public double[] getOrientation() {
        updateSensorInfo();
        return new double[]{formatAngle(angles.angleUnit, angles.firstAngle) - referenceOrientation[0],
                formatAngle(angles.angleUnit, angles.secondAngle) - referenceOrientation[1],
                formatAngle(angles.angleUnit, angles.thirdAngle) - referenceOrientation[2]};
    }

    /**
     *
     * Resets the orientation tracker (gyro) to 0
     * aka, initializes a new instance of the tracker.
     *
     */
    public void resetOrientationRef() {
        referenceOrientation = getRawOrientation();
    }

    public double formatAngle(AngleUnit angleUnit, double angle) {
        return AngleUnit.DEGREES.fromUnit(angleUnit, angle);
    }
}