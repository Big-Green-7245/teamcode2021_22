package org.firstinspires.ftc.teamcode;

// Standard Lib
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;


import java.text.DecimalFormat;
import java.util.*;

// Telemetry Wrapper
import org.firstinspires.ftc.teamcode.util.*;
import org.firstinspires.ftc.teamcode.modules.*;

@TeleOp(name="TeleOpTest", group="opmode")
//@Disabled
public class TeleOpTest extends LinearOpMode {
    // Attributes
    final String programVer = "1.2";
    final double speedMultiplier = 0.7;

    private DriveTrain badStuff = new DriveTrain();
    DecimalFormat nF = new DecimalFormat("#.00");

    private boolean isAtFetch = true;
    private boolean isSucking = false;

    private double suckPower = 1.0;

    private boolean turning;

    DcMotor backLeft;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor frontRight;

    @Override
    public void runOpMode(){
        backLeft = hardwareMap.get(DcMotor.class, "leftBack");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backRight = hardwareMap.get(DcMotor.class, "rightBack");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight = hardwareMap.get(DcMotor.class, "rightFront");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        TelemetryWrapper.init(telemetry, 12);

        waitForStart();

        while(opModeIsActive()) {
            // move((gamepad1.left_stick_x), (gamepad1.left_stick_y), (gamepad1.right_stick_x), speedMultiplier);
            frontLeft.setPower(gamepad1.left_stick_y);
            backLeft.setPower(gamepad1.left_stick_x);
            frontRight.setPower(gamepad1.right_stick_y);
            backRight.setPower(gamepad1.right_stick_x);
        }

    }

    public void move(double powerx, double powery, double turn, double factor) {
        double speedx = factor * powerx;
        double speedy = factor * powery;
        double offset = factor * turn;

        TelemetryWrapper.setLine(7, "move-param: " + nF.format(speedy-speedx-offset) + ":" + nF.format(speedy+speedx+offset) + ":" + nF.format(speedy+speedx-offset) + ":" + nF.format(speedy-speedx+offset));

        frontLeft.setPower(Range.clip(speedy-speedx-offset,-1,1));
        frontRight.setPower(Range.clip(speedy+speedx+offset,-1,1));
        backLeft.setPower(Range.clip(speedy+speedx-offset,-1,1));
        backRight.setPower(Range.clip(speedy-speedx+offset,-1,1));
    }
}
