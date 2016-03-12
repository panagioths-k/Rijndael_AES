
public class Encryption_Process {
	static String[][] S_Box = new String[][]{
			{ "63", "7c", "77", "7b", "f2", "6b", "6f", "c5", "30", "01", "67", "2b", "fe", "d7", "ab", "76" },
			{ "ca", "82", "c9", "7d", "fa", "59", "47", "f0", "ad", "d4", "a2", "af", "9c", "a4", "72", "c0" },
			{ "b7", "fd", "93", "26", "36", "3f", "f7", "cc", "34", "a5", "e5", "f1", "71", "d8", "31", "15" },
			{ "04", "c7", "23", "c3", "18", "96", "05", "9a", "07", "12", "80", "e2", "eb", "27", "b2", "75" },
			{ "09", "83", "2c", "1a", "1b", "6e", "5a", "a0", "52", "3b", "d6", "b3", "29", "e3", "2f", "84" },
			{ "53", "d1", "00", "ed", "20", "fc", "b1", "5b", "6a", "cb", "be", "39", "4a", "4c", "58", "cf" }, 
			{ "d0", "ef", "aa", "fb", "43", "4d", "33", "85", "45", "f9", "02", "7f", "50", "3c", "9f", "a8" },
			{ "51", "a3", "40", "8f", "92", "9d", "38", "f5", "bc", "b6", "da", "21", "10", "ff", "f3", "d2" },
			{ "cd", "0c", "13", "ec", "5f", "97", "44", "17", "c4", "a7", "7e", "3d", "64", "5d", "19", "73" },
			{ "60", "81", "4f", "dc", "22", "2a", "90", "88", "46", "ee", "b8", "14", "de", "5e", "0b", "db" },
			{ "e0", "32", "3a", "0a", "49", "06", "24", "5c", "c2", "d3", "ac", "62", "91", "95", "e4", "79" },
			{ "e7", "c8", "37", "6d", "8d", "d5", "4e", "a9", "6c", "56", "f4", "ea", "65", "7a", "ae", "08" },
			{ "ba", "78", "25", "2e", "1c", "a6", "b4", "c6", "e8", "dd", "74", "1f", "4b", "bd", "8b", "8a" },
			{ "70", "3e", "b5", "66", "48", "03", "f6" ,"0e", "61", "35", "57", "b9", "86", "c1", "1d", "9e" },
			{ "e1", "f8", "98", "11", "69", "d9", "8e", "94", "9b", "1e", "87", "e9", "ce", "55", "28", "df" },
			{ "8c", "a1", "89", "0d", "bf", "e6", "42", "68", "41", "99", "2d", "0f", "b0", "54", "bb", "16" },		
				};
			

	static int[][] Coordinate_vector = new int[][]{
		    { 2, 3, 1, 1},
		    { 1, 2, 3, 1},
		    { 1, 1, 2, 3},
		    { 3, 1, 1, 2},
	};


	
	public Encryption_Process()
	{
		
	}
	
	public static String[][] AddRoundKey(String[][] data, String[][] key)
	{
		String[][] result = new String[4][4];
		String[] s1 = new String[8];
		String[] s2 = new String[8];
		
		for(int i =0;i< 4 ;i++)
			for(int j =0;j< 4 ;j++)
			{
				s1= toBinaryArray(data[i][j]);
				s2= toBinaryArray(key[i][j]);
				
				result[i][j]= Convert8BitArrayToHex(  getXORResult(s1, s2) );
			}
		return result;
	}
	
	public static String[] toBinaryArray(String s)
	{
		int num = Integer.parseInt(s, 16);
		
	    String bin = Integer.toBinaryString(num);
	  //  System.out.println(s+": to int: "+num+" to String:"+bin);
	    
	    String[] toarr = new String[8];
	    int n=8;//1 byte has 8 bits
	    int k=bin.length()-1;
	  //  System.out.println("bin size: "+(k+1));
	    for(int i=7;i>=0;i--)
	    {
	    	toarr[i]="0";
	    	try
	    	{
	    		toarr[i]= String.valueOf( bin.charAt(k) );
	    		k--;
	    	//	System.out.println("i:"+i +" "+(bin.charAt( k)));
	    	}
	    	catch(Exception e)
	    	{
	    		//System.out.println("NOT FOUND: i:"+i +" pos:"+( i ));
	    	}
	    }
	    //printing
	    for(int j=0;j<8;j++)
	    {
	    //	System.out.println("j:"+j +" toarr:"+toarr[j] );
	    }
	    
	    return toarr;
	}

