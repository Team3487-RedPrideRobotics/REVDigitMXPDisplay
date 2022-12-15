// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OuttakeEdits;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Outtake;
import frc.robot.util.TrajectoryHelper;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class TeleopCommand extends CommandBase {
  private static final double TARGET_HEIGHT = (10) * 12 * 2.54 / 100; //in * cm/in * m/cm
  private static final double TARGET_DISTANCE = 7.016; // in *cm/in * m/cm
  private static final double TARGET_ENTRY_ANGLE = -45 * Math.PI/180; // degrees * radians/degree
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Drive m_drive;
  private final Outtake m_outtake;
  private final Intake m_intake;
  public NetworkTableEntry driveSpeed;
  private NetworkTableEntry manipulatorHold;
  private NetworkTableEntry shootTopReverse;
  private NetworkTableEntry shootBottomReverse;
  private NetworkTableEntry monoVsDual;
  private NetworkTableEntry shootTopSpeed;
  private NetworkTableEntry shootBottomSpeed;
  private NetworkTableEntry intakeFrontSpeed;
  private NetworkTableEntry monoVsDualI;
  private NetworkTableEntry intakeBackSpeed;
  private NetworkTableEntry intakeFrontReverse;
  private NetworkTableEntry intakeBackReverse;
  private NetworkTableEntry outtakeSpeed;
  private NetworkTableEntry outtakeAngle;
  private int rightReverseDecide = 1;
  private int leftReverseDecide = 1;
  private int topDecide = 1;
  private int bottomDecide = 1;
  private int frontDecide = 1;
  private int backDecide = 1;
  private Timer reset_timer;
 

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TeleopCommand(Drive drive, Outtake outtake, Intake intake) {
    m_drive = drive;
    m_outtake = outtake;
    m_intake = intake;
    
    // drive
    driveSpeed = Shuffleboard.getTab("Teleop").add("Drive Speed", Constants.DriveEdits.DRIVE_SPEED).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0,"Max",1)).getEntry();

    // outtake
    shootTopReverse = Shuffleboard.getTab("Teleop").add("Shoot Top Reverse", Constants.OuttakeEdits.SHOOT_TOP_REVERSE).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    shootBottomReverse = Shuffleboard.getTab("Teleop").add("Shoot Bottom Reverse", Constants.OuttakeEdits.SHOOT_BOTTOM_REVERSE).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    monoVsDual = Shuffleboard.getTab("Teleop").add("O: Dual or Mono", Constants.OuttakeEdits.MONO_SHOOT).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

    shootTopSpeed = Shuffleboard.getTab("Teleop").add("Shoot Top Or Main Speed", Constants.OuttakeEdits.SHOOT_TOP_SPEED).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0,"Max",1)).getEntry();
    shootBottomSpeed = Shuffleboard.getTab("Teleop").add("Shoot Bottom Speed", Constants.OuttakeEdits.SHOOT_BOTTOM_SPEED).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0,"Max",1)).getEntry();

    manipulatorHold = Shuffleboard.getTab("Teleop").add("Manipulator Hold Power", Constants.OuttakeEdits.MANIPULATOR_HOLD_MULTIPLIER).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0.1,"Max",5)).getEntry();
    // intake
    intakeFrontReverse = Shuffleboard.getTab("Teleop").add("Intake Front Reverse", Constants.IntakeEdits.INTAKE_FRONT_REVERSE).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    intakeBackReverse = Shuffleboard.getTab("Teleop").add("Intake Back Reverse", Constants.IntakeEdits.INTAKE_BACK_REVERSE).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    monoVsDualI = Shuffleboard.getTab("Teleop").add("I: Dual or Mono", Constants.IntakeEdits.MONO_SHOOT).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

    intakeFrontSpeed = Shuffleboard.getTab("Teleop").add("Intake Front or Main Speed", Constants.IntakeEdits.INTAKE_FRONT_SPEED).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0,"Max",1)).getEntry();
    intakeBackSpeed = Shuffleboard.getTab("Teleop").add("Intake Back Speed", Constants.IntakeEdits.INTAKE_BACK_SPEED).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0,"Max",1)).getEntry();

    outtakeSpeed = Shuffleboard.getTab("Teleop").add("Shoot Speed",Constants.OuttakeEdits.SHOOT_SPEED).getEntry();
    outtakeAngle = Shuffleboard.getTab("Teleop").add("Shoot Angle",Constants.OuttakeEdits.SHOOT_ANGLE).getEntry();

    reset_timer = new Timer();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_intake);
    addRequirements(m_drive);
    addRequirements(m_outtake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drive.resetEncoders();
    m_drive.resetPose();
    m_outtake.resetAimEncoder();
    reset_timer.reset();
    reset_timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double[] sticks = new double[] {RobotContainer.getInstance().getLeftYAxis(), RobotContainer.getInstance().getRightYAxis()};
    m_drive.zoomZoom(sticks[0]*Math.sqrt(driveSpeed.getDouble(Constants.DriveEdits.DRIVE_SPEED)), sticks[1]*Math.sqrt(driveSpeed.getDouble(Constants.DriveEdits.DRIVE_SPEED)));
    
    if (RobotContainer.getInstance().getXButton() == 1) {
      if(RobotContainer.getInstance().getXInput().getXButtonPressed()){
        reset_timer.reset();
      }
      if(reset_timer.get() >= 2){
        m_outtake.resetAimEncoder();
      }
    }
    
    topDecide = shootTopReverse.getBoolean(Constants.OuttakeEdits.SHOOT_TOP_REVERSE)? 1:-1;
    bottomDecide = shootBottomReverse.getBoolean(Constants.OuttakeEdits.SHOOT_BOTTOM_REVERSE)? 1:-1;

    if (monoVsDual.getBoolean(Constants.OuttakeEdits.MONO_SHOOT)) {
      m_outtake.shoot(RobotContainer.getInstance().getAButton()*shootTopSpeed.getDouble(Constants.OuttakeEdits.SHOOT_TOP_SPEED)*topDecide);
    } else {
      // System.out.println("IM USEFUL!!!!");
      m_outtake.shoot((RobotContainer.getInstance().getAButton() * shootTopSpeed.getDouble(Constants.OuttakeEdits.SHOOT_TOP_SPEED)*topDecide), (RobotContainer.getInstance().getAButton() * shootBottomSpeed.getDouble(Constants.OuttakeEdits.SHOOT_BOTTOM_SPEED)*bottomDecide));
    }

    frontDecide = intakeFrontReverse.getBoolean(Constants.IntakeEdits.INTAKE_FRONT_REVERSE)? -1:1;
    backDecide = intakeBackReverse.getBoolean(Constants.IntakeEdits.INTAKE_BACK_REVERSE)? -1:1;

    // System.out.println("Y: " + RobotContainer.getInstance().getYButton() + ", Cons: " + Constants.OuttakeEdits.INTAKE_SPEED);
    //m_intake.intake((RobotContainer.getInstance().getYButton() * intakeFrontSpeed.getDouble(Constants.IntakeEdits.INTAKE_FRONT_SPEED)));
    if (monoVsDualI.getBoolean(Constants.IntakeEdits.MONO_SHOOT)) {
      m_intake.intakeBall((RobotContainer.getInstance().getYButton() * intakeFrontSpeed.getDouble(Constants.IntakeEdits.INTAKE_FRONT_SPEED) * frontDecide));
    } else {
      m_intake.intakeBall((RobotContainer.getInstance().getYButton() * intakeFrontSpeed.getDouble(Constants.IntakeEdits.INTAKE_FRONT_SPEED) * frontDecide), (RobotContainer.getInstance().getYButton() * intakeBackSpeed.getDouble(Constants.IntakeEdits.INTAKE_BACK_SPEED)*backDecide));
    }
    //if(RobotContainer.getInstance().getXInput().getXButton()){
    //  m_drive.cry();
    //} (Don't cry robot it's okay :( ))

    // aimer
    if(RobotContainer.getInstance().getXInput().getRightBumper()){
      m_outtake.setAimSpeed(0.3);
    }else if(RobotContainer.getInstance().getXInput().getLeftBumper()){
      m_outtake.setAimSpeed(-0.3);
    }else{
      m_outtake.setAimingVoltage(0);
    }



    // autoaiming and firing
    if(RobotContainer.getInstance().getXInput().getBButton()){
      if(m_outtake.go_to_angle(outtakeAngle.getDouble(OuttakeEdits.SHOOT_ANGLE))){
        m_outtake.setAimingVoltage(0);
        m_outtake.shootAtSpeed(outtakeSpeed.getDouble(OuttakeEdits.SHOOT_SPEED));
      }
    }

    // shooting ball
    if(RobotContainer.getInstance().getXInput().getRightTriggerAxis() > Constants.IntakeEdits.INTAKE_DEADZONE){
      m_intake.intakeBall(Constants.IntakeEdits.INTAKE_FRONT_SPEED, Constants.IntakeEdits.INTAKE_BACK_SPEED);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}