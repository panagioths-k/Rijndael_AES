import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyRep;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;



public class AES_Frame extends JFrame {
	int timerun; //times the encryption process is going to run, depends on the plain text
	public static final double BLOCK_SIZE = 16f;

	String[][] State0 = new String[4][4];
/**	String[][] State0 = new String[][]{
			{ "32", "88", "31", "e0"},
			{ "43", "5a", "31", "37"},
			{ "f6", "30", "98", "07"},
			{ "a8", "8d", "a2", "34"},
			};
**/
	 String[][] key0 = new String[4][4];
/** String[][] key0 = new String[][]{ //Cipher Key for Initial Round
			{ "2b", "28", "ab", "09"},
			{ "7e", "ae", "f7", "cf"},
			{ "15", "d2", "15", "4f"},
			{ "16", "a6", "88", "3c"},
			};
**/
    String[][] State0_N = new String[4][4];//array after initial round: AddRoundKey
    ArrayList<String2DArrays> StateList = new ArrayList<String2DArrays>();//one for each round
    private JTextField PlainTexttxtField;
    private JTextField KeytxtField;
    private JTextArea ResulttxtField;
    private JLabel ResultInfolbl;
    private JLabel UpperMessagelbl;
    private JButton EncryptButton;
    JLabel RunTimeslbl;
    
