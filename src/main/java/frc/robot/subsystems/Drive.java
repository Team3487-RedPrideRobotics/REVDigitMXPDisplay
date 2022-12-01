// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drive extends SubsystemBase {

  private DifferentialDrive gonkDrive;
  PWMSparkMax leftDrive1;
  CANSparkMax leftDrive2;
  PWMSparkMax rightDrive1;
  CANSparkMax rightDrive2;

  /* Testing for Simulation
  PWMSparkMax leftDrive1;
  PWMSparkMax leftDrive2;
  PWMSparkMax rightDrive1;
  PWMSparkMax rightDrive2;
  */

  private MotorControllerGroup leftDrive;
  private MotorControllerGroup rightDrive;
  private RelativeEncoder leftEncoder;
  private DifferentialDrivePoseEstimator m_odometry;
  private Gyro gyroscope;
  private RelativeEncoder rightEncoder;
  private Pose2d m_pose;

  /** Creates a new ExampleSubsystem. */
  public Drive() {
    // sparks for the drive 
    leftDrive1 = new PWMSparkMax(Constants.DriveConstants.LEFT_DRIVE_SPARKS[0]);
    leftDrive2 = new CANSparkMax(Constants.DriveConstants.LEFT_DRIVE_SPARKS[1], MotorType.kBrushless);
    rightDrive1 = new PWMSparkMax(Constants.DriveConstants.RIGHT_DRIVE_SPARKS[0]);
    rightDrive2 = new CANSparkMax(Constants.DriveConstants.RIGHT_DRIVE_SPARKS[1], MotorType.kBrushless);
    
    
    /* ONLY FOR TESTS
    leftDrive1 = new PWMSparkMax(Constants.DriveConstants.LEFT_DRIVE_SPARKS[0]);
    leftDrive2 = new PWMSparkMax(Constants.DriveConstants.LEFT_DRIVE_SPARKS[1]);
    rightDrive1 = new PWMSparkMax(Constants.DriveConstants.RIGHT_DRIVE_SPARKS[0]);
    rightDrive2 = new PWMSparkMax(Constants.DriveConstants.RIGHT_DRIVE_SPARKS[1]);
    */

    // we play stick and stone and make stickstone (combine sparks to do the same thing)
    leftDrive = new MotorControllerGroup(leftDrive1, leftDrive2);
    rightDrive = new MotorControllerGroup(rightDrive1, rightDrive2);
    
    // grug needs to make both work together (inverts some inputs)
    leftDrive.setInverted(Constants.DriveEdits.LEFT_DRIVE_REVERSE);
    rightDrive.setInverted(Constants.DriveEdits.RIGHT_DRIVE_REVERSE);

    // the driver
    gonkDrive = new DifferentialDrive(leftDrive , rightDrive);

    leftEncoder = leftDrive2.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    leftEncoder.setPosition(0);

    rightEncoder = rightDrive2.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    // odometry
    gyroscope = new ADXRS450_Gyro();
    m_pose = new Pose2d(Constants.DriveConstants.START_X, Constants.DriveConstants.START_Y, new Rotation2d());
    m_odometry = new DifferentialDrivePoseEstimator(gyroscope.getRotation2d(), m_pose,
    new MatBuilder<>(Nat.N5(), Nat.N1()).fill(0.02, 0.02, 0.01, 0.02, 0.02), // State measurement standard deviations. X, Y, theta.
    new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0.02, 0.02, 0.01), // Local measurement standard deviations. Left encoder, right encoder, gyro.
    new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0.1, 0.1, 0.01)); // Global measurement standard deviations. X, Y, and theta. 
  }

  @Override
  public void periodic() {
    var gyroAngle = Rotation2d.fromDegrees(-gyroscope.getAngle());
    m_pose = m_odometry.update(gyroAngle, new DifferentialDriveWheelSpeeds(leftEncoder.getVelocity(), rightEncoder.getVelocity()), leftEncoder.getPosition(), rightEncoder.getPosition());
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Drive Left Speed", leftEncoder.getVelocity()); 
    SmartDashboard.putNumber("Drive Right Speed", rightEncoder.getVelocity()); 
    SmartDashboard.putNumber("Drive Left Position", leftEncoder.getPosition());
    SmartDashboard.putNumber("Drive Right Position", rightEncoder.getPosition());
    SmartDashboard.putNumber("Gyro Angle", gyroAngle.getDegrees());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  // methods
  public void zoomZoom(double leftSpeed, double rightSpeed) {
    gonkDrive.tankDrive(leftSpeed, rightSpeed);
  }

  public void resetEncoders() {
    leftEncoder.setPosition(0);
  }

  // funny commands 

  public void cry(){
    rightDrive1.set(1);
    rightDrive2.set(1);
    System.out.println("waaah waaaaaah waaaaaaah im a huge baby look at me");
  }

  public void cmake(){
    System.out.println("Me after labotomy");
    leftDrive.set(1);
    rightDrive.set(0);
  }

  public void ltg(){
    System.out.println("Because you guy's don't possess the JEM gene, that's why you're upset, no JEM equals ANGER!");
    rightDrive.set(1);
    leftDrive.set(1);
  }
}
