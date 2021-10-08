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

//Definition of Roller Mechanism
//For reusability in auto + TeleOp - Ros 10/5/21

public class Roller{

  private DcMotor rollerMotor; //This motor spins the carousel

  public Roller(DcMotor inputMotor){
    rollerMotor = inputMotor;
  }

//this will activate the roller motor
  private void spin(int input){ // 0 is clockwise, 1 is counter

  switch (input) {
            case 0:  rollerMotor.setDirection(DcMotor.Direction.REVERSE);
                     rollerMotor.setPower(1);
                     break;
            case 2:  rollerMotor.setDirection(DcMotor.Direction.FORWARD);
                     rollerMotor.setPower(1);
                     break;
            default:
                      rollerMotor.setPower(0);
        }
  }
}
