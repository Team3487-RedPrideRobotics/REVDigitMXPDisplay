// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final class DriveConstants { // The constants for subsytems/Drive.java
        public static int[] LEFT_DRIVE_SPARKS = {3, 2};
        public static int[] RIGHT_DRIVE_SPARKS = {0, 1};
        
    }

    public static final class OuttakeConstants { // The constants for subsystems/Outtake.java
        public static int SHOOT_BOTTOM_SPARK = 3;
        public static int SHOOT_TOP_SPARK = 4;
        public static int AIMER_SPARK = 5;
        
    }

    public static final class IntakeConstants { // The constants for subsystems/Intake.java
        public static int INTAKE_FRONT_SPARK = 7;
        public static int INTAKE_BACK_SPARK = 4;
        
    }

    public static final class DriveEdits { // Editable Items in Drive.java
        public static double DRIVE_SPEED = 1;
        public static boolean LEFT_DRIVE_REVERSE = true;
        public static boolean RIGHT_DRIVE_REVERSE = false;
    }

    public static final class OuttakeEdits { // Editable Items in Outtake.java
        public static double MANIPULATOR_HOLD_MULTIPLIER = 2.15;
        public static boolean SHOOT_TOP_REVERSE = true;
        public static boolean SHOOT_BOTTOM_REVERSE = true;
        public static boolean MONO_SHOOT = true;

        public static double SHOOT_TOP_SPEED = 1; // Top would always be main
        public static double SHOOT_BOTTOM_SPEED = 1; 

        
    }

    public static final class IntakeEdits {
        public static boolean INTAKE_FRONT_REVERSE = true;
        public static boolean INTAKE_BACK_REVERSE = true;
        public static boolean MONO_SHOOT = true;

        public static double INTAKE_FRONT_SPEED = 1; // Front would always be main
        public static double INTAKE_BACK_SPEED = 1;
    }

    public static final class Controller { // fun controller stuff
        public static double DEAD_ZONE = 0.1;
        public static boolean SAFETY = false;
    }
}