	public static int[] getXORResult(String[] s1, String[] s2)//Works with arrays, Gets 2, 2d string arrays and performs XOR 
	{
		//System.out.println("XOR:");
		int[] tempXOR = new int[8];
		for(int l =0;l< 8 ;l++)
		{
			tempXOR[l] = Integer.parseInt( s1[l] )^Integer.parseInt( s2[l] );
		//	System.out.println(tempXOR[l]+"= "+s1[l]+"^"+s2[l]);
		}
		return tempXOR;
	}
	public static String getXORResult2(String s1, String s2)//This one accepts simple strings and returns string
	{
	//	System.out.println("XOR:");
		String tempXOR="" ;
		for(int l =0;l< 8 ;l++)
			tempXOR += Character.forDigit( s1.charAt(l) ^ s2.charAt(l),2) ;
		
		return tempXOR;
	}
	
	public static String Convert8BitArrayToHex(int[] arr)//This method returns 2 hex chars in one String 
	{                                                //4,5,6,7 bits: 1rst hex     0,1,2,3 bits: 2nd hex  
	//	System.out.println("Back to hex:");
		String s1= Integer.toHexString( Integer.parseInt(""+ arr[4]+arr[5]+arr[6]+arr[7],2) );
		String s2= Integer.toHexString( Integer.parseInt(""+ arr[0]+arr[1]+arr[2]+arr[3],2) );
		//System.out.println("s1: "+s1+", s2: "+s2+", added: "+(s2+s1));
		
		return s2+s1;
	}

	public static String[][] SubBytes(String[][] arr)
	{
		int row, column;
		String[][] result = new String[4][4];
		for(int i =0;i< 4 ;i++)
			for(int j =0;j< 4 ;j++)//1rst char is the row,    2nd char is the column
			{
				row=    Integer.parseInt( String.valueOf( arr[i][j].charAt(0) ),16);
				column= Integer.parseInt( String.valueOf( arr[i][j].charAt(1) ),16 );
				result[i][j] = S_Box[row][column];
				//System.out.println("row: "+row+", column: "+column+", got: "+(result[i][j]));
			}
		return result;
	}
	
	public static String[] SubBytes1DArray(String[] arr) //for Key Scheduling
	{
		int  column=-1,row;
		String[] result = new String[4];
		for(int i =0;i< 4 ;i++)
			{
				row=    Integer.parseInt( String.valueOf( arr[i].charAt(0) ),16);
				//System.out.println("arr: "+arr[i]);
				column= Integer.parseInt( String.valueOf( arr[i].charAt(1) ),16 );
				result[i] = S_Box[row][column];
				//System.out.println("SubBytes1DArray**\nrow: "+row+", column: "+column+", got: "+(result[i]));
			}
		return result;
	}
	
