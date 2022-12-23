package frc.robot.util;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.hal.simulation.SimDeviceCallback;
import edu.wpi.first.wpilibj.AnalogInput;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.IntPredicate;

public class REVDigitBoard {
	/*
	 * DOCUMENTATION::
	 * 
	 * REVDigitBoard() : constructor
	 * void display(String str) : displays the first four characters of the string (only alpha (converted to uppercase), numbers, and spaces)
	 * void display(double batt) : displays a decimal number (like battery voltage) in the form of 12.34 (ten-one-decimal-tenth-hundredth)
	 * void clear() : clears the display
	 * boolean getButtonA() : button A on the board
	 * boolean getButtonB() : button B on the board
	 * double getPot() : potentiometer value
	 */
	
	I2C i2c;
	DigitalInput buttonA, buttonB;
	AnalogInput pot;
	
	byte[][] charreg;
	HashMap<Character, Integer> charmap;
	
	public REVDigitBoard() {
		i2c = new I2C(Port.kMXP, 0x70);
		buttonA = new DigitalInput(19);
		buttonB = new DigitalInput(20);
		pot = new AnalogInput(7);
		
		byte[] osc = new byte[1];
	 	byte[] blink = new byte[1];
	 	byte[] bright = new byte[1];
	 	osc[0] = (byte)0x21;
	 	blink[0] = (byte)0x81;
	 	bright[0] = (byte)0xEF;

		i2c.writeBulk(osc);
		Timer.delay(.01);
		i2c.writeBulk(bright);
		Timer.delay(.01);
		i2c.writeBulk(blink);
		Timer.delay(.01);
		
		charreg = new byte[37][2]; //charreg is short for character registry
		charmap = new HashMap<Character, Integer>(); 
		
		charreg[0][0] = (byte)0b00111111; charreg[0][1] = (byte)0b00000000; //0
		charmap.put('0',0);
		charreg[1][0] = (byte)0b00000110; charreg[1][1] = (byte)0b00000100; //1
		charmap.put('1',1);
	 	charreg[2][0] = (byte)0b11011011; charreg[2][1] = (byte)0b00000000; //2
		charmap.put('2',2);
	 	charreg[3][0] = (byte)0b10001111; charreg[3][1] = (byte)0b00000000; //3
		charmap.put('3',3);
	 	charreg[4][0] = (byte)0b11100110; charreg[4][1] = (byte)0b00000000; //4
		charmap.put('4',4);
	 	charreg[5][0] = (byte)0b11101101; charreg[5][1] = (byte)0b00000000; //5
		charmap.put('5',5);
	 	charreg[6][0] = (byte)0b11111101; charreg[6][1] = (byte)0b00000000; //6
		charmap.put('6',6);
	 	charreg[7][0] = (byte)0b00000001; charreg[7][1] = (byte)0b00100100; //7
		charmap.put('7',7);
	 	charreg[8][0] = (byte)0b11111111; charreg[8][1] = (byte)0b00000000; //8
		charmap.put('8',8);
	 	charreg[9][0] = (byte)0b11100111; charreg[9][1] = (byte)0b00000000; //9
		charmap.put('9',9);

	 	charreg[10][0] = (byte)0b11110111; charreg[10][1] = (byte)0b00000000; //A
		charmap.put('A',10);
	 	charreg[11][0] = (byte)0b10001111; charreg[11][1] = (byte)0b00010010; //B
		charmap.put('B',11);
	 	charreg[12][0] = (byte)0b00111001; charreg[12][1] = (byte)0b00000000; //C
		charmap.put('C',12);
	 	charreg[13][0] = (byte)0b00001111; charreg[13][1] = (byte)0b00010010; //D
		charmap.put('D',13);
	 	charreg[14][0] = (byte)0b11111001; charreg[14][1] = (byte)0b00000000; //E
		charmap.put('E',14);
	 	charreg[15][0] = (byte)0b11110001; charreg[15][1] = (byte)0b00000000; //F
		charmap.put('F',15);
	 	charreg[16][0] = (byte)0b10111101; charreg[16][1] = (byte)0b00000000; //G
		charmap.put('G',16);
	 	charreg[17][0] = (byte)0b11110110; charreg[17][1] = (byte)0b00000000; //H
		charmap.put('H',17);
	 	charreg[18][0] = (byte)0b00001001; charreg[18][1] = (byte)0b00010010; //I
		charmap.put('I',18);
	 	charreg[19][0] = (byte)0b00011110; charreg[19][1] = (byte)0b00000000; //J
		charmap.put('J',19);
	 	charreg[20][0] = (byte)0b01110000; charreg[20][1] = (byte)0b00001100; //K
		charmap.put('K',20);
	 	charreg[21][0] = (byte)0b00111000; charreg[21][1] = (byte)0b00000000; //L
		charmap.put('L',21);
	 	charreg[22][0] = (byte)0b00110110; charreg[22][1] = (byte)0b00000101; //M
		charmap.put('M',22);
	 	charreg[23][0] = (byte)0b00110110; charreg[23][1] = (byte)0b00001001; //N
		charmap.put('N',23);
	 	charreg[24][0] = (byte)0b00111111; charreg[24][1] = (byte)0b00000000; //O
		charmap.put('O',24);
	 	charreg[25][0] = (byte)0b11110011; charreg[25][1] = (byte)0b00000000; //P
		charmap.put('P',25);
	 	charreg[26][0] = (byte)0b00111111; charreg[26][1] = (byte)0b00001000; //Q
		charmap.put('Q',26);
	 	charreg[27][0] = (byte)0b11110011; charreg[27][1] = (byte)0b00001000; //R
		charmap.put('R',27);
	 	charreg[28][0] = (byte)0b10001101; charreg[28][1] = (byte)0b00000001; //S
		charmap.put('S',28);
	 	charreg[29][0] = (byte)0b00000001; charreg[29][1] = (byte)0b00010010; //T
		charmap.put('T',29);
	 	charreg[30][0] = (byte)0b00111110; charreg[30][1] = (byte)0b00000000; //U
		charmap.put('U',30);
	 	charreg[31][0] = (byte)0b00110000; charreg[31][1] = (byte)0b00100100; //V
		charmap.put('V',31);
	 	charreg[32][0] = (byte)0b00110110; charreg[32][1] = (byte)0b00101000; //W
		charmap.put('W',32);
	 	charreg[33][0] = (byte)0b00000000; charreg[33][1] = (byte)0b00101101; //X
		charmap.put('X',33);
	 	charreg[34][0] = (byte)0b00000000; charreg[34][1] = (byte)0b00010101; //Y
		charmap.put('Y',34);
	 	charreg[35][0] = (byte)0b00001001; charreg[35][1] = (byte)0b00100100; //Z
		charmap.put('Z',35);
		charreg[36][0] = (byte)0b00000000; charreg[36][1] = (byte)0b00000000; //space
		charmap.put(' ',36);
	}

