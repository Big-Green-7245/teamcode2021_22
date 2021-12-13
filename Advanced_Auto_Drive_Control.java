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

public class Advanced_Auto_Drive_Control
{
    private ElapsedTime runtime = new ElapsedTime();
    //    static final double METRIC_CONVERT = 1/8;
    static final double COUNTS_PER_MOTOR_REV = 180;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 3.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    static final double DRIFT_VARIABLE = 1.6;
    Map MainMap = new Map(144, 144);
    Nav direction = new Nav();
  direction.init(hardwareMap);
    DriveTrain driveTrain = new DriveTrain();
  driveTrain.init(hardwareMap);
  direction.init(hardwareMap);
    public double[] StartLocation = new double[]{0, 0};

    public void MoveToLocation(double[] location)
    {

    }

    public void FollowPath()
    {

    }

    public void SetBearing(double bearing)
    {
        while (bearing != direction.getOrientation()[0])
        {
            driveTrain.move(0, 0, 1.0);
        }

    }

    public void Translate()
    {

    }

    public void GetPosition()
    {

    }
}