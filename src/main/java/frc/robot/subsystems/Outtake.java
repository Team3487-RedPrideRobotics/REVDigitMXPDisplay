// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.OuttakePID;

public class Outtake extends SubsystemBase {

  private CANSparkMax shoot_top;
  private CANSparkMax shoot_bottom;
  private MotorControllerGroup shooter;
  private CANSparkMax aimer;
  private RelativeEncoder aim_encoder;
  private double kP_aimer = 1/71;
  private double aimTheshold = 2*Math.PI/180;
  private double spin_ratio = 1; //ratio between top and bottom speed
  private double top_radius = 2d/2d*2.54/100; // radius of top flywheel in meters
  private double bottom_radius = 4d/2d*2.54/100; // radius of bottom flywheel in meters
  private RelativeEncoder top_encoder;
  private RelativeEncoder bottom_encoder;
  private SparkMaxPIDController PIDController_top;
  private SparkMaxPIDController PIDController_bottom;
  private SimpleMotorFeedforward feedforward_top;
  private SimpleMotorFeedforward feedforward_bottom;

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
    aimer = new CANSparkMax(Constants.OuttakeConstants.AIMER_SPARK, MotorType.kBrushless);
    aim_encoder = aimer.getEncoder();

    // TODO: set distance per pulse to be equal to 360 degrees per 1 rotation
    top_encoder = shoot_top.getEncoder();
    top_encoder.setPosition(0);
    bottom_encoder = shoot_bottom.getEncoder();
    top_encoder.setPosition(0);

    // Shooting PID controller
    PIDController_top = shoot_top.getPIDController();
    PIDController_bottom = shoot_bottom.getPIDController();

    feedforward_top = new SimpleMotorFeedforward(OuttakePID.kS_shooter_top, OuttakePID.kV_shooter_top, OuttakePID.kA_shooter_top);
    
    PIDController_top.setP(OuttakePID.kP_shooter_top);
    PIDController_top.setI(OuttakePID.kI_shooter);
    PIDController_top.setD(OuttakePID.kD_shooter);
    PIDController_top.setIZone(OuttakePID.kIz_shooter);
    PIDController_top.setFF(OuttakePID.kFF_shooter);
    PIDController_top.setOutputRange(OuttakePID.kMinOutput_shooter, OuttakePID.kMaxOutput_shooter);

    feedforward_bottom = new SimpleMotorFeedforward(OuttakePID.kS_shooter_bottom, OuttakePID.kV_shooter_bottom, OuttakePID.kA_shooter_bottom);


    PIDController_bottom.setP(OuttakePID.kP_shooter_bottom);
    PIDController_bottom.setI(OuttakePID.kI_shooter);
    PIDController_bottom.setD(OuttakePID.kD_shooter);
    PIDController_bottom.setIZone(OuttakePID.kIz_shooter);
    PIDController_bottom.setFF(OuttakePID.kFF_shooter);
    PIDController_bottom.setOutputRange(OuttakePID.kMinOutput_shooter, OuttakePID.kMaxOutput_shooter);    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Top Velocity", top_encoder.getVelocity());
    SmartDashboard.putNumber("Bottom Position", bottom_encoder.getPosition());
    SmartDashboard.putNumber("Bottom Velocity", bottom_encoder.getVelocity());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  /**
   * PID control to move shooter to specific angle
   * @param angle in radians
   * @return boolean value for whether the aimer is within the error threshold
   */
  public boolean go_to_angle(double angle){
    double deltaDistance = aim_encoder.getPosition() - angle;
    if(Math.abs(deltaDistance) >= aimTheshold){
      aimer.set(deltaDistance*kP_aimer);
      return false;
    }else{
      aimer.set(0);
      return true;
    }
  }
  
  public void resetAimEncoder(){
    aim_encoder.setPosition(0);
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
   * @param speed the desired speed of the ball in meters per second
   */
  public void shootAtSpeed(double speed){
    double linear_top_speed = Math.sqrt(speed/spin_ratio);
    double rotation_top_speed = linear_top_speed/top_radius;
    double rotation_bottom_speed = linear_top_speed*spin_ratio/bottom_radius;

    shoot_bottom.setVoltage(feedforward_bottom.calculate(rotation_bottom_speed));
    shoot_top.setVoltage(feedforward_top.calculate(rotation_top_speed));

  }
  /**
   * sets top and bottom shooter to specified angular speed
   * @param angular_speed in rotations per second
   */
  public void shootAtRawSpeed(double angular_speed){
    shoot_bottom.setVoltage(feedforward_bottom.calculate(angular_speed));
    shoot_top.setVoltage(feedforward_top.calculate(angular_speed));
    //PIDController_bottom.setFF(feedforward_bottom.calculate(angular_speed));
    //PIDController_top.setReference(angular_speed, CANSparkMax.ControlType.kVelocity);
    //PIDController_bottom.setReference(angular_speed, CANSparkMax.ControlType.kVelocity);
  }

  public void setAimingVoltage(double voltage){
    aimer.setVoltage(voltage);
  }
  // methods
}
