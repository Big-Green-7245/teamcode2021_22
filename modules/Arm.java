package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.modules.*;
import org.firstinspires.ftc.teamcode.util.*;

public class Arm
{
    public DcMotor Lever;
    public Servo BoxJoint;
    public CRServo BoxFinger;
    private boolean isDropped = true;
    
    private final double SERVO_FETCH_POS = 0.6;
    private final double SERVO_DROP_POS = 0.4;
    
    public boolean isAtFetchPos = true;
    
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
    
    public void TiltBox(double power)
    {
        //BoxJoint.setPower(power);
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
