// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.commands.TeleopCommand;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Outtake;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private static RobotContainer m_robotContainer = new RobotContainer();  
  // The robot's subsystems and commands are defined here...
  private final Drive m_drive = new Drive();
  private final Outtake m_outtake = new Outtake();
  private final Intake m_intake = new Intake();

  private final XboxController XInput = new XboxController(0);

  private final TeleopCommand m_teleopCommand = new TeleopCommand(m_drive, m_outtake, m_intake);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    Shuffleboard.getTab("Values");
    configureButtonBindings();
  }

  public static RobotContainer getInstance() {
    return m_robotContainer;
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {}

  public XboxController getXInput(){
    return XInput;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null;
  }

  public Command getTeleopCommand(){
    return m_teleopCommand;
  }

  // copy-ith the evergreen joystick input code (I'm lazy)
  public double getLeftYAxis(){
    if(XInput == null){
      return 0;
    }
    double item = (Math.abs(XInput.getLeftY()) > Constants.Controller.DEAD_ZONE) ? XInput.getLeftY() : 0;
    return item;
  }

  public double getRightYAxis(){
    if(XInput == null){
      return 0;
    }
    double item = (Math.abs(XInput.getRightY()) > Constants.Controller.DEAD_ZONE) ? XInput.getRightY() : 0;
    return item;
  }

public double getLeftXAxis(){
    if(XInput == null){
      return 0;
    }
    double item = (Math.abs(XInput.getLeftX()) > Constants.Controller.DEAD_ZONE) ? XInput.getLeftX() : 0;
    return item;
  }

  public double getRightXAxis(){
    if(XInput == null){
      return 0;
    }
    double item = (Math.abs(XInput.getRightX()) > Constants.Controller.DEAD_ZONE) ? XInput.getRightX() : 0;
    return item;
  }

  //real buttons

  public int getXButton() {
    int item = (XInput.getXButton() == true) ? 1 : 0;
    return item; 
  }

  public int getYButton() {
    int item = (XInput.getYButton() == true) ? 1 : 0;
    return item; 
  }

  public int getAButton() {
    int item = (XInput.getAButton() == true) ? 1 : 0;
    return item; 
  }
}
