package org.firstinspires.ftc.teamcode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

@Autonomous(name="PowerPlayAuton", group="Linear OpMode")
public class PowerPlayAuton extends LinearOpMode {

    private RobotManager robotManager;
    private ElapsedTime elapsedTime = new ElapsedTime();

    @Override
    public void runOpMode() {
        initSharedPreferences();
        //Problem: path is currently not set to the desired path in autonomous paths!
        robotManager = new RobotManager(hardwareMap, gamepad1, gamepad2, PowerPlayAuton.navigationPath,
                PowerPlayAuton.allianceColor, PowerPlayAuton.startingSide,
                PowerPlayAuton.movementMode, telemetry, elapsedTime);

        IMUPositioning.Initialize(this);

        /*
        robotManager.computerVision.startStreaming();

        Robot.SlidesState hubLevel;
        do {
            hubLevel = robotManager.readBarcode();
        }
        while (!isStarted());

//        hubLevel = Robot.SlidesState.L3;

        robotManager.computerVision.stopStreaming();

         */

        while (!isStarted());

//        waitForStart(); // Wait for the play button to be pressed

        double startTime = robotManager.elapsedTime.milliseconds();
        while (robotManager.elapsedTime.milliseconds() - startTime < waitTime * 1000) {}

        Position lastPOI;
        while ((lastPOI = robotManager.travelToNextPOI()) != null) {
            telemetry.update();
            telemetry.addData("POI", lastPOI.name);
            telemetry.addData("Action", lastPOI.action.name());
            telemetry.update();
            switch (lastPOI.action) {
                case PICK_UP_CONE:
                    robotManager.pickUpCone(); //Robot should move forward a bit so it can put the cone into the intake
                    break;
                case DELIVER_CONE_LOW:
                    robotManager.deliverToPole(Robot.SlidesState.LOW, robotManager.robot);
                    break;
                case DELIVER_CONE_MEDIUM:
                    robotManager.deliverToPole(Robot.SlidesState.MEDIUM, robotManager.robot);
                    break;
                case DELIVER_CONE_HIGH:
                    robotManager.deliverToPole(Robot.SlidesState.HIGH, robotManager.robot);
                    break;
            }
        }

//        if (navigationMode == RobotManager.NavigationMode.DUCK_CAROUSEL || navigationMode == RobotManager.NavigationMode.DUCK_WAREHOUSE) {
//            robotManager.travelToNextPOI();  // Go to carousel.
//            robotManager.deliverDuck();
//            robotManager.travelToNextPOI();  // Park in alliance storage unit.
//        }
//        else {
//            robotManager.travelToNextPOI();  // Park in warehouse.
//        }

        while (opModeIsActive()) {}
    }

    // ANDROID SHARED PREFERENCES
    // ==========================

    // Adapted from https://github.com/ver09934/twentytwenty/blob/ian-dev/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/SkystoneAuton.java

    private static SharedPreferences sharedPrefs;

    private static int waitTime = 0;
    private static Navigation.MovementMode movementMode;
    private static RobotManager.StartingSide startingSide;
    private static RobotManager.AllianceColor allianceColor;
    private static ArrayList<Position> navigationPath;

