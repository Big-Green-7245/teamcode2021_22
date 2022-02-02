package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.*;
import org.firstinspires.ftc.teamcode.util.*;

public class Arm
{
    public DcMotor Lever;
    public Servo BoxJoint;
    public CRServo BoxFinger;
    private ElapsedTime runtime = new ElapsedTime();

    public boolean isAtFetchPos = true;

    private final double SERVO_FETCH_POS = 0.6;
    private final double SERVO_DROP_POS = 0.4;

    public Arm(HardwareMap hwMap)
    {
        BoxJoint = hwMap.get(Servo.class, "boxJoint");
        BoxFinger = hwMap.get(CRServo.class, "boxFinger");
        BoxFinger.setDirection(DcMotorSimple.Direction.FORWARD);

        setBoxFetch(isAtFetchPos);

        Lever = hwMap.get(DcMotor.class, "arm");
        Lever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void MoveArm(double power)
    {
        Lever.setPower(power);
    }

    public void moveArmDist(int timeout) {
        runtime.reset();
        Lever.setTargetPosition(Lever.getCurrentPosition() - 700);
        Lever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Lever.setPower(0.75);
        while(Lever.isBusy() && runtime.seconds() < timeout) {}
        Lever.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void deposit(int timeout) {
        Lever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        runtime.reset();
        Lever.setTargetPosition(Lever.getCurrentPosition() - 1200);
        Lever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Lever.setPower(0.7);
        while(Lever.isBusy() && runtime.seconds() < timeout) {}
        Lever.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        runtime.reset();
        SpinFinger(-1.0);
        while(runtime.seconds() < 2) {}
        Lever.setTargetPosition(Lever.getCurrentPosition() + 1200);
        Lever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Lever.setPower(0.8);
        while(Lever.isBusy() && runtime.seconds() < timeout) {}
        Lever.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setBoxPos(double pos) {
        BoxJoint.setPosition(pos);
    }
    public void setBoxFetch(boolean state) {
        if(state) BoxJoint.setPosition(SERVO_FETCH_POS);
        else BoxJoint.setPosition(SERVO_DROP_POS);
    }

    public void toggleBoxStatus() {
        isAtFetchPos = !isAtFetchPos;
        setBoxFetch(isAtFetchPos);
    }

    public void SpinFinger(double power)
    {
        BoxFinger.setPower(power);
    }
}
