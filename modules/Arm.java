package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Arm
{
    public DcMotor lever;
    public Servo boxJoint;
    public CRServo boxSuction;
    private ElapsedTime runtime = new ElapsedTime();

    public boolean isAtFetchPos = true;

    private final double SERVO_FETCH_POS = 0.6;
    private final double SERVO_DROP_POS = 0.4;

    public Arm(HardwareMap hwMap)
    {
        boxJoint = hwMap.get(Servo.class, "boxJoint");
        boxSuction = hwMap.get(CRServo.class, "boxFinger");
        boxSuction.setDirection(DcMotorSimple.Direction.FORWARD);

        setBoxFetch(isAtFetchPos);

        lever = hwMap.get(DcMotor.class, "arm");
        lever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void rotateArm(double power)
    {
        lever.setPower(power);
    }

    public void deposit(int rotTicks, int slowTicks, int timeout) {
        lever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        runtime.reset();
        lever.setTargetPosition(lever.getCurrentPosition() - (rotTicks - slowTicks));
        lever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lever.setPower(0.7);
        while(lever.isBusy() && runtime.seconds() < timeout) {}
        lever.setTargetPosition(lever.getCurrentPosition() - slowTicks);
        lever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lever.setPower(0.4);
        while(lever.isBusy() && runtime.seconds() < timeout) {}
        lever.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        runtime.reset();
        setSuctionPower(-1.0);
        while(runtime.seconds() < 2) {}
        lever.setTargetPosition(lever.getCurrentPosition() + (rotTicks - slowTicks));
        lever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lever.setPower(0.8);
        while(lever.isBusy() && runtime.seconds() < timeout) {}
        lever.setTargetPosition(lever.getCurrentPosition() + slowTicks);
        lever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lever.setPower(0.4);
        while(lever.isBusy() && runtime.seconds() < timeout) {}
        lever.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void deposit(int timeout) {
        lever.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        runtime.reset();
        lever.setTargetPosition(lever.getCurrentPosition() - 1200);
        lever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lever.setPower(0.7);
        while(lever.isBusy() && runtime.seconds() < timeout) {}
        lever.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        runtime.reset();
        setSuctionPower(-1.0);
        while(runtime.seconds() < 2) {}
        lever.setTargetPosition(lever.getCurrentPosition() + 1200);
        lever.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lever.setPower(0.8);
        while(lever.isBusy() && runtime.seconds() < timeout) {}
        lever.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setBoxPos(double pos) {
        boxJoint.setPosition(pos);
    }
    public void setBoxFetch(boolean state) {
        if(state) boxJoint.setPosition(SERVO_FETCH_POS);
        else boxJoint.setPosition(SERVO_DROP_POS);
    }

    public void setSuctionPower(double power)
    {
        boxSuction.setPower(power);
    }
}
