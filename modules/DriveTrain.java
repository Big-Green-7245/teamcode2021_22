package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import java.text.DecimalFormat;
import java.util.*;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.*;

public class DriveTrain
{
    private ElapsedTime runtime = new ElapsedTime();
    DecimalFormat nF = new DecimalFormat("#.00");

    final double XY_CORRECTION = 10/9.2;
    final double COUNTS_PER_INCH = 27.7;
    final double COUNTS_PER_DEGREE = 2140 / 360;

    public HardwareMap hwMap;
    public double[] position = {0, 0};
    Nav direction;

    DcMotor backLeft;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor frontRight;

    public void init(HardwareMap map, double length, double breadth)
    {
        hwMap = map;

        backLeft = hwMap.get(DcMotor.class, "leftBack");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        backRight = hwMap.get(DcMotor.class, "rightBack");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft = hwMap.get(DcMotor.class, "leftFront");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontRight = hwMap.get(DcMotor.class, "rightFront");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // MainMap = new org.firstinspires.ftc.teamcode.modules.Map(length, breadth);
        direction = new Nav();
        direction.init(hwMap);
    }

    public static double getLen(double[] startPoint, double[] endPoint) {
        return Math.sqrt((endPoint[0] - startPoint[0]) * (endPoint[0] - startPoint[0]) + (endPoint[1] - startPoint[1]) * (endPoint[1] - startPoint[1]));
    }


    public void moveToLocation(double[] location, double speed)
    {
        double distance = getLen(position, location);
        turn(Math.asin(position[0]/distance));
        Translate(distance, speed);
    }

    public void followPath(List<double[]> path, double speed)
    {
        for (int i = 0; i < path.size(); i++)
        {
            moveToLocation(path.get(i), speed);
        }
    }

    public void turn(double bearingDelta)
    {
        double initialBearing = direction.getOrientation()[0];
        move(0, 0, 0.1, 1);
        while (Math.abs(initialBearing - direction.getOrientation()[1]) < bearingDelta)
        {
        }
        stop();
    }

    public void move(double powerx, double powery, double turn, double factor) {
        double speedx = factor * powerx;
        double speedy = factor * powery;
        double offset = factor * turn;

        //TelemetryWrapper.setLine(7, "move-param: " + nF.format(speedy-speedx-offset) + ":" + nF.format(speedy+speedx+offset) + ":" + nF.format(speedy+speedx-offset) + ":" + nF.format(speedy-speedx+offset));

        frontLeft.setPower(Range.clip(speedy-speedx-offset,-1,1));
        frontRight.setPower(Range.clip(speedy+speedx+offset,-1,1));
        backLeft.setPower(Range.clip(speedy+speedx-offset,-1,1));
        backRight.setPower(Range.clip(speedy-speedx+offset,-1,1));
    }

