// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Outtake extends SubsystemBase {

  private PWMSparkMax shoot_top;
  private PWMSparkMax shoot_bottom;
  private MotorControllerGroup shooter;
  private Spark aimer;
  private Encoder aim_encoder;
  private double kP = 1/71;
  private double aimTheshold = 2;

  /** Creates a new ExampleSubsystem. */
  public Outtake() {
    // the shooters, stephen curry
    shoot_bottom = new PWMSparkMax(Constants.OuttakeConstants.SHOOT_BOTTOM_SPARK);
    shoot_top = new PWMSparkMax(Constants.OuttakeConstants.SHOOT_TOP_SPARK);

    // stephen curry's inverts
    shoot_top.setInverted(Constants.OuttakeEdits.SHOOT_TOP_REVERSE);
    shoot_bottom.setInverted(Constants.OuttakeEdits.SHOOT_BOTTOM_REVERSE);

    // make stephen curry use both his arms
    shooter = new MotorControllerGroup(shoot_top, shoot_bottom);

    // Aiming the shooter
    aimer = new Spark(Constants.OuttakeConstants.AIMER_SPARK);
    aim_encoder = new Encoder(0, 1);

    // TODO: set distance per pulse to be equal to 360 degrees per 1 rotation
    aim_encoder.setDistancePerPulse(1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public void go_to_angle(double angle){
    double deltaDistance = aim_encoder.getDistance() - angle;
    if(Math.abs(deltaDistance) >= aimTheshold){
      aimer.set(deltaDistance*kP);
    }else{
      aimer.set(Constants.OuttakeEdits.MANIPULATOR_HOLD_MULTIPLIER);
    }
  }

  // shooters
  public void shoot(double power) {
    shooter.set(power);
  }

  public void shoot(double topPower, double bottomPower) {
    shoot_top.set(topPower);
    shoot_bottom.set(bottomPower);
  }
  // methods
}
