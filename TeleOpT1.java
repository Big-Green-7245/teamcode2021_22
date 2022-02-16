package org.firstinspires.ftc.teamcode;

// Standard Lib
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    private DriveTrain badStuff = new DriveTrain();

    private boolean isAtFetch = true;
    private boolean isSucking = false;

    private double suckPower = 1.0;

    private boolean turning;

    @Override
    public void runOpMode(){
        // Robot modules | components declaration
        Nav nav = new Nav();
        Arm arm = new Arm(hardwareMap);
        DcMotor carousel = hardwareMap.get(DcMotor.class, "carousel");

        DriveTrain driveTrain = new DriveTrain();
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
            driveTrain.move((gamepad1.left_stick_x), (gamepad1.left_stick_y), (gamepad1.right_stick_x), speedMultiplier);

            if(gp2.pressing(gp2.dpad_up)) {
                arm.setBoxPos(arm.boxJoint.getPosition() + 0.05);
            } else if(gp2.pressing(gp2.dpad_down)) {
                arm.setBoxPos(arm.boxJoint.getPosition() - 0.05);
            }

            if(gp2.pressing(gp2.x)) {
                isAtFetch = ! isAtFetch;
                suckPower = 1.0;
            }
            if(gp2.pressing(gp2.y)) isSucking = ! isSucking;
            if(gp2.pressing(gp2.b)) suckPower = suckPower == 1.0 ? -1.0 : 1.0;

            if(gp2.pressing(gp2.left_bumper)) {
                if(Math.abs(carousel.getPower()) < 0.1) carousel.setPower(0.5);
                else carousel.setPower(0);
            }
            else if(gp2.pressing(gp2.right_bumper)) {
                if(Math.abs(carousel.getPower()) < 0.1) carousel.setPower(-0.5);
                else carousel.setPower(0);
            }

            if(gp2.pressing(gp2.dpad_left)) arm.deposit(1100, 500, 10);

            if(gp1.pressing(gp1.dpad_left)) driveTrain.translate(0.5, 10, 0, 0, 100);
            if(gp1.pressing(gp1.dpad_right)) driveTrain.translate(0.5, 0, 10, 0, 100);

            arm.setBoxFetch(isAtFetch);

            if(isSucking) arm.setSuctionPower(suckPower);
            else arm.setSuctionPower(0);

            arm.rotateArm(gamepad2.left_stick_y);

            // Add data for orientation
            TelemetryWrapper.setLine(1, "TeleOpT1 v" + programVer);
            TelemetryWrapper.setLine(2, "Magnetic Fields " + Arrays.toString(nav.getMagneticFieldDirection()));
            TelemetryWrapper.setLine(3, "Gyro Orientation " + Arrays.toString(nav.getOrientation()));
            TelemetryWrapper.setLine(4, "Joystick (X, Y, R) " + gamepad1.left_stick_x + " " + gamepad1.left_stick_y + " " + gamepad1.right_stick_x);
            TelemetryWrapper.setLine(5, "isSucking=" + isSucking);
            TelemetryWrapper.setLine(6, "isAtFetch=" + isAtFetch);
            TelemetryWrapper.setLine(7, "suckPowerStatus = " + arm.boxSuction.getPower());
            TelemetryWrapper.setLine(8, "boxPosStatus = " + arm.boxJoint.getPosition());
        }

    }
}
