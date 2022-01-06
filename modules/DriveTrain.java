package org.firstinspires.ftc.teamcode.modules;


import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.util.Range;


public class DriveTrain {
    HardwareMap hwMap = null;

    DcMotor backLeft;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor frontRight;
    
    double speedx;
    double speedy;
    double offset;

    /**
     * 
     * Initializes module with a hardwareMap from robot
     * @param map hardware map of current robot
     * 
     */
    public void init(HardwareMap map) {
        hwMap = map;

        backLeft = hwMap.get(DcMotor.class, "backLeft");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backRight = hwMap.get(DcMotor.class, "backRight");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft = hwMap.get(DcMotor.class, "frontLeft");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight = hwMap.get(DcMotor.class, "frontRight");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    /**
     * 
     * Moves the robot with a certain power in the x, y direction and a turning
     * power for turning the robot's orientation
     * 
     */
    public void move(double powerx, double powery, double turn, double factor) {
        speedx = factor * powerx;
        speedy = factor * powery;
        offset = factor * turn;

        frontLeft.setPower(Range.clip(speedy-speedx+offset,-1,1));
        frontRight.setPower(Range.clip(speedy+speedx-offset,-1,1));
        backLeft.setPower(Range.clip(speedy+speedx+offset,-1,1));
        backRight.setPower(Range.clip(speedy-speedx-offset,-1,1));
    }


    /**
     * 
     * Stops the motion of the robot
     * 
     */
    public void stop() {
        move(0, 0, 0, 0);
    }
}