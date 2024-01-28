package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name = "Red Back", group = "CenterStage", preselectTeleOp = "Full")
public class Auto_RedBackFull extends CSBase {
    @Override
    public void runOpMode() {
        color teamColor = color.r;
        setup(teamColor);

        // ---------------------
        // ------Main Code------
        // ---------------------


        pos = findPos();
        int ID = setID(pos, teamColor);
        telemetry.addData("Team Prop X", x);
        telemetry.addData("Team Prop Position", pos);
        telemetry.update();
        /*
        purplePixel();
        drive(2);
        turn(-90);
        drive(tilesToInches(-1));
        turn(-90); //*/
        drive(tilesToInches(-1));
        turn(90);
        setSpeed(1000);
        align(ID);
        drive(-12);
        strafe(tilesToInches(1), dir.r);
        drive(-18);

        //*/

        s(1);  // Pause to display final telemetry message.
    }
}