    public AES_Frame()
    {
    	//window stuff
    	//
    	this.setTitle("Rijndael AES");  //we need float for correct division
    	this.setSize(500, 400);
    	
    	
    	
    	this.setResizable(false);
    	getContentPane().setLayout(null);
    	
    	PlainTexttxtField = new JTextField();
    	PlainTexttxtField.setBounds(0, 64, 494, 51);
    	getContentPane().add(PlainTexttxtField);
    	
    	
    	KeytxtField = new JTextField();
    	KeytxtField.setBounds(0, 151, 494, 51);
    	getContentPane().add(KeytxtField);
    	
    	ResulttxtField = new JTextArea();
    	ResulttxtField.setBounds(0, 261, 494, 111);
    	getContentPane().add(ResulttxtField);
    	
    	JLabel PlainTextInfolbl = new JLabel("Insert Text to Encrypt");
    	PlainTextInfolbl.setBounds(0, 42, 157, 23);
    	getContentPane().add(PlainTextInfolbl);
    	
    	JLabel KeyInfolbl = new JLabel("Insert Key");
    	KeyInfolbl.setBounds(0, 126, 157, 23);
    	getContentPane().add(KeyInfolbl);
    	
    	ResultInfolbl = new JLabel("Encypted Result");
    	ResultInfolbl.setBounds(0, 238, 157, 23);
    	getContentPane().add(ResultInfolbl);
    	
    	UpperMessagelbl = new JLabel("Rijndael AES 128-bit");
    	UpperMessagelbl.setHorizontalAlignment(SwingConstants.CENTER);
    	UpperMessagelbl.setBounds(180, 0, 148, 23);
    	getContentPane().add(UpperMessagelbl);
    	
    	
    	EncryptButton = new JButton("Encrypt");
    	EncryptButton.setBounds(204, 213, 89, 23);
    	getContentPane().add(EncryptButton);
    	
    	RunTimeslbl = new JLabel("");
    	RunTimeslbl.setBounds(293, 213, 128, 23);
    	getContentPane().add(RunTimeslbl);
    	
    	PlainTexttxtField.setText("21àCZ17ö0¨¢4");//for testing reasons
    	KeytxtField.setText("2b28ab097eaef7cf15d2154f16a6883c");//for testing reasons
    	
        EncryptButton.addActionListener(new ActionListener() {
			
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		ResulttxtField.setText("");
        		
        		//Times its going to run
        		timerun=(int) ((Math.ceil( PlainTexttxtField.getText().length()/BLOCK_SIZE ))); 
        		RunTimeslbl.setText("Runs "+timerun+" time(s)");
        		int start;
        		for(int iter=0;iter<timerun;iter++)
        		{
        			start=iter*(int)(BLOCK_SIZE);
        			
        			int stop;
        			if( PlainTexttxtField.getText().length() > start +(int)(BLOCK_SIZE) )
        				stop = start +(int)(BLOCK_SIZE);
        			else
        				stop = PlainTexttxtField.getText().length();
        			
        			System.out.println(iter+": start:"+start+" stop:"+stop);
        			String currentPlainText=PlainTexttxtField.getText().substring(start, stop); //spaces: [ , )
        			System.out.println("text:"+currentPlainText);
        			State0=Encryption_Process.asciiStringToHexArray(currentPlainText);
        			
        			//GetKey
        			if(KeytxtField.getText().length()!= (int)(2* BLOCK_SIZE))//check length
        			{
        				ResulttxtField.setText("ERROR WORNG KEY LENGTH  ERROR WORNG KEY LENGTH  ERROR WORNG KEY LENGTH  ");
        			}
        			else if(!Encryption_Process.CorrectHexFormat(KeytxtField.getText()))//check format
        			{
        				ResulttxtField.setText("ERROR WORNG KEY FORMAT  ERROR WORNG KEY FORMAT  ERROR WORNG KEY FORMAT  ");
        			}
        			else  //If the key is OK  Decrypt
        			{
        				key0=Encryption_Process.HexToHexArray(KeytxtField.getText());

        				//START ENCRYPTING
        				//Initial Round
        				Cipher_Key cipher_key = new Cipher_Key(key0);
        				State0_N = Encryption_Process.AddRoundKey(State0, cipher_key.key0);
        				//System.out.println("N0");
        				//String2DArrays.Print2dStringArray(State0_N);

        				StateList.add(new String2DArrays(State0_N));

        				//At the beginning of round i: State[i] = State[i-1]
        				for(int i=0;i<9;i++)
        				{
        					if(i!=0)
        						StateList.add(new String2DArrays(StateList.get(StateList.size()-1).array ));

        					StateList.set(i, new String2DArrays(Encryption_Process.SubBytes(StateList.get(i).array)));//Subbyted array
        					//System.out.println("After SUBBYTES");
        					//String2DArrays.Print2dStringArray( StateList.get(i).array  );


        					StateList.set(i, new String2DArrays( Encryption_Process.ShiftRows(StateList.get(i).array)));
        					//System.out.println("ShiftRows");
        					//String2DArrays.Print2dStringArray( StateList.get(i).array  );

        					StateList.set(i, new String2DArrays( Encryption_Process.MixColumns(StateList.get(i).array)));
        					//System.out.println("MIXCOLUMNS");
        					//String2DArrays.Print2dStringArray( StateList.get(i).array  );

        					StateList.set(i, new String2DArrays( Encryption_Process.AddRoundKey((StateList.get(i).array),  cipher_key.KEYS.get(i).array)));
        					//System.out.println("ADDROUNDKEY");
        					//String2DArrays.Print2dStringArray( StateList.get(i).array  );

        				}

        				int finalindex=StateList.size()-1;

        				StateList.set(finalindex, new String2DArrays(Encryption_Process.SubBytes(StateList.get(finalindex).array)));//Subbyted array
        				StateList.set(finalindex, new String2DArrays( Encryption_Process.ShiftRows(StateList.get(finalindex).array)));
        				StateList.set(finalindex, new String2DArrays( Encryption_Process.AddRoundKey((StateList.get(finalindex).array),
        						cipher_key.KEYS.get(cipher_key.KEYS.size()-1).array)));

        				//FINISHED
        				//back to ascii
        				String2DArrays.Print2dStringArray( StateList.get(finalindex).array  );
        				ResulttxtField.setText(ResulttxtField.getText()+ "\n"+
        						Encryption_Process.HexArrayToasciiString( StateList.get(finalindex).array ));
        				
        				
        				StateList.clear();//get ready for next decryption
        			}
        		}
        	}
		});  	
    	
    	
    	this.setVisible(true);
    	this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
    	
    	
    }
}
