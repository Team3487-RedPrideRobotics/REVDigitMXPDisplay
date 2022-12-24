package frc.robot.util;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;

import java.util.*;

public class REVDigitMXPDisplay {
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
	private boolean AButtonReleased;
	private boolean BButtonReleased;
	
	public REVDigitMXPDisplay() {
		i2c = new I2C(Port.kMXP, 0x70);
		buttonA = new DigitalInput(19);
		buttonB = new DigitalInput(20);
		pot = new AnalogInput(7);

		AButtonReleased = true;
		BButtonReleased = true;
		
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
		
		charreg = new byte[86][2]; //charreg is short for character registry
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
		charreg[37][0] = (byte)0b11011011; charreg[37][1] = (byte)0b00010010; //$
		charmap.put('$',37);
		charreg[38][0] = (byte)0b00000000; charreg[38][1] = (byte)0b00000010; //apostrophe
		charmap.put('\'',38);
		charreg[39][0] = (byte)0b00111001; charreg[39][1] = (byte)0b00000000; //[
		charmap.put('[',39);
		charreg[40][0] = (byte)0b00001111; charreg[40][1] = (byte)0b00000000; //]
		charmap.put(']',40);
		charreg[41][0] = (byte)0b00000000; charreg[41][1] = (byte)0b00111111; //star
		charmap.put('*',41);
		charreg[42][0] = (byte)0b11000000; charreg[42][1] = (byte)0b00010010; //plus
		charmap.put('+',42);
		charreg[43][0] = (byte)0b00000000; charreg[43][1] = (byte)0b00100000; //comma
		charmap.put(',',43);
		charreg[44][0] = (byte)0b11000000; charreg[44][1] = (byte)0b00000000; //minus
		charmap.put('-',44);
		charreg[45][0] = (byte)0b00000000; charreg[45][1] = (byte)0b00100100; //slash
		charmap.put('/',45);
		charreg[46][0] = (byte)0b00000000; charreg[46][1] = (byte)0b00010010; //pipe
		charmap.put('|',46);
		charreg[47][0] = (byte)0b00000000; charreg[47][1] = (byte)0b00001100; //less than
		charmap.put('<',47);
		charreg[48][0] = (byte)0b00000000; charreg[48][1] = (byte)0b00100001; //Greater than
		charmap.put('>',48);
		charreg[49][0] = (byte)0b11001000; charreg[49][1] = (byte)0b00000000; //Equals
		charmap.put('=',49);
		charreg[50][0] = (byte)0b00000000; charreg[50][1] = (byte)0b00001001; //Backslash
		charmap.put('\\',50);
		charreg[51][0] = (byte)0b00001000; charreg[51][1] = (byte)0b00000000; //Underscore
		charmap.put('_',51);
		charreg[52][0] = (byte)0b00000000; charreg[52][1] = (byte)0b00000001; // ` <- this thingy
		charmap.put('`',52);
		charreg[53][0] = (byte)0b11011111; charreg[53][1] = (byte)0b00000000; // a
		charmap.put('a',53);
		charreg[54][0] = (byte)0b10001110; charreg[54][1] = (byte)0b00100000; // d
		charmap.put('d',54);
		charreg[55][0] = (byte)0b01111000; charreg[55][1] = (byte)0b00001000; // b
		charmap.put('b',55);
		charreg[56][0] = (byte)0b11011000; charreg[56][1] = (byte)0b00000000; // c
		charmap.put('c',56);
		charreg[57][0] = (byte)0b01111001; charreg[57][1] = (byte)0b00000000; // e
		charmap.put('e',57);
		charreg[58][0] = (byte)0b01110001; charreg[58][1] = (byte)0b00000000; // f
		charmap.put('f',58);
		charreg[59][0] = (byte)0b10001111; charreg[59][1] = (byte)0b00000001; // g
		charmap.put('g',59);
		charreg[60][0] = (byte)0b11110100; charreg[60][1] = (byte)0b00000000; // h
		charmap.put('h',60);
		charreg[61][0] = (byte)0b00000000; charreg[61][1] = (byte)0b00010000; // i
		charmap.put('i',61);
		charreg[62][0] = (byte)0b00001110; charreg[62][1] = (byte)0b00000000; // j
		charmap.put('j',62);
		charreg[63][0] = (byte)0b00000000; charreg[63][1] = (byte)0b00011110; // k
		charmap.put('k',63);
		charreg[64][0] = (byte)0b00000000; charreg[64][1] = (byte)0b00010010; // l
		charmap.put('l',64);
		charreg[65][0] = (byte)0b11010100; charreg[65][1] = (byte)0b00010000; // m
		charmap.put('m',65);
		charreg[66][0] = (byte)0b01010000; charreg[66][1] = (byte)0b00001000; // n
		charmap.put('n',66);
		charreg[67][0] = (byte)0b11011100; charreg[67][1] = (byte)0b00000000; // o
		charmap.put('o',67);
		charreg[68][0] = (byte)0b01110001; charreg[68][1] = (byte)0b00000100; // p
		charmap.put('p',68);
		charreg[69][0] = (byte)0b11100011; charreg[69][1] = (byte)0b00001000; // q
		charmap.put('q',69);
		charreg[70][0] = (byte)0b01010000; charreg[70][1] = (byte)0b00000000; // r
		charmap.put('r',70);
		charreg[71][0] = (byte)0b10001101; charreg[71][1] = (byte)0b00000001; // s
		charmap.put('s',71);
		charreg[72][0] = (byte)0b01111000; charreg[72][1] = (byte)0b00000000; // t
		charmap.put('t',72);
		charreg[73][0] = (byte)0b00011100; charreg[73][1] = (byte)0b00000000; // u
		charmap.put('u',73);
		charreg[74][0] = (byte)0b00010000; charreg[74][1] = (byte)0b00100000; // v
		charmap.put('v',74);
		charreg[75][0] = (byte)0b00010100; charreg[75][1] = (byte)0b00101000; // w
		charmap.put('w',75);
		charreg[76][0] = (byte)0b00000000; charreg[76][1] = (byte)0b00101101; // x
		charmap.put('x',76);
		charreg[77][0] = (byte)0b10000111; charreg[77][1] = (byte)0b00000010; // y
		charmap.put('y',77);
		charreg[78][0] = (byte)0b00001001; charreg[78][1] = (byte)0b00100100; // z
		charmap.put('z',78);
		charreg[79][0] = (byte)0b00000000; charreg[79][1] = (byte)0b00010010; // colon
		charmap.put(':',79);
		charreg[80][0] = (byte)0b00100010; charreg[80][1] = (byte)0b00000000; // double quote
		charmap.put('"',80);
		charreg[81][0] = (byte)0b00000000; charreg[81][1] = (byte)0b00101000; // caret
		charmap.put('^',81);
		charreg[82][0] = (byte)0b00111001; charreg[82][1] = (byte)0b00000000; // open parenthesis
		charmap.put('(',82);
		charreg[83][0] = (byte)0b00001111; charreg[83][1] = (byte)0b00000000; // close parenthesis
		charmap.put(')',83);
		charreg[84][0] = (byte)0b00111001; charreg[84][1] = (byte)0b00000000; // left curly brace
		charmap.put('{',84);
		charreg[85][0] = (byte)0b00001111; charreg[85][1] = (byte)0b00000000; // right curly brace
		charmap.put('}',85);
	}	

	
	/**
	 * Writes string to the display board
	 * @param text the text to be written.
	 * Characters after the fourth (excluding periods) will be ignored, as will periods placed before the first occurence of a non-period character.
	 * Strings with less than four non-period character will be padded with spaces to the right.
	 * @param debug will print out the string to be displayed if true
	 */
	public void displayText(String text, Boolean debug){
		String outputString = "";
		ArrayList<Byte> output = new ArrayList<Byte>();
		int i = 0;
		text = text.concat("     ");
		while(output.size() < 8){
			if(text.charAt(i) == '.'){
				if(output.size() < 1){
					i++;
					continue;
				}
				// right shift last byte by six so that the sixth bit is in the 2^0 place. AND with one to eliminate all other digits, then check equivalence with 1.
				if((output.get(output.size()-1) >> 6 & 1) == 1){
					output.add((byte)0b00000000);
					output.add((byte)0b01000000);
					outputString = outputString.concat(" .");
					i++;
					continue;
				}
				output.set(output.size()-1, byteOr(output.get(output.size()-1), (byte)0b01000000));
				outputString = outputString.concat(".");
				i++;
				continue;
				
			}
			try{
				output.add(charreg[charmap.get(text.charAt(i))][0]);
				output.add(charreg[charmap.get(text.charAt(i))][1]);
				outputString = outputString.concat(Character.toString(text.charAt(i)));
			}catch(IndexOutOfBoundsException|NullPointerException e){
				System.out.println("Attempted to display illegal character to REVDigitBoard - '" + text.charAt(i)+"'");
				e.printStackTrace();
			}	
			i++;
		}
		if(text.charAt(i) == '.'){
			output.set(output.size()-1, byteOr(output.get(output.size()-1), (byte)0b01000000));
			outputString = outputString.concat(".");
		}
		
		Byte[] bytez = {};
		bytez = output.toArray(bytez);
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
		if(debug){
			System.out.println(outputString);
		}
	}