    public void Translate(double distance, double speed)
    {

        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + (int) (distance *COUNTS_PER_INCH));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + (int) (distance*COUNTS_PER_INCH));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + (int) (distance*COUNTS_PER_INCH));
        backRight.setTargetPosition(backRight.getCurrentPosition() + (int) (distance*COUNTS_PER_INCH));
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerToAllDriveMotors(speed);
        while (frontLeft.isBusy()&&frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())
        {
        }
        stop();
        // Update position
        position[0] = Math.sin(direction.getOrientation()[0])  * distance;
        position[1] = Math.cos(direction.getOrientation()[0]) * distance;
        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void translate(double power, double dX, double dY, double dTheta, double timeout)
    {
        int newFLTarget;
        int newFRTarget;
        int newBLTarget;
        int newBRTarget;
        int dFL, dFR, dBL, dBR;
        int maxDelta;

        position = new double[]{position[0] + dX,position[1] + dY};

        // Determine new target position, and pass to motor controller
        dFL = (int)((-dY -dX * XY_CORRECTION) * COUNTS_PER_INCH + dTheta * COUNTS_PER_DEGREE);
        dFR = (int)((-dY +dX * XY_CORRECTION) * COUNTS_PER_INCH - dTheta * COUNTS_PER_DEGREE);
        dBL = (int)((-dY +dX * XY_CORRECTION) * COUNTS_PER_INCH + dTheta * COUNTS_PER_DEGREE);
        dBR = (int)((-dY -dX * XY_CORRECTION) * COUNTS_PER_INCH - dTheta * COUNTS_PER_DEGREE);
        maxDelta = max(max((int)(abs(dBL)), (int)(abs(dFL))), max((int)(abs(dBR)), (int)(abs(dFR))));

        setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
        setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        newFLTarget = frontLeft.getCurrentPosition() + dFL;
        newFRTarget = frontRight.getCurrentPosition() + dFR;
        newBLTarget = backLeft.getCurrentPosition() + dBL;
        newBRTarget = backRight.getCurrentPosition() + dBR;

        frontLeft.setTargetPosition(newFLTarget);
        frontRight.setTargetPosition(newFRTarget);
        backLeft.setTargetPosition(newBLTarget);
        backRight.setTargetPosition(newBRTarget);

        // Turn On RUN_TO_POSITION
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();

        setZeroPowerBehaviorToAllDriveMotors(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        TelemetryWrapper.setLine(0,  "Running to (x:y:r)=("+dX+":"+dY +":"+dTheta+")");
        TelemetryWrapper.setLine(1,  "Wheels to (lf:rf:lr:rr) ("+newFLTarget+":"+newFRTarget +":"+newBLTarget+":"+newBRTarget+")");
        while ((runtime.seconds() < timeout) && (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {
            TelemetryWrapper.setLine(2,  "Running @ ("+frontLeft.getCurrentPosition()+":"+frontRight.getCurrentPosition()
                    +":"+backLeft.getCurrentPosition()+":"+backRight.getCurrentPosition()+")");
        }

        // Stop all motion;
        setPowerToAllDriveMotors(0);
        TelemetryWrapper.setLine(10, "Motor power 0");

        // Turn off RUN_TO_POSITION
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double[] getEncPos() {
        return new double[]{frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(), backLeft.getCurrentPosition(), backRight.getCurrentPosition()};
    }

    public String getEncPosStr() {
        return "FL" + frontLeft.getCurrentPosition() + " FR" + frontRight.getCurrentPosition() +
                " BL" + backLeft.getCurrentPosition() + " BR" + backRight.getCurrentPosition() +  "\tX: " + position[0] + "Y: " + position[1];
    }

    public void initEnc() {
        setModeToAllDriveMotors(DcMotor.RunMode.RUN_USING_ENCODER);
        setModeToAllDriveMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setTargetPosition(frontLeft.getCurrentPosition());
        frontRight.setTargetPosition(frontRight.getCurrentPosition());
        backLeft.setTargetPosition(backLeft.getCurrentPosition());
        backRight.setTargetPosition(backRight.getCurrentPosition());

        setModeToAllDriveMotors(DcMotor.RunMode.RUN_TO_POSITION);
        setZeroPowerBehaviorToAllDriveMotors(DcMotor.ZeroPowerBehavior.BRAKE);
        setPowerToAllDriveMotors(0);
    }

    public void setPowerToAllDriveMotors(double powerForAll) {
        frontLeft.setPower(powerForAll);
        frontRight.setPower(powerForAll);
        backLeft.setPower(powerForAll);
        backRight.setPower(powerForAll);
    }

    public void setModeToAllDriveMotors(DcMotor.RunMode runModeForAll) {
        frontLeft.setMode(runModeForAll);
        frontRight.setMode(runModeForAll);
        backLeft.setMode(runModeForAll);
        backRight.setMode(runModeForAll);
    }

    public void setZeroPowerBehaviorToAllDriveMotors(DcMotor.ZeroPowerBehavior zeroPowerBehaviorForAll) {
        frontLeft.setZeroPowerBehavior(zeroPowerBehaviorForAll);
        frontRight.setZeroPowerBehavior(zeroPowerBehaviorForAll);
        backLeft.setZeroPowerBehavior(zeroPowerBehaviorForAll);
        backRight.setZeroPowerBehavior(zeroPowerBehaviorForAll);
    }

    public double abs(double num) {
        return Math.abs(num);
    }

    public int max(int num1, int num2) {
        if (num1 > num2) return num1;
        return num2;
    }

    public void stop() {
        setPowerToAllDriveMotors(0);
    }
}

