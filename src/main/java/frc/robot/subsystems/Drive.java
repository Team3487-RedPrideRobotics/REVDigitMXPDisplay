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
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveEdits;

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
  private NetworkTableInstance inst;
  private NetworkTable datatable;
  private NetworkTableEntry yEntry;
  private NetworkTableEntry xEntry;
  private NetworkTableEntry angleEntry;
  private Field2d m_field;

  /** Creates a new ExampleSubsystem. */
  public Drive() {
     //get the default instance of NetworkTables
    inst = NetworkTableInstance.getDefault();

      //get a reference to the subtable called "datatable"
    datatable = inst.getTable("Vision");

      //get a reference to key in "datatable" called "Y"
    yEntry = datatable.getEntry("y");
    xEntry = datatable.getEntry("x");
    angleEntry = datatable.getEntry("angle");
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
    leftEncoder.setPositionConversionFactor(Constants.DriveConstants.DRIVE_POSITION_SCALE);
    leftEncoder.setVelocityConversionFactor(Constants.DriveConstants.DRIVE_VELOCITY_SCALE);

    rightEncoder = rightDrive2.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    rightEncoder.setPosition(0);
    rightEncoder.setPositionConversionFactor(Constants.DriveConstants.DRIVE_POSITION_SCALE);
    rightEncoder.setVelocityConversionFactor(Constants.DriveConstants.DRIVE_VELOCITY_SCALE);

    m_field = new Field2d();
    

    // odometry
    gyroscope = new ADXRS450_Gyro();
    gyroscope.calibrate();
    gyroscope.reset();
    m_pose = new Pose2d(Constants.DriveConstants.START_X, Constants.DriveConstants.START_Y, new Rotation2d());
    m_odometry = new DifferentialDrivePoseEstimator(gyroscope.getRotation2d(), m_pose,
    new MatBuilder<>(Nat.N5(), Nat.N1()).fill(0.02, 0.02, 0.01, 0.02, 0.02), // State measurement standard deviations. X, Y, theta.
    new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0.02, 0.02, 0.01), // Local measurement standard deviations. Left encoder, right encoder, gyro.
    new MatBuilder<>(Nat.N3(), Nat.N1()).fill(0.1, 0.1, 0.01)); // Global measurement standard deviations. X, Y, and theta. 
    yEntry.addListener(event -> {
      Pose2d previous = m_odometry.getEstimatedPosition();
      Pose2d visionPose = new Pose2d(new Translation2d(xEntry.getDouble(previous.getX()), yEntry.getDouble(previous.getY())), new Rotation2d(angleEntry.getDouble(previous.getRotation().getRadians())));
      Transform2d deltaPose = visionPose.minus(previous);
      if(deltaPose.getX() < DriveEdits.VISION_THRESHOLD.getX() && deltaPose.getY() < DriveEdits.VISION_THRESHOLD.getY() && deltaPose.getRotation().getDegrees() < DriveEdits.VISION_THRESHOLD.getRotation().getDegrees()){
        m_odometry.addVisionMeasurement(visionPose, Timer.getFPGATimestamp());
      }
   }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
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
    m_field.setRobotPose(m_odometry.getEstimatedPosition());
    SmartDashboard.putData("Field",m_field);
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  // methods
  public void zoomZoom(double leftSpeed, double rightSpeed) {
    gonkDrive.tankDrive(leftSpeed, rightSpeed);
  }

  /**
   * tank drive without input smoothing
   * @param leftSpeed
   * @param rightSpeed
   */
  public void tankDriveRaw(double leftSpeed, double rightSpeed){
    leftDrive.set(leftSpeed);
    rightDrive.set(rightSpeed);
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
  /**
   * 
   * @param angle in degrees
   */
  public void turnToAngle(double angle){
    double delta_angle = angle - m_pose.getRotation().getDegrees();
    if(Math.abs(delta_angle) >= Constants.DriveEdits.TURN_THRESHOLD){
      double speed = delta_angle * Constants.DriveEdits.KP_DRIVETRAIN;
      tankDriveRaw(-speed, speed);
    }

  }

  public void turnToGoal(){
    turnToAngle(Math.atan2(Constants.DriveEdits.GOAL_POSE.getX()-m_pose.getX(), Constants.DriveEdits.GOAL_POSE.getY()-m_pose.getY()));
  }
}
