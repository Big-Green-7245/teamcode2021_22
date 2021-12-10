package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// This interface is for the claw NOT THE arm
// essentially activate the grappling
// - Ros 10/5/21
public class Claw {

private CRServo jointServo;

public Claw(CRServo inputServo){

  jointServo = inputServo;
}

//this will close the gripper
private void grappleClose(){
  jointServo.setDirection(servo.Direction.FORWARD);
  jointServo.setPower(1);
}

//this will open the gripper
private void grappleOpen(){
  jointServo.setDirection(servo.Direction.REVERSE);
  jointServo.setPower(1);
}

}