	public void displayRaw(byte first, byte last){
		byte[] byte1 = new byte[10];
		byte1[0] = (byte)(0b0000111100001111);
 		byte1[2] = first;
 		byte1[3] = last;
 		byte1[4] = first;
 		byte1[5] = last;
 		byte1[6] = first;
 		byte1[7] = last;
 		byte1[8] = first;
 		byte1[9] = last;
 		//send the array to the board
 		i2c.writeBulk(byte1);
	}

	public Byte[] toDigitArray(String input){
		ArrayList<Byte> output = new ArrayList<Byte>();
		int i = 0;
		input = input.toUpperCase();
		while(input.length() < 5){
			input = input.concat(" ");
		}
		while(output.size() < 8){
			if(input.charAt(i) == '.'){
				if(output.size() < 1){
					i++;
					continue;
				}
				output.set(output.size()-1, byteOr(output.get(output.size()-1), (byte)0b01000000));
				i++;
				continue;
				
			}
			output.add(charreg[charmap.get(input.charAt(i))][0]);
			output.add(charreg[charmap.get(input.charAt(i))][1]);
			i++;
		}
		if(input.charAt(input.length()-1) == '.'){
			output.set(output.size()-1, byteOr(output.get(output.size()-1), (byte)0b01000000));
		}
		Byte[] outputArray = {};
		outputArray = output.toArray(outputArray);
		return outputArray;
	}

	public void displayText(String text){
		Byte[] bytez = toDigitArray(text);
		byte[] outputByte = new byte[10];
		outputByte[0] = (byte)(0b0000111100001111);
		outputByte[2] = bytez[6];
		outputByte[3] = bytez[7];
		outputByte[4] = bytez[4];
		outputByte[5] = bytez[5];
		outputByte[6] = bytez[2];
		outputByte[7] = bytez[3];
		outputByte[8] = bytez[0];
		outputByte[9] = bytez[1];
		i2c.writeBulk(outputByte);
		System.out.println(text);
	}
	
	
	public void clear() {
		 int[] charz = {36,36,36,36}; // whyy java
		 this._display(charz);
	 }
	 
	public boolean getButtonA() {
		 return buttonA.get();
	 }
	public boolean getButtonB() {
		 return buttonB.get();
	 }
	public double getPot() {
		 return pot.getVoltage();
	 }
	
////// not supposed to be publicly used..
	
	void _display(int[] charz) {
		byte[] byte1 = new byte[10];
		byte1[0] = (byte)(0b0000111100001111);
 		byte1[2] = charreg[charz[3]][0];
 		byte1[3] = charreg[charz[3]][1];
 		byte1[4] = charreg[charz[2]][0];
 		byte1[5] = charreg[charz[2]][1];
 		byte1[6] = charreg[charz[1]][0];
 		byte1[7] = charreg[charz[1]][1];
 		byte1[8] = charreg[charz[0]][0];
 		byte1[9] = charreg[charz[0]][1];
 		//send the array to the board
 		i2c.writeBulk(byte1);
	}
	
	String repeat(char c, int n) {
	    char[] arr = new char[n];
	    Arrays.fill(arr, c);
	    return new String(arr);
	}

	byte byteOr(byte first, byte second){
		BitSet firstBitSet = BitSet.valueOf(new byte[] {first});
		BitSet secondBitSet = BitSet.valueOf(new byte[] {second});
		firstBitSet.or(secondBitSet);
		return firstBitSet.toByteArray()[0];

	}
	
}