	public static String[][] ShiftRows(String[][] arr)
	{
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<i;j++)
				LeftShift(arr, i);//shift 1,2,3 times 1rst,2nd,3rd row
		}
		return arr;
	}
	
	public static String[][] LeftShift(String[][] arr, int rowToShift)
	{
		String temp= arr[rowToShift][0];//save the first
		arr[rowToShift][0]=arr[rowToShift][1];
		arr[rowToShift][1]=arr[rowToShift][2];
		arr[rowToShift][2]=arr[rowToShift][3];
		arr[rowToShift][3]=temp;
		
		return arr;		
	}
	
	public static String[][] MixColumns(String[][] arr)
	{	
		//System.out.println("\n\n\n");
		String[] temparr= new String[4];        //column from data
		int[] coordinatetemparr= new int[4]; //row from Coordinate_vector 
		String BinaryResult;

		for(int n=0;n<4;n++)
		{
			temparr[0] = arr[0][n] ;   //Keep the l column
			temparr[1] = arr[1][n]; 
			temparr[2] = arr[2][n]; 
			temparr[3] = arr[3][n]; 
			for(int m=0;m<4;m++)
			{

			//	System.out.println(m+": !!!!temp: "+temparr[0]+" : "+temparr[1]+" , "+temparr[2] +" , " +temparr[3]);

				coordinatetemparr[0] = Coordinate_vector[m][0] ;   //Keep the l row
				coordinatetemparr[1] = Coordinate_vector[m][1]; 
				coordinatetemparr[2] = Coordinate_vector[m][2]; 
				coordinatetemparr[3] = Coordinate_vector[m][3]; 
			//	System.out.println(m+": !!!!coord: "+coordinatetemparr[0]+" : "+coordinatetemparr[1]+" , "+coordinatetemparr[2] +" , " +coordinatetemparr[3]);

				BinaryResult= MultiplyIntArrays( temparr, coordinatetemparr ); // row x column
				//System.out.println("GOT: "+BinaryResult);
				arr[m][n]=BinaryResult;
			}	
		}
		return arr;
	}

	public static String MultiplyIntArrays( String[] B , int[] coordinate) //4 Rows - 1 Column
	{
		String[] beforefinalxor = new String[4]; //save the result of middle actions 
		String   finalresult="";
		for(int k=0;k<B.length;k++)           //columns
		{
			//System.out.println("data: "+B[k]+ " coordinate:" +coordinate[k]);
			if(coordinate[k]==1)   //simple binary conversion
			{  
				//System.out.println("It is 1");
				beforefinalxor[k] = HexToBinaryAndPadding(B[k]);

			}
			else if(coordinate[k]==2)//Shift left and if (after shifting) msbit==1 XORwith27
			{
				//System.out.println("It is 2");
				beforefinalxor[k] = HexToBinaryAndPadding(B[k]);		
				beforefinalxor[k] = ShiftLeftAndXORwith27(beforefinalxor[k]);

			}
			else if(coordinate[k]==3) //  (result from 1) XOR (result from 2)
			{
				//System.out.println("It is 3");
				String s1 =  HexToBinaryAndPadding(B[k]);
				String s2 = ShiftLeftAndXORwith27(s1);
				//System.out.println("S1: "+s1+" S2: "+s2);
				beforefinalxor[k] = getXORResult2(s1, s2);
			}
			//System.out.println(k+": RESULT: *** "+beforefinalxor[k] +" ***");
		}	
		//XOR ALL 4 RESULTS, example: result = {02.d4} + {03.bf} + {01.5d} + {01.30} 
		//                            1011 0011 XOR 1101 1010 XOR 0101 1101 XOR 0011 0000

		finalresult=getXORResult2( getXORResult2(getXORResult2(beforefinalxor[0], beforefinalxor[1]), beforefinalxor[2]), beforefinalxor[3]);
		//System.out.println(B[0]+":"+beforefinalxor[0]+"  "+B[1]+":"+beforefinalxor[1]+"  "+B[2]+":"+beforefinalxor[2]+"   "+
			//	B[3]+":"+beforefinalxor[3]);
		
		//from binary: to integer -> to hex
		finalresult=Integer.toHexString(Integer.parseInt(finalresult,2));
		return finalresult;
		/**
       // int aRows = A.length;
      //  int aColumns = A[0].length;
      //  int bRows = B.length;
      //  int bColumns = 1;

        if (A[0].length != B.length) {
            throw new IllegalArgumentException("A:Rows: " + A[0].length + " did not match B:Columns " + B.length + ".");
        }
        int[] C = new int[A.length]; //result
        for (int i = 0; i < A.length; i++)          //Initialization
                C[i] = 0;
        
        for (int i = 0; i < A.length; i++)            // aRow
        	C[i] += A[i][0] * B[0] + A[i][1] * B[1] + A[i][2] * B[2] + A[i][3] * B[3] ;         
        return C;
        
        **/
    }	
	
	public static String ShiftLeftAndXORwith27(String str)
	{
		//1 shift left
		//XOR or with (00011011)binary = (27) decimal
		char[] chararr = str.toCharArray();
		char[] xor27   = "00011011".toCharArray();
		boolean flag = chararr[0]=='1';
		
		//System.out.println("chararr before"+(String.valueOf(chararr)) +" str= "+str);
		for(int i=0;i<chararr.length-1;i++)      //1 shift left
			chararr[i]=chararr[i+1];             
		chararr[chararr.length-1]='0';
		
		if(flag) //means most significant bit before shift is 1
		{
		//	System.out.println("DO XOR");
			for(int i=0;i<chararr.length;i++)    //XOR with (00011011)binary = (27)decimal and conversion from int to char
				chararr[i] = Character.forDigit( chararr[i]^xor27[i], 2);  
		}
		
		//System.out.println(str+" in 2 mode: "+(String.valueOf(chararr)));
		return String.valueOf(chararr);
	}
	
	public static String HexToBinaryAndPadding(String hex)
	{            
		String result="";
	
		                     //from hex: to integer -> to binary string
		String binary  = Integer.toBinaryString( Integer.parseInt(hex, 16) ); //converting
		int initial_length = binary.length();                               //padding with zeros
		int final_length = 8;
		
		for(int i=0;i<final_length-initial_length;i++)//add zeros first
		{
			result+="0";
		}
		result += binary;
		//System.out.println("from "+hex+" to "+result +" ");
		return result;
	}
	
	public static String BinaryToHexAndPadding(String bin)
	{            
		//from binary: to integer -> write hex to result
		String hexa=""+bin.charAt(0)+bin.charAt(1)+bin.charAt(2)+bin.charAt(3);
		hexa=Integer.toHexString(Integer.parseInt(hexa,2));
		
		String hexb=""+bin.charAt(4)+bin.charAt(5)+bin.charAt(6)+bin.charAt(7);
		hexb=Integer.toHexString(Integer.parseInt(hexb,2));
		
		return hexa+hexb;
	}
	
	public static String[][] asciiStringToHexArray(String asciiValue)
	{
		String[][] result = new String[4][4];
		char[] chars = asciiValue.toCharArray();
		StringBuffer hex = new StringBuffer();
		String tempStr;
		//System.out.println("ASCIITOHEX");
		int k=0;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
			{
				try
				{
					tempStr=Integer.toHexString((int) chars[k]);
				}catch(ArrayIndexOutOfBoundsException e)
				{
					tempStr="00";
				}
				if(tempStr.length()==1) tempStr="0"+ tempStr; //one letters are padded with zero: 7 => 07
				result[i][j]=tempStr;
				//hex.append(tempStr);
				// System.out.println(i+": " +tempStr);
				k++;
			}
		
		System.out.println("TESTING");
		String2DArrays.Print2dStringArray(result);
		
		return result;
	}
	
	public static String HexArrayToasciiString(String[][] arr)
	{
		String result="";
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
			{
				result+= (char)Integer.parseInt(arr[i][j], 16); 
			}
		return result;
			
	}
	
	public static String[][] HexToHexArray(String hex)//hex has 32 characters: going into pairs on 4x4 array, 2 on each cell
	{
		String[][] result = new String[4][4];
		int k=0;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
			{
				result[i][j]=""+hex.charAt(k) + hex.charAt(k+1);
				k+=2;
			}
		return result;
	}
	
	public static boolean CorrectHexFormat(String s)
	{
		boolean flag=true;
		for(int i=0;i<s.length();i++)
		{
			int ascii_value= (int) (s.charAt(i));
			// (0-9) or (a-f) or (A-F)
			if( !((ascii_value>=48 && ascii_value<=57) || (ascii_value>=65 && ascii_value<=70) ||
					(ascii_value>=97 && ascii_value<=102)))
			{
				flag=false;
				System.out.println("Error at: "+s.charAt(i)+": "+ ((int) s.charAt(i) ) );
			}
			
		}
		return flag;
	}

}
