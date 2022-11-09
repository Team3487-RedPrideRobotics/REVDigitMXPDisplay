// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.ExampleSubsystem;
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
  private int rightReverseDecide = 1;
  private int leftReverseDecide = 1;
 

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
    manipulatorHold = Shuffleboard.getTab("Teleop").add("Manipulator Hold Power", Constants.OuttakeEdits.MANIPULATOR_HOLD_MULTIPLIER).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min",0.1,"Max",5)).getEntry();

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
    leftReverseDecide = leftReverse.getBoolean(Constants.DriveEdits.LEFT_DRIVE_REVERSE)? 1:-1;
    rightReverseDecide = rightReverse.getBoolean(Constants.DriveEdits.RIGHT_DRIVE_REVERSE)? 1:-1;
    m_drive.zoomZoom(sticks[0]*Math.sqrt(driveSpeed.getDouble(Constants.DriveEdits.DRIVE_SPEED))*leftReverseDecide, sticks[1]*Math.sqrt(driveSpeed.getDouble(Constants.DriveEdits.DRIVE_SPEED))*rightReverseDecide);
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