	/**
	 * Writes string to the display board
	 * @param text the text to be written.
	 * Characters after the fourth (excluding periods) will be ignored, as will periods placed before the first occurence of a non-period character.
	 * Strings with less than four non-period character will be padded with spaces to the right.
	 */
	public void displayText(String text){
		displayText(text, false);
	}
	
	
	public void clear() {
		 displayText("    ");
	 }
	 /**
	  * 
	  * @return true if button is released, false if button is held
	  */
	public boolean getButtonA() {
		 return buttonA.get();
	 }
	 /**
	  * 
	  * @return true if button is released, false is button is held
	  */
	public boolean getButtonB() {
		 return buttonB.get();
	 }
	/**
	 * 
	 * @return voltage running through potentiometer
	 */
	public double getPot() {
		 return pot.getVoltage();
	 }

	 /**
	 * 
	 * @return true the first function call that the a button is pressed for
	 */
	 public boolean getAButtonPressed(){
        if(!getButtonA() && AButtonReleased){
            AButtonReleased = false;
            return true;
        }
        AButtonReleased = getButtonA();
        return false;
    }

	/**
	 * 
	 * @return true the first function call that the b button is pressed for
	 */
	public boolean getBButtonPressed(){
        if(!getButtonB() && BButtonReleased){
            BButtonReleased = false;
            return true;
        }
        BButtonReleased = getButtonB();
        return false;
    }
	
////// not supposed to be publicly used..

	private byte byteOr(byte first, byte second){
		BitSet firstBitSet = BitSet.valueOf(new byte[] {first});
		BitSet secondBitSet = BitSet.valueOf(new byte[] {second});
		firstBitSet.or(secondBitSet);
		return firstBitSet.toByteArray()[0];

	}
}