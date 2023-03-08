// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private RobotContainer robotContainer;

  private final CANSparkMax m_ArmAngle = new CANSparkMax(13, MotorType.kBrushless);
  private final CANSparkMax m_ArmSlide = new CANSparkMax(14, MotorType.kBrushless);
  private final CANSparkMax m_FClamp = new CANSparkMax(15, MotorType.kBrushless);
  private final CANSparkMax m_BClamp = new CANSparkMax(16, MotorType.kBrushless);

  private final XboxController c_Controller1 = new XboxController(0);
  private final XboxController c_Controller2 = new XboxController(1);

  RelativeEncoder ArmAngleEncoder = m_ArmAngle.getEncoder();
  RelativeEncoder ArmSlideEncoder = m_ArmSlide.getEncoder();
  RelativeEncoder FrontClampEncoder = m_FClamp.getEncoder();
  RelativeEncoder BackClampEncoder = m_BClamp.getEncoder();

  private final Joystick j_flightstick = new Joystick(0);

  int counter = 0;
   boolean autoRan = false;
  double positionvar = ArmAngleEncoder.getPosition();
  public void printposition() {
  System.out.println("positionvar");
 }
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    robotContainer = new RobotContainer();
    counter = 0;
    CameraServer.startAutomaticCapture(0);
    ArmAngleEncoder.setPosition(0);
    ArmSlideEncoder.setPosition(0);
    BackClampEncoder.setPosition(0);
    FrontClampEncoder.setPosition(0);
  }
  boolean button1 = false;
  boolean button2 = false;
  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    Command autoCommand = robotContainer.getAutonomousCommand();
    if (autoCommand != null) {
      autoCommand.schedule();
    }
    autoRan = false;
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    ArmAngleEncoder.setPosition(0);
    ArmSlideEncoder.setPosition(0);
    BackClampEncoder.setPosition(0);
    FrontClampEncoder.setPosition(0);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //button 1: drop all
    if (j_flightstick.getRawButton(1)) {
      button1 = true;
      button2 = false;
    }
    if (j_flightstick.getRawButton(2)) {
      button1 = false;
      button2 = true;
    }
     if (button1 == true && ArmAngleEncoder.getPosition() <= 30) {
      m_ArmAngle.set(.1);
     } else if (button2 == true && ArmAngleEncoder.getPosition() >= 0) {
      m_ArmAngle.set(-.1);
    } else if (j_flightstick.getRawButton(12)) {
      m_ArmAngle.set(0);
    }

    System.out.println("Arm Angle: " + ArmAngleEncoder.getPosition());
    System.out.println("Arm Slide: " + ArmSlideEncoder.getPosition());

    //button 2: Will hates this one specifically

    //button 3: front grabber cone grab
    
    //button 4: back grabber cone grab
    
    //button 5: front grabber cube grab
    
    //button 6: back grabber cube grab
    
    
    //button 7: run ArmSlide to a position
    if (j_flightstick.getRawButton(7)) {
      while (ArmAngleEncoder.getPosition() < 60) {
        m_ArmAngle.set(-.1);
      } if (ArmAngleEncoder.getPosition() >= 60) {
        m_ArmAngle.set(0);
      }
    }
    //button 8: run ArmAngle to a position
    if (j_flightstick.getRawButton(8)) {
      while (ArmAngleEncoder.getPosition() > 0) {
        m_ArmAngle.set(.1);
      } if (ArmAngleEncoder.getPosition() <= 0) {
        m_ArmAngle.set(0);
      }
    }
    //button 9: hold to make ArmSlide extend

    //button 10: ArmSlide in and ArmAngle up to drive

    //button 11: hold to make ArmSlide retract

    //button 12: move ArmAngle down to pick-up position

    //Hat: forward = ArmAngle out back = ArmAngle in
    System.out.println("Clamp Position:"+ ArmAngleEncoder.getPosition());
    System.out.println("Button 1 pressed:"+ button1);
    System.out.println("Button 2 pressed:"+ button1);
    SmartDashboard.putNumber("ProcessVariable", ArmAngleEncoder.getPosition()); 
  }

  
  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    robotContainer.printWheelAngles();
  }
}
