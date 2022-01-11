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

@TeleOp(name="TeleOpCalibrate", group="opmode")
//@Disabled
public class TeleOpCalibrate extends LinearOpMode {
    // Attributes
    final String programVer = "1.0";

    double[] lastEncPos = {0, 0, 0, 0};
    double lastAng = 0;

    @Override
    public void runOpMode(){

        // Robot modules | components declaration
        Nav nav = new Nav();
        DriveTrain driveTrain = new DriveTrain();
        ButtonHelper gp1 = new ButtonHelper(gamepad1);
        double initialDirection = 0;
        // Robot modules | components initialization
        nav.init(hardwareMap);
        driveTrain.init(hardwareMap);
                
        TelemetryWrapper.init(telemetry, 12);

        TelemetryWrapper.setLine(1, "TeleOpCalibrate v" + programVer + "\t Press start to start >");
        waitForStart();

        double[] magInitial = nav.getMagneticFieldDirection();

        while(opModeIsActive()) {
            // Update buttonhelper
            gp1.update();
            
            // Mecanum wheels
            driveTrain.move(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, 1.0);
            

            // Calibrate manually for forward/backward translation
            if(gp1.pressing(gp1.a)) nav.resetOrientationRef();

            // Calibrate manually for leftward/rightward translation
            if(gp1.pressing(gp1.b)) {
                
            }

            // Calibrate automatically for rotation (ticks/rev)
            if(gp1.pressing(gp1.x)) {
                lastEncPos = driveTrain.getEncPos();
                lastAng = nav.getOrientation()[0];

                TelemetryWrapper.setLine(1, "Calibrating Turning Angle...");

                driveTrain.move(0, 0, -0.1, 1);
                while(nav.getOrientation()[0] - lastAng < 90) {}
                driveTrain.move(0, 0, 0, 1);
                double[] dEnc = driveTrain.getEncPos();
                for (int i = 0; i < dEnc.length; i++) {
                    dEnc[i] = dEnc[i] - lastEncPos[i];
                }
                double dAng = nav.getOrientation()[0] - lastAng;
        
                double sum = 0;
                for(double num:dEnc) sum += Math.abs(num);
                sum /= dEnc.length;
                
                TelemetryWrapper.setLine(2, "dEnc " + Arrays.toString(dEnc));
                TelemetryWrapper.setLine(3, "dAng " + dAng);
                TelemetryWrapper.setLine(6, "Rot Result: " + (sum / dAng * 360 + "ticks/rev"));
                TelemetryWrapper.setLine(5, "Press Start Up to Exit >");
                while(!gp1.pressing(gp1.x)) {}
            }
            
            // Add data for orientation
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);
            TelemetryWrapper.setLine(2, "Magnetic Fields " + Arrays.toString(nav.getMagneticFieldDirection()));
            TelemetryWrapper.setLine(3, "Gyro Orientation " + Arrays.toString(nav.getOrientation()));
            TelemetryWrapper.setLine(4, "Joystick (X, Y, R) " + gamepad1.left_stick_x + " " + gamepad1.left_stick_y + " " + gamepad1.right_stick_x);
            TelemetryWrapper.setLine(5, "Enc Pos " + driveTrain.getEncPosStr());
        }

    }
}
