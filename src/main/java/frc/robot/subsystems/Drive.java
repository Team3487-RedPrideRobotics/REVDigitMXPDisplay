// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drive extends SubsystemBase {

  private DifferentialDrive gonkDrive;
  CANSparkMax leftDrive1;
  CANSparkMax leftDrive2;
  CANSparkMax rightDrive1;
private CANSparkMax rightDrive2;
  private MotorControllerGroup leftDrive;
  private MotorControllerGroup rightDrive;
  private RelativeEncoder leftEncoder;

  /** Creates a new ExampleSubsystem. */
  public Drive() {
    // sparks for the drive 
    leftDrive1 = new CANSparkMax(Constants.DriveConstants.LEFT_DRIVE_SPARKS[0], MotorType.kBrushless);
    leftDrive2 = new CANSparkMax(Constants.DriveConstants.LEFT_DRIVE_SPARKS[1], MotorType.kBrushless);
    rightDrive1 = new CANSparkMax(Constants.DriveConstants.RIGHT_DRIVE_SPARKS[0], MotorType.kBrushless);
    rightDrive2 = new CANSparkMax(Constants.DriveConstants.RIGHT_DRIVE_SPARKS[1], MotorType.kBrushless);

    // we play stick and stone and make stickstone (combine sparks to do the same thing)
    leftDrive = new MotorControllerGroup(leftDrive1, leftDrive2);
    rightDrive = new MotorControllerGroup(rightDrive1, rightDrive2);
    
    // grug needs to make both work together (inverts some inputs)
    leftDrive.setInverted(Constants.DriveEdits.LEFT_DRIVE_REVERSE);
    rightDrive.setInverted(Constants.DriveEdits.RIGHT_DRIVE_REVERSE);

    // the driver
    gonkDrive = new DifferentialDrive(leftDrive , rightDrive);

    leftEncoder = leftDrive1.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    leftEncoder.setPosition(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
   SmartDashboard.putNumber("Drive Speed", leftEncoder.getVelocity() * 0.5 * Math.PI / 60); // Rotation/min * ft/rotation * min/sec = ft/sec 
   SmartDashboard.putNumber("Drive Position", leftEncoder.getPosition() * 0.5 * Math.PI / 10);
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  // methods
  public void zoomZoom(double leftSpeed, double rightSpeed) {
    gonkDrive.tankDrive(leftSpeed, rightSpeed);
  }

<<<<<<< Updated upstream
  public void resetEncoders(){
    leftEncoder.setPosition(0);
=======
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
>>>>>>> Stashed changes
  }
}
