// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Outtake;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class TeleopCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Drive m_drive;
  private final Outtake m_outtake;
  public NetworkTableEntry driveSpeed;
  private NetworkTableEntry leftReverse;
  private NetworkTableEntry rightReverse;
  private NetworkTableEntry manipulatorHold;
  private NetworkTableEntry shootTopReverse;
  private NetworkTableEntry shootBottomReverse;
  private NetworkTableEntry monoVsDual;
  private NetworkTableEntry shootTopSpeed;
  private NetworkTableEntry shootBottomSpeed;
  private NetworkTableEntry intakeSpeed;
  private int rightReverseDecide = 1;
  private int leftReverseDecide = 1;
  private int topDecide = 1;
  private int bottomDecide = 1;
 

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TeleopCommand(Drive drive, Outtake outtake) {
    m_drive = drive;
    m_outtake = outtake;
    
    // drive
    driveSpeed = Shuffleboard.getTab("Teleop").add("Drive Speed", Constants.DriveEdits.DRIVE_SPEED).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0,"Max",1)).getEntry();
    leftReverse = Shuffleboard.getTab("Teleop").add("Left Reversed", Constants.DriveEdits.LEFT_DRIVE_REVERSE).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    rightReverse = Shuffleboard.getTab("Teleop").add("Right Reversed", Constants.DriveEdits.RIGHT_DRIVE_REVERSE).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

    // outtake
    shootTopReverse = Shuffleboard.getTab("Teleop").add("Shoot Top Reverse", Constants.OuttakeEdits.SHOOT_TOP_REVERSE).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    shootBottomReverse = Shuffleboard.getTab("Teleop").add("Shoot Bottom Reverse", Constants.OuttakeEdits.SHOOT_BOTTOM_REVERSE).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    monoVsDual = Shuffleboard.getTab("Teleop").add("Dual or Mono", Constants.OuttakeEdits.MONO_SHOOT).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();

    shootTopSpeed = Shuffleboard.getTab("Teleop").add("Shoot Top Or Main Speed", Constants.OuttakeEdits.SHOOT_TOP_SPEED).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0,"Max",1)).getEntry();
    shootBottomSpeed = Shuffleboard.getTab("Teleop").add("Shoot Bottom Speed", Constants.OuttakeEdits.SHOOT_BOTTOM_SPEED).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0,"Max",1)).getEntry();

    intakeSpeed = Shuffleboard.getTab("Teleop").add("Intake Speed", Constants.OuttakeEdits.INTAKE_SPEED).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0,"Max",1)).getEntry();
    //
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_drive);
    addRequirements(m_outtake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drive.resetEncoders();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double[] sticks = new double[] {RobotContainer.getInstance().getLeftYAxis(), RobotContainer.getInstance().getRightYAxis()};
    leftReverseDecide = leftReverse.getBoolean(Constants.DriveEdits.LEFT_DRIVE_REVERSE)? -1:1;
    rightReverseDecide = rightReverse.getBoolean(Constants.DriveEdits.RIGHT_DRIVE_REVERSE)? -1:1;
    m_drive.zoomZoom(sticks[0]*Math.sqrt(driveSpeed.getDouble(Constants.DriveEdits.DRIVE_SPEED))*leftReverseDecide, sticks[1]*Math.sqrt(driveSpeed.getDouble(Constants.DriveEdits.DRIVE_SPEED))*rightReverseDecide);
    
    if (RobotContainer.getInstance().getXButton() == 1) {
      m_drive.resetEncoders();
    }


    topDecide = shootTopReverse.getBoolean(Constants.OuttakeEdits.SHOOT_TOP_REVERSE)? -1:1;
    bottomDecide = shootBottomReverse.getBoolean(Constants.OuttakeEdits.SHOOT_BOTTOM_REVERSE)? -1:1;

    if (monoVsDual.getBoolean(Constants.OuttakeEdits.MONO_SHOOT)) {
      m_outtake.shoot(RobotContainer.getInstance().getAButton()*shootTopSpeed.getDouble(Constants.OuttakeEdits.SHOOT_TOP_SPEED*topDecide));
    } else {
      // System.out.println("IM USEFUL!!!!");
      m_outtake.shoot((RobotContainer.getInstance().getAButton() * shootTopSpeed.getDouble(Constants.OuttakeEdits.SHOOT_TOP_SPEED)*topDecide), (RobotContainer.getInstance().getAButton() * shootBottomSpeed.getDouble(Constants.OuttakeEdits.SHOOT_BOTTOM_SPEED)*bottomDecide));
    }

    // System.out.println("Y: " + RobotContainer.getInstance().getYButton() + ", Cons: " + Constants.OuttakeEdits.INTAKE_SPEED);
    m_outtake.intake((RobotContainer.getInstance().getYButton() * intakeSpeed.getDouble(Constants.OuttakeEdits.INTAKE_SPEED)));
    
    //if(RobotContainer.getInstance().getXInput().getXButton()){
    //  m_drive.cry();
    //} (Don't cry robot it's okay :( ))
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
