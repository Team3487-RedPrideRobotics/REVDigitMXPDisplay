package frc.robot.subsystems;

import java.text.DecimalFormat;

import org.usfirst.frc.team3487.REVDigitMXPDisplay;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
        df_obj = new DecimalFormat("##.0V");
        displayBoard.setDebug(true);
        displayBoard.clear();
        idleTimer = new Timer();
        idleTimer.start();
    }

    @Override
    public void periodic(){
        infoLoop();
    }

    public void battery() {
        displayBoard.displayBattery();
        
    }
    

    private void infoLoop(){
        if(state=="idle"){
            if(displayBoard.getAButtonPressed()){
                state="battery";
            }
            if(displayBoard.getBButtonPressed()){
                state="custom";
            }
            idle();
        }else if(state=="battery"){
            if(displayBoard.getAButtonPressed()){
                state="idle";
            }
            if(displayBoard.getBButtonPressed()){
                state="custom";
            }
            battery();
        }else if(state=="custom"){
            if(displayBoard.getAButtonPressed()){
                state="battery";
            }
            if(displayBoard.getBButtonPressed()){
                state="idle";
            }
            customDisplay();
        }
    }

    @Override
    public void simulationPeriodic(){
    }
    public void idle(){
        displayBoard.displayScrollText("3487", 0.2);
    }

    public void customDisplay(){
        displayBoard.displayScrollText(stringEntry.getString("3487"), 0.2);
    }
}
