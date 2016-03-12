import java.util.ArrayList;

import javax.naming.BinaryRefAddr;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


public class Cipher_Key {
	String[][] key0 = new String[4][4];
	
	String[][] Rcon = new String[][]{
			{ "1", "2", "4", "8", "10", "20", "40", "80", "1b", "36"},
			{ "0", "0", "0", "0", "0",  "0",  "0", "0", "0", "0",},
			{ "0", "0", "0", "0", "0",  "0",  "0", "0", "0", "0",},
			{ "0", "0", "0", "0", "0",  "0",  "0", "0", "0", "0",},
			};
	
	ArrayList<String2DArrays> KEYS = new ArrayList<String2DArrays>();//one for each round
	
	String[] before1column= new String[4];//last column of previous array
	String[] before4columns= new String[4];//first column of previous array
	public Cipher_Key(String[][] _key0)
	{
		key0 = _key0;
		Calculate10RoundKeys();
	}
	
	public void Calculate10RoundKeys() //9 rounds + final round
	{
		for(int i=0;i<10;i++)
		{
			KEYS.add(i, new String2DArrays());
			for(int j=0;j<4;j++)
			{
				if(i==0)
				{
					if(j==0)   //Save the arrays we need
					{          //1rst array, 1rst column
						before1column[0]=key0[0][3];//column 4 of previous array
						before1column[1]=key0[1][3];
						before1column[2]=key0[2][3];
						before1column[3]=key0[3][3];

						before4columns[0]=key0[0][0];
						before4columns[1]=key0[1][0];
						before4columns[2]=key0[2][0];
						before4columns[3]=key0[3][0];
						String[] currentcolumn = new String[4];//temporary column to save
						currentcolumn= CalculateColumn1(before1column, before4columns, i);
						KEYS.get(i).AddColumnAt(currentcolumn, j);
					}
					else//1rst array, 2,3,4 column
					{
						before1column[0]=KEYS.get(i).array[0][j-1];//column 4 of previous array
						before1column[1]=KEYS.get(i).array[1][j-1]; 
						before1column[2]=KEYS.get(i).array[2][j-1];
						before1column[3]=KEYS.get(i).array[3][j-1];

						before4columns[0]=key0[0][j];
						before4columns[1]=key0[1][j];
						before4columns[2]=key0[2][j];
						before4columns[3]=key0[3][j];

						String[] currentcolumn = new String[4];//temporary column to save
						currentcolumn= CalculateOtherColumns(before1column, before4columns);
						KEYS.get(i).AddColumnAt(currentcolumn, j);
					}
				}
				else //other arrays
				{
					if(j==0) 
					{          //other array, 1rst column
						before1column[0]=KEYS.get(i-1).array[0][3];//column 4(3: starting from zero) of previous array
						before1column[1]=KEYS.get(i-1).array[1][3];
						before1column[2]=KEYS.get(i-1).array[2][3];
						before1column[3]=KEYS.get(i-1).array[3][3];

						before4columns[0]=KEYS.get(i-1).array[0][0];//column 1(0: starting from zero) of previous array
						before4columns[1]=KEYS.get(i-1).array[1][0];
						before4columns[2]=KEYS.get(i-1).array[2][0];
						before4columns[3]=KEYS.get(i-1).array[3][0];
						String[] currentcolumn = new String[4];//column to save
						currentcolumn= CalculateColumn1(before1column, before4columns, i);
						KEYS.get(i).AddColumnAt(currentcolumn, j); //create array and add column
					}
					else//other array, 2,3,4 column
					{
						before1column[0]=KEYS.get(i).array[0][j-1];
						before1column[1]=KEYS.get(i).array[1][j-1]; 
						before1column[2]=KEYS.get(i).array[2][j-1];
						before1column[3]=KEYS.get(i).array[3][j-1];

						before4columns[0]=KEYS.get(i-1).array[0][j];//column 1(0: starting from zero) of previous array
						before4columns[1]=KEYS.get(i-1).array[1][j];
						before4columns[2]=KEYS.get(i-1).array[2][j];
						before4columns[3]=KEYS.get(i-1).array[3][j];

						String[] currentcolumn = new String[4];//column to save
						currentcolumn= CalculateOtherColumns(before1column, before4columns);
						KEYS.get(i).AddColumnAt(currentcolumn, j); //create array and add column
					}
				}
			}
		}
	}

	public String[] CalculateColumn1(String[] b1, String b4[], int iter)//i: current iteration, needed for RCon column
	{
		String[] result= new String[4];
		//Rot world
		result[0]=b1[1];
		result[1]=b1[2];
		result[2]=b1[3];
		result[3]=b1[0];
		
		//SubBytes
		result=Encryption_Process.SubBytes1DArray(result);
	
		//System.out.println("KEY SUBBYTES: "+result[0]+" : "+result[1]+" , "+result[2] +" , " +result[3]);
		
		//TripleXOR
		String[] Rcontemp= getNextRConColumn(iter);
		for(int i=0;i<4;i++)//from hex -> binary  ,   in the second: result i is already in binary
		{
			result[i]= Encryption_Process.getXORResult2(Encryption_Process.HexToBinaryAndPadding(b4[i]), Encryption_Process.HexToBinaryAndPadding(result[i]))   ;
			result[i]= Encryption_Process.getXORResult2(result[i], Encryption_Process.HexToBinaryAndPadding(Rcontemp[i]));
			
			//from binary: to integer -> write hex to result[i]
			result[i]= Encryption_Process.BinaryToHexAndPadding(result[i]);
		}
		//System.out.println("KEY final: "+result[0]+" : "+result[1]+" , "+result[2] +" , " +result[3]);
		return result;
	}
	public String[] getNextRConColumn(int iter)
	{
		String[] s = new String[4];
		s[0]= Rcon[0][iter];
		s[1]= Rcon[1][iter];
		s[2]= Rcon[2][iter];
		s[3]= Rcon[3][iter];
		return s;
		
	}

	public String[] CalculateOtherColumns(String[] b1, String b4[])
	{
		String[] result = new String[4];
		for(int i=0;i<4;i++)
		{
			result[i]= Encryption_Process.getXORResult2(Encryption_Process.HexToBinaryAndPadding(b4[i]),
					                                    Encryption_Process.HexToBinaryAndPadding(b1[i]));
			

			result[i]= Encryption_Process.BinaryToHexAndPadding(result[i]);
		}
		//System.out.println("b1: "+b1[0]+" : "+b1[1]+" , "+b1[2] +" , " +b1[3]);
		//System.out.println("b4: "+b4[0]+" : "+b4[1]+" , "+b4[2] +" , " +b4[3]);
		//System.out.println("XOR OTHERS: "+result[0]+" : "+result[1]+" , "+result[2] +" , " +result[3]);
		return result;
	}
}
