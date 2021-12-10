package org.firstinspires.ftc.teamcode;

// Standard Lib
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@TeleOp(name="TeleOpP1", group="opmode")
//@Disabled
public class TeleOpP1 extends LinearOpMode {
    // Attributes
    final String programVer = "1.0";

    @Override
    public void runOpMode(){
        // Robot modules | components declaration
        Nav nav = new Nav();
        DriveTrain driveTrain = new DriveTrain();

        // Robot modules | components declaration
        nav.init(hardwareMap);
        driveTrain.init(hardwareMap);
                
        TelemetryWrapper.init(telemetry, 12);

        TelemetryWrapper.setLine(1, "TeleOpP1 v" + programVer + "\t Press start to start >");
        waitForStart();

        nav.updateSensorInfo();
        double[] magInitial = nav.getMagneticFieldDirection();

        while(opModeIsActive()) {
            nav.updateSensorInfo();
            // Mecanum wheels
            // backLeft.setPower((gamepad1.left_stick_y + gamepad1.left_stick_x + (gamepad1.right_stick_x)));
            // backRight.setPower((gamepad1.left_stick_y - gamepad1.left_stick_x - (gamepad1.right_stick_x)));
            // frontLeft.setPower((gamepad1.left_stick_y + gamepad1.left_stick_x - (gamepad1.right_stick_x)));
            // frontRight.setPower((gamepad1.left_stick_y - gamepad1.left_stick_x + (gamepad1.right_stick_x)));
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            // Add data for orientation
            TelemetryWrapper.setLine(1, "TeleOpP1 v" + programVer);
            TelemetryWrapper.setLine(2, "Magnetic Fields " + Arrays.toString(nav.getMagneticFieldDirection()));
            TelemetryWrapper.setLine(3, "Gyro Orientation " + Arrays.toString(nav.getOrientation()));
        }

    }
}
