// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Display;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private static RobotContainer m_robotContainer = new RobotContainer();  
    @SuppressWarnings("unused")
    private final Display m_display = new Display();

  public RobotContainer() {
    configureButtonBindings();
  }

  public static RobotContainer getInstance() {
    return m_robotContainer;
  }
  private void configureButtonBindings() {}

  public Command getAutonomousCommand() {
    return new WaitCommand(1);
  }

  public Command getTeleopCommand(){
    return new WaitCommand(1);
  }
}
