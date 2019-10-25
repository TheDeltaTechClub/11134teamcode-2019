package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="BasicTeleOp")
public class SimpleTeleop extends OpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;

    DcMotor liftMotor;
    DcMotor extMotor;

    Servo clampServo;
    //DcMotor spinMotor;
    CRServo spinServo;

    // double power = 0.1;

    public void SimpleTeleop() {

    }

    @Override
    public void init() {
        leftDrive = hardwareMap.dcMotor.get("leftMotor");
        rightDrive = hardwareMap.dcMotor.get("rightMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        extMotor = hardwareMap.dcMotor.get("extMotor");
        clampServo = hardwareMap.servo.get("clampServo");
        //spinMotor = hardwareMap.dcMotor.get("spinMotor");
        spinServo = hardwareMap.crservo.get("spinServo");

       // claw = hardwareMap.servo.get("claw");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        extMotor.setDirection(DcMotor.Direction.REVERSE);
        spinServo.setDirection(CRServo.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.addData("Init finished.", "");
        telemetry.update();
    }

    boolean moveExtDirection = false;
    boolean moveLiftDirection = false;

    boolean lastKeyDownR = false;
    boolean lastKeyDownL = false;
    boolean lastKeyDownX = false;

    boolean isClamped = false;

    @Override
    public void loop() {
        float left = gamepad1.left_stick_y;
        float right = gamepad1.right_stick_y;
        float moveLift = moveLiftDirection ? gamepad1.left_trigger : -gamepad1.left_trigger;
        float moveExt = moveExtDirection ? gamepad1.right_trigger : -gamepad1.right_trigger;
        //float rotateClaw = gamepad1.dpad_left ? 0.5f : gamepad1.dpad_right ? -0.5f : 0f;

        left = Range.clip(left, -1, 1);
        right = Range.clip(right, -1, 1);
        moveExt = Range.clip(moveExt, -1, 1);
        moveLift = Range.clip(moveLift, -1, 1);
        //rotateClaw = Range.clip(rotateClaw, -1, 1);

        leftDrive.setPower(left);
        rightDrive.setPower(right);
        liftMotor.setPower(moveLift);
        extMotor.setPower(moveExt);
        //spinMotor.setPower(rotateClaw);

        boolean extKeyDown = gamepad1.right_bumper;
        boolean liftKeyDown = gamepad1.left_bumper;
        boolean dpadLeftDown = gamepad1.dpad_left;
        boolean dpadRightDown = gamepad1.dpad_right;
        boolean clampKeyDown = gamepad1.x;

        if (!lastKeyDownL && extKeyDown) {
            lastKeyDownL = true;
            moveExtDirection = !moveExtDirection;
        }

        if (lastKeyDownL && !extKeyDown) {
            lastKeyDownL = false;
        }

        if (!lastKeyDownR && liftKeyDown) {
            lastKeyDownR = true;
            moveLiftDirection = !moveLiftDirection;
        }

        if (lastKeyDownR && !liftKeyDown) {
            lastKeyDownR = false;
        }

        if (dpadLeftDown) {
            spinServo.setPower(0.2f);
        } else if (dpadRightDown) {
            spinServo.setPower(-0.2f);
        } else {
            spinServo.setPower(0);
        }

        if (!lastKeyDownX && clampKeyDown) {
            lastKeyDownX = true;
            isClamped = !isClamped;
            clampServo.setPosition(isClamped ? 0 : 1);
        }

        if (lastKeyDownX && !clampKeyDown) {
            lastKeyDownX = false;
        }
    }

    @Override
    public void stop() {

    }
}