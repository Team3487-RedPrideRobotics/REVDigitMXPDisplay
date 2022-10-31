// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {

  private DifferentialDrive gonkDrive;
  CANSparkMax leftDrive1;
  CANSparkMax leftDrive2;
  CANSparkMax rightDrive1;
private CANSparkMax rightDrive2;
  private MotorControllerGroup leftDrive;
  private MotorControllerGroup rightDrive;

  /** Creates a new ExampleSubsystem. */
  public Drive() {
    leftDrive1 = new CANSparkMax(1, MotorType.kBrushless);
    leftDrive2 = new CANSparkMax(2, MotorType.kBrushless);
    rightDrive1 = new CANSparkMax(3, MotorType.kBrushless);
    rightDrive2 = new CANSparkMax(4, MotorType.kBrushless);
    leftDrive = new MotorControllerGroup(leftDrive1, leftDrive2);
    rightDrive = new MotorControllerGroup(rightDrive1, rightDrive2);
    leftDrive.setInverted(true);
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

  public void cry(){
    rightDrive1.set(1);
    rightDrive2.set(1);
    System.out.println("crying");
  }
}
