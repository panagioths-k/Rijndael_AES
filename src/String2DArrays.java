
public class String2DArrays {

	int rows;
	int columns;
	String[][] array;
	
	public String2DArrays()
	{
		rows=4;
		columns=4;
		array = new String[4][4];
	}
	
	public String2DArrays(String[][] arr)
	{
		rows=4;
		columns=4;
		array = arr;
	}
	public static void Print2dStringArray(String[][] arr)
	{
		System.out.println("Printing array");
		for(int i =0;i< 4 ;i++)
			System.out.println(arr[i][0]+" "+arr[i][1]+" "+arr[i][2]+" "+arr[i][3]);	
	}
	
	public void AddColumnAt(String[] clmn, int index)//Add a column in my array
	{
		array[0][index]= clmn[0];
		array[1][index]= clmn[1];
		array[2][index]= clmn[2];
		array[3][index]= clmn[3];	
	}
	
	
	
	
	
	
}
