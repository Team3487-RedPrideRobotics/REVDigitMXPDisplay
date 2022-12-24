package frc.robot.subsystems;

import java.text.DecimalFormat;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.usfirst.frc.team3487.REVDigitMXPDisplay;

public class Display extends SubsystemBase{

    private REVDigitMXPDisplay displayBoard;
    private NetworkTableEntry stringEntry;
    private String state;
    DecimalFormat df_obj;
    private Timer idleTimer;

    public Display(){
        displayBoard = REVDigitMXPDisplay.getInstance();
        stringEntry = Shuffleboard.getTab("Display").add("String","3487").getEntry();
        state = "idle";
        df_obj = new DecimalFormat("##.0");
        displayBoard.clear();
        idleTimer = new Timer();
        idleTimer.start();
    }

    @Override
    public void periodic(){
        infoLoop();
    }

    public void battery(Boolean debug) {
        double voltage = RobotController.getBatteryVoltage();
        displayBoard.displayText(df_obj.format(voltage).concat("V"),debug);
        
    }
    public void battery() {
        battery(false);
        
    }
    

    private void infoLoop(){
        if(state=="idle"){
            if(displayBoard.getAButtonPressed()){
                state="battery";
            }
            if(displayBoard.getBButtonPressed()){
                state="custom";
            }
            idle(true);
        }else if(state=="battery"){
            if(displayBoard.getAButtonPressed()){
                state="idle";
            }
            if(displayBoard.getBButtonPressed()){
                state="custom";
            }
            battery(true);
        }else if(state=="custom"){
            if(displayBoard.getAButtonPressed()){
                state="battery";
            }
            if(displayBoard.getBButtonPressed()){
                state="idle";
            }
            customDisplay(true);
        }
    }

    @Override
    public void simulationPeriodic(){
    }   

    public boolean getAButtonPressed(){
        return displayBoard.getAButtonPressed();
    }

    public boolean getBButtonPressed(){
        return displayBoard.getBButtonPressed();
    }
    public void idle(boolean debug){
        displayBoard.displayScrollText("3487", 0.2, debug);
    }

    public void idle(){
        idle(false);
    }
    public void customDisplay(Boolean debug){
        displayBoard.displayScrollText(stringEntry.getString("3487"), 0.2, true);
    }
    public void customDisplay(){
        customDisplay(false);
    }
}