    public void initSharedPreferences() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.hardwareMap.appContext);

        String movementMode = sharedPrefs.getString("movement_mode", "ERROR");
        String waitTime = sharedPrefs.getString("wait_time", "ERROR");
        String startingSide = sharedPrefs.getString("starting_side", "ERROR");
        String allianceColor = sharedPrefs.getString("alliance_color", "ERROR");
        String autonMode = sharedPrefs.getString("auton_type", "ERROR");

        telemetry.addData("Movement mode", movementMode);
        telemetry.addData("Wait time", waitTime);
        telemetry.addData("Auton mode", autonMode);
        telemetry.addData("Starting side", startingSide);
        telemetry.addData("Alliance color", allianceColor);

        switch (movementMode) {
            case "STRAFE":
                PowerPlayAuton.movementMode = Navigation.MovementMode.STRAFE;
                break;
            case "FORWARD_ONLY":
                PowerPlayAuton.movementMode = Navigation.MovementMode.FORWARD_ONLY;
                break;
        }

        switch (waitTime) {
            case "0_SECONDS":
                PowerPlayAuton.waitTime = 0;
                break;
            case "5_SECONDS":
                PowerPlayAuton.waitTime = 5;
                break;
            case "10_SECONDS":
                PowerPlayAuton.waitTime = 10;
                break;
            case "15_SECONDS":
                PowerPlayAuton.waitTime = 15;
                break;
            case "20_SECONDS":
                PowerPlayAuton.waitTime = 20;
                break;
        }

        switch(startingSide) {
            case "THEIR_COLOR":
                PowerPlayAuton.startingSide = RobotManager.StartingSide.THEIR_COLOR;
                break;

            case "OUR_COLOR":
                PowerPlayAuton.startingSide = RobotManager.StartingSide.OUR_COLOR;
                break;
        }

        if (allianceColor.equals("BLUE")) {
            PowerPlayAuton.allianceColor = RobotManager.AllianceColor.BLUE;
        }
        else if (allianceColor.equals("RED")) {
            PowerPlayAuton.allianceColor = RobotManager.AllianceColor.RED;
        }

        switch (autonMode) {
            case "MEDIUM_LARGE":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.MEDIUM_LARGE.clone();
                break;
            case "SMALL_LARGE":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.SMALL_LARGE.clone();
                break;
            case "SMALL_MEDIUM":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.SMALL_MEDIUM.clone();
                break;
            case "SMALL_SMALL":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.SMALL_SMALL.clone();
                break;
            case "LARGE_LARGE":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.LARGE_LARGE.clone();
                break;
        }
/*
        switch (autonMode) {
            case "PARK_ASU":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PARK_ASU.clone();
                break;
            case "PRELOAD_BOX":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PRELOAD_BOX.clone();
                break;
            case "CAROUSEL":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.CAROUSEL.clone();
                break;
            case "PRELOAD_BOX_AND_PARK_ASU":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PRELOAD_BOX_AND_PARK_ASU.clone();
                break;
            case "CAROUSEL_AND_PRELOAD_BOX":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.CAROUSEL_AND_PRELOAD_BOX.clone();
                break;
            case "PRELOAD_BOX_AND_CAROUSEL":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PRELOAD_BOX_AND_CAROUSEL.clone();
                break;
            case "CAROUSEL_PRELOAD_BOX_AND_PARK_ASU":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.CAROUSEL_PRELOAD_BOX_AND_PARK_ASU.clone();
                break;
            case "PRELOAD_BOX_CAROUSEL_AND_PARK_ASU":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PRELOAD_BOX_CAROUSEL_AND_PARK_ASU.clone();
                break;
            case "CAROUSEL_AND_PARK_ASU":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.CAROUSEL_AND_PARK_ASU.clone();
                break;
            case "PARK_WAREHOUSE":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PARK_WAREHOUSE.clone();
                break;
            case "PRELOAD_BOX_AND_PARK_WAREHOUSE":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PRELOAD_BOX_AND_PARK_WAREHOUSE.clone();
                break;
            case "CAROUSEL_AND_PARK_WAREHOUSE":
                PowerPlayAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.CAROUSEL_AND_PARK_WAREHOUSE.clone();
                break;

//            case "PRELOAD_BOX_ONLY":
//                FreightFrenzyAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PRELOAD_BOX_ONLY.clone();
//                break;
//            case "PRELOAD_BOX_AND_PARK":
//                FreightFrenzyAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PRELOAD_BOX_AND_PARK.clone();
//                break;
//            case "PARK":
//                FreightFrenzyAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.PARK_STORAGE_UNIT.clone();
//                break;
//            case "MOVE_STRAIGHT":
//                FreightFrenzyAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.MOVE_STRAIGHT.clone();
//                break;
//            case "ROTATE_180":
//                FreightFrenzyAuton.navigationPath = (ArrayList<Position>) AutonomousPaths.ROTATE_180.clone();
//                break;
        }
        */

    }
}