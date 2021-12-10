//Note one motor for extension vertically

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

//This is the interface for the arm
//using one motor for extension (up) and (down)
// - Ros 10/5/21

public class Arm {

  private DcMotor armMotor;

  public Arm(DcMotor inputMotor){
    armMotor = inputMotor;
  }

// make the arm rise
  private void rise(){
    armMotor.setDirection(DcMotor.Direction.FORWARD);
    armMotor.setPower(1);
  }

  //make the arm lower
  private void lower(){
    armMotor.setDirection(DcMotor.Direction.REVERSE);
    armMotor.setPower(1);
  }
}
