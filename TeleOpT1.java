package org.firstinspires.ftc.teamcode;

// Standard Lib
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.lang.reflect.Method;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import java.util.*;

// Telemetry Wrapper
import org.firstinspires.ftc.teamcode.util.*;
import org.firstinspires.ftc.teamcode.modules.*;

@TeleOp(name="TeleOpT1", group="opmode")
//@Disabled
public class TeleOpT1 extends LinearOpMode {
    // Attributes
    final String programVer = "1.2";
    final double speedMultiplier = 0.7;

    private Advanced_Auto_Drive_Control badStuff = new Advanced_Auto_Drive_Control();

    private boolean isAtFetch = true;
    private boolean isSucking = false;

    private double suckPower = 1.0;

    private boolean turning;

    @Override
    public void runOpMode(){
        // Robot modules | components declaration
        Nav nav = new Nav();
        Arm arm = new Arm(hardwareMap);

        Advanced_Auto_Drive_Control driveTrain = new Advanced_Auto_Drive_Control();
        ButtonHelper gp1 = new ButtonHelper(gamepad1);
        ButtonHelper gp2 = new ButtonHelper(gamepad2);
        double initialDirection = 0;

        // Robot modules | components initialization
        nav.init(hardwareMap);
        driveTrain.init(hardwareMap, 0, 0);
        //driveTrain.initEnc();

        TelemetryWrapper.init(telemetry, 12);

        TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer + "\t Press start to start >");
        waitForStart();

        double[] magInitial = nav.getMagneticFieldDirection();

        while(opModeIsActive()) {
            // Update buttonhelper
            gp1.update();
            gp2.update();

            // Mecanum wheels
            driveTrain.Move((gamepad1.left_stick_x), (gamepad1.left_stick_y), (gamepad1.right_stick_x), speedMultiplier);

            if(gp2.pressing(gp2.x)) isAtFetch = ! isAtFetch;
            if(gp2.pressing(gp2.y)) isSucking = ! isSucking;
            if(gp2.pressing(gp2.b)) suckPower = ((suckPower == 1.0) ? -1.0 : 1.0);

            if(isAtFetch) {
                suckPower = 1.0;
            }
            arm.setBoxFetch(isAtFetch);

            if(isSucking) arm.SpinFinger(suckPower);
            else arm.SpinFinger(0);

            arm.MoveArm(gamepad2.left_stick_y);

//            if(gamepad1.x && !turning)
//            {
//                turning = true;
//                initialDirection = nav.getOrientation()[0];
//            }
//
//            if (turning)
//            {
//                if (Math.abs(initialDirection - nav.getOrientation()[0]) < 90)
//                {
//                    turning = true;
//                    driveTrain.move(0, 0, 0.8, 1);
//                }else{
//                    turning = false;
//                }
//            }
//
//            if (gamepad1.y)
//            {
//                badStuff.Translate(12, 0.3);
//                hardwareMap.get(DcMotor.class, "backLeft").setPower(1.0);
//                hardwareMap.get(DcMotor.class, "frontLeft").setPower(1.0);
//                hardwareMap.get(DcMotor.class, "backRight").setPower(1.0);
//                hardwareMap.get(DcMotor.class, "frontRight").setPower(1.0);
//            }
//            if(gp1.pressing(gp1.a)) nav.resetOrientationRef();
//            if(gp1.pressing(gp1.b)) {
//
//
//            }

            // Add data for orientation
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);
            TelemetryWrapper.setLine(2, "Magnetic Fields " + Arrays.toString(nav.getMagneticFieldDirection()));
            TelemetryWrapper.setLine(3, "Gyro Orientation " + Arrays.toString(nav.getOrientation()));
            TelemetryWrapper.setLine(4, "Joystick (X, Y, R) " + gamepad1.left_stick_x + " " + gamepad1.left_stick_y + " " + gamepad1.right_stick_x);
        }

    }
}
