// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Outtake extends SubsystemBase {

  private CANSparkMax shoot_top;
  private CANSparkMax shoot_bottom;
  private MotorControllerGroup shooter;
  private Spark aimer;
  private Encoder aim_encoder;
  private double kP_aimer = 1/71;
  private double aimTheshold = 2;
  private double spin_ratio = 1; //ratio between top and bottom speed
  private double top_radius = 2d/2d*2.54/100; // radius of top flywheel in meters
  private double bottom_radius = 4d/2d*2.54/100; // radius of bottom flywheel in meters
  private RelativeEncoder top_encoder;
  private RelativeEncoder bottom_encoder;
  private double kP_shooter = 8e-5;
  private double kI_shooter = 0;
  private double kD_shooter = 0;
  private double kIz_shooter = 0;
  private double kFF_shooter = 0;
  private double kMinOutput_shooter = -1;
  private double kMaxOutput_shooter = 1;
  private double maxRPM = 5800;
  private SparkMaxPIDController PIDController_top;
  private SparkMaxPIDController PIDController_bottom;
  private NetworkTableEntry kP_entry;

  /** Creates a new ExampleSubsystem. */
  public Outtake() {
    // the shooters, stephen curry
    shoot_bottom = new CANSparkMax(Constants.OuttakeConstants.SHOOT_BOTTOM_SPARK, MotorType.kBrushless);
    shoot_top = new CANSparkMax(Constants.OuttakeConstants.SHOOT_TOP_SPARK, MotorType.kBrushless);

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

    top_encoder = shoot_top.getEncoder();
    bottom_encoder = shoot_bottom.getEncoder();

    // Shooting PID controller
    PIDController_top = shoot_top.getPIDController();
    PIDController_bottom = shoot_bottom.getPIDController();
    
    PIDController_top.setP(kP_shooter);
    PIDController_top.setI(kP_shooter);
    PIDController_top.setD(kP_shooter);
    PIDController_top.setIZone(kIz_shooter);
    PIDController_top.setFF(kFF_shooter);
    PIDController_top.setOutputRange(kMinOutput_shooter, kMaxOutput_shooter);

    PIDController_bottom.setP(kP_shooter);
    PIDController_bottom.setI(kP_shooter);
    PIDController_bottom.setD(kP_shooter);
    PIDController_bottom.setIZone(kIz_shooter);
    PIDController_bottom.setFF(kFF_shooter);
    PIDController_bottom.setOutputRange(kMinOutput_shooter, kMaxOutput_shooter);

    kP_entry = Shuffleboard.getTab("Outtake").add("kP",kP_shooter).getEntry();
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Top Velocity", top_encoder.getVelocity());
    SmartDashboard.putNumber("Bottom Velocity", bottom_encoder.getVelocity());
    PIDController_top.setP(kP_entry.getDouble(kP_shooter));
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public void go_to_angle(double angle){
    double deltaDistance = aim_encoder.getDistance() - angle;
    if(Math.abs(deltaDistance) >= aimTheshold){
      aimer.set(deltaDistance*kP_aimer);
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

  public void setAimSpeed(double speed){
    aimer.set(speed);
  }

  /**
   * Sets the bottom and top shooter motors so that the ball leaves the outtake at a set speed
   * 
   * @param speed the desired speed of the ball in meters
   */
  public void shootAtSpeed(double speed){
    double linear_top_speed = Math.sqrt(speed/spin_ratio);
    double rotation_top_speed = linear_top_speed/top_radius;
    double rotation_bottom_speed = linear_top_speed*spin_ratio/bottom_radius;

    if(top_encoder.getVelocity() < rotation_top_speed){
      shoot_top.set(1);
    }

    if(bottom_encoder.getVelocity() < rotation_bottom_speed){
      shoot_bottom.set(1);
    }

  }

  public void shootAtRawSpeed(double angular_speed){
    PIDController_top.setReference(angular_speed, CANSparkMax.ControlType.kVelocity);
    PIDController_bottom.setReference(angular_speed, CANSparkMax.ControlType.kVelocity);
  }

  public void setAimingVoltage(double voltage){
    aimer.setVoltage(voltage);
  }
  // methods
}
