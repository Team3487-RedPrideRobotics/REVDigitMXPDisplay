// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {

  private PWMSparkMax leftDrive, rightDrive;
  private DifferentialDrive gonkDrive;

  /** Creates a new ExampleSubsystem. */
  public Drive() {
    leftDrive = new PWMSparkMax(0);
    rightDrive = new PWMSparkMax(1);
    gonkDrive = new DifferentialDrive(leftDrive , rightDrive);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  // methods
  public void zoomZoom(double leftSpeed, double rightSpeed) {
    gonkDrive.tankDrive(leftSpeed, rightSpeed);
  }
}
