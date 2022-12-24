package frc.robot.subsystems;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.util.REVDigitBoard;

public class Display extends SubsystemBase{

    private REVDigitBoard displayBoard;
    private NetworkTableEntry oneD1Entry;
    private NetworkTableEntry oneD2Entry;
    private NetworkTableEntry oneD3Entry;
    private NetworkTableEntry oneD4Entry;
    private NetworkTableEntry oneD5Entry;
    private NetworkTableEntry oneD6Entry;
    private NetworkTableEntry oneD7Entry;
    private NetworkTableEntry oneD8Entry;
    private NetworkTableEntry twoD1Entry;
    private NetworkTableEntry twoD2Entry;
    private NetworkTableEntry twoD3Entry;
    private NetworkTableEntry twoD4Entry;
    private NetworkTableEntry twoD5Entry;
    private NetworkTableEntry twoD6Entry;
    private NetworkTableEntry twoD7Entry;    
    private NetworkTableEntry twoD8Entry;
    private NetworkTableEntry stringEntry;
    private String state;
    private boolean AButtonReleased;
    DecimalFormat df_obj;
    private Timer idleTimer;
    private boolean BButtonReleased;

    public Display(){
        displayBoard = new REVDigitBoard();
        oneD1Entry = Shuffleboard.getTab("Display").add("1D1",false).getEntry();
        oneD2Entry = Shuffleboard.getTab("Display").add("1D2",false).getEntry();
        oneD3Entry = Shuffleboard.getTab("Display").add("1D3",false).getEntry();
        oneD4Entry = Shuffleboard.getTab("Display").add("1D4",false).getEntry();
        oneD5Entry = Shuffleboard.getTab("Display").add("1D5",false).getEntry();
        oneD6Entry = Shuffleboard.getTab("Display").add("1D6",false).getEntry();
        oneD7Entry = Shuffleboard.getTab("Display").add("1D7",false).getEntry();
        oneD8Entry = Shuffleboard.getTab("Display").add("1D8",false).getEntry();
        twoD1Entry = Shuffleboard.getTab("Display").add("2D1",false).getEntry();
        twoD2Entry = Shuffleboard.getTab("Display").add("2D2",false).getEntry();
        twoD3Entry = Shuffleboard.getTab("Display").add("2D3",false).getEntry();
        twoD4Entry = Shuffleboard.getTab("Display").add("2D4",false).getEntry();
        twoD5Entry = Shuffleboard.getTab("Display").add("2D5",false).getEntry();
        twoD6Entry = Shuffleboard.getTab("Display").add("2D6",false).getEntry();
        twoD7Entry = Shuffleboard.getTab("Display").add("2D7",false).getEntry();
        twoD8Entry = Shuffleboard.getTab("Display").add("2D8",false).getEntry();
        stringEntry = Shuffleboard.getTab("Display").add("String","3487").getEntry();
        state = "idle";
        AButtonReleased = true;
        df_obj = new DecimalFormat("##.0");
        displayBoard.clear();
        idleTimer = new Timer();
        idleTimer.start();
        BButtonReleased = true;
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
        if(idleTimer.get() < 0.2){
            displayBoard.displayText("   3",debug);
            return;
        }
        if(idleTimer.get() < 0.4){
            displayBoard.displayText("  34",debug);
            return;
        }
        if(idleTimer.get() < 0.6){
            displayBoard.displayText(" 348",debug);
            return;
        }
        if(idleTimer.get() < 0.8){
            displayBoard.displayText("3487",debug);
            return;
        }
        if(idleTimer.get() < 1){
            displayBoard.displayText("487 ",debug);
            return;
        }
        if(idleTimer.get() < 1.2){
            displayBoard.displayText("87  ",debug);
            return;
        }
        if(idleTimer.get() < 1.4){
            displayBoard.displayText("7   ",debug);
            return;
        }
        if(idleTimer.get() < 1.6){
            displayBoard.displayText("    ",debug);
            return;
        }
        if(idleTimer.get() > 2.6){
            idleTimer.reset();
        }
    }

    public void idle(){
        idle(false);
    }
    public void customDisplay(Boolean debug){
        displayBoard.displayText(stringEntry.getString("3487"),debug);
    }
    public void customDisplay(){
        customDisplay(false);
    }
}
