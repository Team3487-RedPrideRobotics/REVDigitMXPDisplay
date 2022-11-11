// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

    Spark intakeFront;
    Spark intakeBack; 
    private MotorControllerGroup intake;

  /** Creates a new ExampleSubsystem. */
  public Intake() {
    // Taking in the ball
    intakeFront = new Spark(Constants.IntakeConstants.INTAKE_FRONT_SPARK);
    intakeBack = new Spark(Constants.IntakeConstants.INTAKE_BACK_SPARK);
    intakeBack.setInverted(true);

    intake = new MotorControllerGroup(intakeFront, intakeBack);
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
  public void intakeBall(double power){
    intake.set(power);
  }

  public void intakeBall(double powerFront, double powerBack){
    intakeFront.set(powerFront);
    intakeBack.set(powerBack);
  }
}
