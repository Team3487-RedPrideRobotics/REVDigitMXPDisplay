package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
    }

    @Override
    public void periodic(){
        byte[] bytez = getNetworkBytes();
        byte first;
        byte last;
        if(bytez.length == 0){
            first=(byte)0b00000000;
            last=(byte)0b00000000;
        }else if(bytez.length == 1){
            first=bytez[0];
            last=(byte)0b00000000;
        }else{
            first=bytez[0];
            last=bytez[1];
        }
        displayBoard.displayRaw(first, last);
    }

    @Override
    public void simulationPeriodic(){

    }

    public void displayVoltage(){
        double voltage = RobotController.getBatteryVoltage();
        
    }

    public byte[] getNetworkBytes(){
        Boolean[] input = new Boolean[] {
            oneD1Entry.getBoolean(false),
            oneD2Entry.getBoolean(false),
            oneD3Entry.getBoolean(false),
            oneD4Entry.getBoolean(false),
            oneD5Entry.getBoolean(false),
            oneD6Entry.getBoolean(false),
            oneD7Entry.getBoolean(false),
            oneD8Entry.getBoolean(false),
            twoD1Entry.getBoolean(false),
            twoD2Entry.getBoolean(false),
            twoD3Entry.getBoolean(false),
            twoD4Entry.getBoolean(false),
            twoD5Entry.getBoolean(false),
            twoD6Entry.getBoolean(false),
            twoD7Entry.getBoolean(false),
            twoD8Entry.getBoolean(false)
        };

        SmartDashboard.putBooleanArray("Array",input);


        BitSet m_bitArray = new BitSet(16);
        for(int i=0;i<15;i++){
            m_bitArray.set(i,input[i]);
        }

        SmartDashboard.putRaw("Thingy", m_bitArray.toByteArray());

        return m_bitArray.toByteArray();
    }
}
