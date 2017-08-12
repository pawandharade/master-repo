
package main.java.com.pgp.utils;

import javax.swing.JTabbedPane;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import java.awt.EventQueue;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import javax.swing.JOptionPane;
import java.io.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class PGPUtils extends JFrame {
	private BufferedImage img;
	private static final long serialVersionUID = 1L;
	JMenuBar menuBar1;
	JMenu menu1;
	JTabbedPane tab1;

	JMenuItem exitmenu,helpmenu; 
	String input,output,secretKey,spublicKey,input2,output2;
	char []password;
	JLabel jl,label1, label2,label3,label4,label5,label6,label7,label8,label10,label11,label12,label13,label14;
	JTextArea logtxt,logtxt2;
	JScrollPane scrolltxt,scrolltxt2;
	JPasswordField jpswd;
	JPanel panel1,panel2;
	JButton browseButton,destPathButton,decryptButton,pgpsecretKeyringButton,browseButton2,destPathButton2,encryptButton,encryptBrowseButton;
	JTextField text1,text2,text3,text4,text5,text6;
	JFileChooser fileChooser,destPathChooser,secretKeyChooser,fileChooser2,destPathChooser2,publicKeyChooser;
	private BouncyCastleProvider bcp=null;
	private static final String Provider1="BC";;
	static InputStream in,keyIn;
	static OutputStream out;
	public PGPUtils() {
		initUI();
	}
	
	public void encrUI(){
		panel2= new JPanel();       
		panel2.setLayout(null); 
		panel2.setBackground(Color.gray);
		panel2.setBorder(BorderFactory.createLineBorder(Color.black));
		getContentPane().add(panel2);
		panel2.setVisible(true);

		label8=new JLabel("Select input file to be encrypted :");
		label8.setFont(new Font("Tahoma", 1, 12));
		label8.setLocation(10, 10);       
		label8.setSize(300, 40); 

		text4=new JTextField();			 
		text4.setSize(370,25);
		text4.setLocation(270,20);
		text4.setEditable(false);

		browseButton2=new JButton("Browse..");			 
		browseButton2.setToolTipText("Browse for input file");			 
		browseButton2.setBounds(50, 60, 90, 30);   			 
		browseButton2.setLocation(650,16);		

		label10=new JLabel("Dest'n path to save encrypted message :");
		label10.setFont(new Font("Tahoma", 1, 12));
		label10.setLocation(10, 40);       
		label10.setSize(310, 40); 

		text5=new JTextField();       
		text5.setSize(370,25);
		text5.setLocation(270,50);
		text5.setEditable(false); 

		destPathButton2= new JButton("Select..");       
		destPathButton2.setToolTipText("Click to set the Destination folder to save the encrypted message.");       
		destPathButton2.setBounds(50, 60, 90, 30);   			 
		destPathButton2.setLocation(650,50);

		encryptButton= new JButton("Encrypt");			 
		encryptButton.setToolTipText("Click to Encrypt the message");			 
		encryptButton.setBounds(50, 60, 90, 30);   			 
		encryptButton.setLocation(270,110);

		label11=new JLabel("Select PGP Public key:");
		label11.setFont(new Font("Tahoma", 1, 12));
		label11.setLocation(10, 70);       
		label11.setSize(310, 40);       

		text6=new JTextField();       
		text6.setSize(370,25);
		text6.setLocation(270,80);
		text6.setEditable(false);           

		encryptBrowseButton= new JButton("Select..");       
		encryptBrowseButton.setToolTipText("Click to select the public key to encrypt the message.");       
		encryptBrowseButton.setBounds(50, 60, 90, 30);   			 
		encryptBrowseButton.setLocation(650,84);

		label12=new JLabel("Status :");
		label12.setFont(new Font("Tahoma", 1, 12));
		label12.setSize(60, 40);
		label12.setLocation(10,140);

		label13=new JLabel("");
		label13.setFont(new Font("Tahoma", 1, 12));
		label13.setSize(550, 40);
		label13.setLocation(80,140);


		label14=new JLabel("Log : ");
		label14.setFont(new Font("Tahoma", 1, 12));
		label14.setSize(550, 40);
		label14.setLocation(10,170);
		logtxt2 = new JTextArea();
		logtxt2.setEditable(false);
		scrolltxt2 = new JScrollPane(logtxt2);
		scrolltxt2.setLocation(60,190);
		scrolltxt2.setSize(680,220);

		panel2.add(label8);
		panel2.add(text4);
		panel2.add(browseButton2);
		panel2.add(label10);
		panel2.add(text5);
		panel2.add(destPathButton2);
		panel2.add(label11);
		panel2.add(encryptButton);
		panel2.add(label11);
		panel2.add(text6);
		panel2.add(encryptBrowseButton);
		panel2.add(label12);
		panel2.add(label13);
		panel2.add(label14);
		panel2.add(scrolltxt2);


		browseButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label13.setText("");
				logtxt2.append("Browsing for the input file....\n");
				fileChooser2=new JFileChooser();
				fileChooser2.setDialogTitle("Select input message");
				fileChooser2.setFileSelectionMode(JFileChooser.FILES_ONLY);			 
				fileChooser2.setAcceptAllFileFilterUsed(false); 			 
				int rVal1 = fileChooser2.showOpenDialog(PGPUtils.this);
				if (rVal1 == JFileChooser.APPROVE_OPTION)			 {
					text4.setText(fileChooser2.getSelectedFile().toString());
					logtxt2.append("Input File selected. : "+fileChooser2.getSelectedFile().toString()+"\n");
					text4.setBackground(Color.white);			   
				}			  
			}			 
		});	

		destPathButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label13.setText("");
				logtxt2.append("Selecting the destination folder to save the encrypted message....\n");
				destPathChooser2=new JFileChooser();
				destPathChooser2.setDialogTitle("Select destination folder");
				destPathChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);			 
				destPathChooser2.setAcceptAllFileFilterUsed(false);
				destPathChooser2.setDialogTitle("Select Destination folder");
				int rVal2 = destPathChooser2.showOpenDialog(PGPUtils.this);
				if (rVal2 == JFileChooser.APPROVE_OPTION)			 {
					GregorianCalendar gcal = new GregorianCalendar();
					String datetime=""+gcal.get(Calendar.MONTH)+gcal.get(Calendar.DATE)+gcal.get(Calendar.YEAR)+gcal.get(Calendar.HOUR)+gcal.get(Calendar.MINUTE)+gcal.get(Calendar.SECOND)+"";
					text5.setText(destPathChooser2.getSelectedFile().toString()+ "\\Encr_"+datetime+".txt");
					logtxt2.append("Destination folder path selected.\n"+destPathChooser2.getSelectedFile().toString()+ "\\Encr_"+datetime+".txt\n");
					text5.setBackground(Color.white);
				}
			}
		});

		encryptBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label13.setText("");
				logtxt2.append("Searching for public key....\n");
				publicKeyChooser=new JFileChooser();
				publicKeyChooser.setDialogTitle("Select public key");
				publicKeyChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);			 
				publicKeyChooser.setAcceptAllFileFilterUsed(false); 			 
				int rVal1 = publicKeyChooser.showOpenDialog(PGPUtils.this);
				if (rVal1 == JFileChooser.APPROVE_OPTION)			 {
					text6.setText(publicKeyChooser.getSelectedFile().toString());
					text6.setBackground(Color.white);
					logtxt2.append("Public key selected.:"+publicKeyChooser.getSelectedFile().toString()+"\n");			   
				}			  
			}			 
		});

		encryptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try { 
					input2=text4.getText().toString();
					output2=text5.getText().toString();
					spublicKey=text6.getText().toString();

					if (!(input2.equals("")) && !(output2.equals("")) && !(spublicKey.equals(""))){
						encryptFile(new File(input2),new File(spublicKey),new File(output2),true);
					}
					else{
						String tmpSts="";

						if (input2.equals("")){
							text4.setBackground(Color.pink);
							tmpSts=tmpSts+" Input_File";
						}
						if (output2.equals("")){
							text5.setBackground(Color.pink);
							tmpSts=tmpSts+" Destination_Folder";
						}
						if (spublicKey.equals("")){
							text6.setBackground(Color.pink);
							tmpSts=tmpSts+" Public_Key";
						}
						label13.setForeground(Color.yellow);
						label13.setText("Missing :" + tmpSts + ".");
						JOptionPane.showMessageDialog(null,"Please provide the valid input for highlighted fields.","Input missing",JOptionPane.WARNING_MESSAGE);
					}
				}
				catch(Exception e1){
					logtxt2.append("Error occured:"+e1.getStackTrace().toString()+"\n");
					e1.printStackTrace();
				}
			}
		});
	}

	public void decrUI(){
		panel1= new JPanel();       
		panel1.setLayout(null); 
		panel1.setBackground(Color.gray);
		panel1.setBorder(BorderFactory.createLineBorder(Color.black));
		getContentPane().add(panel1); 
		panel1.setVisible(true);

		label1= new JLabel("Select PGP encrypted file to decrypt :");
		label1.setFont(new Font("Tahoma", 1, 12));
		label1.setLocation(10, 10);       
		label1.setSize(300, 40);       
		text1=new JTextField();			 
		text1.setSize(370,25);
		text1.setLocation(270,20);
		text1.setEditable(false);			 
		browseButton=new JButton("Browse..");			 
		browseButton.setToolTipText("Browse for pgp encrypted file");			 
		browseButton.setBounds(50, 60, 90, 30);   			 
		browseButton.setLocation(650,16);			 
		label2=new JLabel("Dest'n path to save decrypted message :");
		label2.setFont(new Font("Tahoma", 1, 12));
		label2.setLocation(10, 40);       
		label2.setSize(310, 40);       
		text2=new JTextField();       
		text2.setSize(370,25);
		text2.setLocation(270,50);
		text2.setEditable(false);           
		destPathButton= new JButton("Select..");       
		destPathButton.setToolTipText("Click to set the Destination folder to save the decrypted message.");       
		destPathButton.setBounds(50, 60, 90, 30);   			 
		destPathButton.setLocation(650,50);

		label3=new JLabel("Select PGP Secret Keyring:");
		label3.setFont(new Font("Tahoma", 1, 12));
		label3.setLocation(10, 70);       
		label3.setSize(310, 40);       
		text3=new JTextField();       
		text3.setSize(370,25);
		text3.setLocation(270,80);
		text3.setEditable(false);           
		pgpsecretKeyringButton= new JButton("Select..");       
		pgpsecretKeyringButton.setToolTipText("Click to select the private key to decrypt the message.");       
		pgpsecretKeyringButton.setBounds(50, 60, 90, 30);   			 
		pgpsecretKeyringButton.setLocation(650,84);

		label4=new JLabel("Enter the Passphrase/Password :");
		label4.setFont(new Font("Tahoma", 1, 12));
		label4.setLocation(10, 100); 
		label4.setSize(310, 40);

		jpswd = new JPasswordField(10);	
		jpswd.setEchoChar('*');
		jpswd.setLocation(270,111);
		jpswd.setSize(210,25);

		label5=new JLabel("Status :");
		label5.setFont(new Font("Tahoma", 1, 12));
		label5.setSize(60, 40);
		label5.setLocation(10,170);

		label6=new JLabel("");
		label6.setFont(new Font("Tahoma", 1, 12));
		label6.setSize(550, 40);
		label6.setLocation(80,170);

		decryptButton= new JButton("Decrypt");			 
		decryptButton.setToolTipText("Click to Decrypt the message");			 
		decryptButton.setBounds(50, 60, 90, 30);   			 
		decryptButton.setLocation(270,140);

		label7=new JLabel("Log : ");
		label7.setFont(new Font("Tahoma", 1, 12));
		label7.setSize(550, 40);
		label7.setLocation(10,190);
		logtxt = new JTextArea();
		logtxt.setEditable(false);
		scrolltxt = new JScrollPane(logtxt);
		scrolltxt.setLocation(60,210);
		scrolltxt.setSize(680,200);


		panel1.add(label1);			 
		panel1.add(text1);			 
		panel1.add(browseButton);			 
		panel1.add(label2);			 
		panel1.add(text2);			 
		panel1.add(destPathButton);

		panel1.add(label3);			 
		panel1.add(text3);			 
		panel1.add(pgpsecretKeyringButton);

		panel1.add(label4);
		panel1.add(jpswd);

		panel1.add(decryptButton);
		panel1.add(label5);
		panel1.add(label6);
		panel1.add(label7);
		panel1.add(scrolltxt);



		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label6.setText("");
				logtxt.append("Browsing for encrypted file....\n");
				fileChooser=new JFileChooser();
				fileChooser.setDialogTitle("Select encrypted message");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);			 
				fileChooser.setAcceptAllFileFilterUsed(false); 			 
				int rVal1 = fileChooser.showOpenDialog(PGPUtils.this);
				if (rVal1 == JFileChooser.APPROVE_OPTION)			 {
					text1.setText(fileChooser.getSelectedFile().toString());
					logtxt.append("Input File selected. : "+fileChooser.getSelectedFile().toString()+"\n");
					text1.setBackground(Color.white);			   
				}			  
			}			 
		});			 

		destPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label6.setText("");
				logtxt.append("Searching for destination folder path to save the decrypted message....\n");
				destPathChooser=new JFileChooser();
				destPathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);			 
				destPathChooser.setAcceptAllFileFilterUsed(false);
				destPathChooser.setDialogTitle("Select Destination folder");
				int rVal2 = destPathChooser.showOpenDialog(PGPUtils.this);
				if (rVal2 == JFileChooser.APPROVE_OPTION)			 {
					GregorianCalendar gcal = new GregorianCalendar();
					String datetime=""+gcal.get(Calendar.MONTH)+gcal.get(Calendar.DATE)+gcal.get(Calendar.YEAR)+gcal.get(Calendar.HOUR)+gcal.get(Calendar.MINUTE)+gcal.get(Calendar.SECOND)+"";
					text2.setText(destPathChooser.getSelectedFile().toString()+ "\\Decr_"+datetime+".txt");
					logtxt.append("Destination folder path selected.\n"+destPathChooser.getSelectedFile().toString()+ "\\Decr_"+datetime+".txt\n");
					text2.setBackground(Color.white);
				}
			}
		});						 

		pgpsecretKeyringButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label6.setText("");
				logtxt.append("Searching for private key....\n");
				secretKeyChooser=new JFileChooser();
				secretKeyChooser.setDialogTitle("Select secret key");
				secretKeyChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);			 
				secretKeyChooser.setAcceptAllFileFilterUsed(false); 			 
				int rVal1 = secretKeyChooser.showOpenDialog(PGPUtils.this);
				if (rVal1 == JFileChooser.APPROVE_OPTION)			 {
					text3.setText(secretKeyChooser.getSelectedFile().toString());
					text3.setBackground(Color.white);
					logtxt.append("Private key selected.:"+secretKeyChooser.getSelectedFile().toString()+"\n");			   
				}			  
			}			 
		});
		decryptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try { 
					input=text1.getText().toString();
					output=text2.getText().toString();
					secretKey=text3.getText().toString();
					password=jpswd.getPassword();

					if (!String.valueOf(password).equals("")){
						jpswd.setBackground(Color.white);
					}
					logtxt.append("Validating input fileds......\n");
					if ((!input.equals(""))&&(!output.equals(""))&&(!secretKey.equals(""))&&(!String.valueOf(password).equals(""))){
						logtxt.append("Input fields validated.\n");
						decryptFile(new File(input),new File(output), new File(secretKey),password);
					}
					else if ((input.equals(""))&&(output.equals(""))&&(secretKey.equals(""))&&(String.valueOf(password).equals(""))){
						text1.setBackground(Color.pink);
						text2.setBackground(Color.pink);
						text3.setBackground(Color.pink);
						jpswd.setBackground(Color.pink);
						logtxt.append("All the input field values are invalid or null.\n");
						label6.setText("Please provide the valid input for all the mentioned fields.");
						label6.setForeground(Color.yellow);
						JOptionPane.showMessageDialog(null,"Please provide the valid input for all the highlighted fields.","Input missing",JOptionPane.WARNING_MESSAGE);
					}
					else {
						String tmpstat="Missing :-";
						if (input.equals("")){
							text1.setBackground(Color.pink);
							tmpstat=tmpstat+" Input_file";
							label6.setForeground(Color.yellow);
							logtxt.append("Invalid input file or null.\n");
						}

						if (output.equals("")){
							text2.setBackground(Color.pink);
							tmpstat=tmpstat+" Destination_folder";
							logtxt.append("Invalid Destination folder path or null.\n");
							label6.setForeground(Color.yellow);
						}

						if (secretKey.equals("")){
							text3.setBackground(Color.pink);
							tmpstat=tmpstat+" Private_Key";
							logtxt.append("Invalid secret key or null.\n");
							label6.setForeground(Color.yellow);
						}

						if (String.valueOf(password).equals("")){
							jpswd.setBackground(Color.pink);
							tmpstat=tmpstat+" Passphrase";
							logtxt.append("Invalid passphrase or null.\n");
							label6.setForeground(Color.yellow);
						}
						label6.setText(tmpstat+".");
						JOptionPane.showMessageDialog(null,"Please provide the valid input for highlighted fields.","Input missing",JOptionPane.WARNING_MESSAGE);
					}  
				}
				catch(Exception e1){
					logtxt.append("Error occured:"+e1.getStackTrace().toString()+"\n");
					e1.printStackTrace();}
			}
		});
	}
	
	public final void initUI()  {

		setTitle("PGP Encryption/Decrypion Tool");  

		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("Logo.png")).getImage());    
		setSize(760, 500);       
		setResizable(false);       
		setLocationRelativeTo(null); 

		menuBar1= new JMenuBar();
		menu1= new JMenu("Options");

		decrUI();
		encrUI();

		helpmenu= new JMenuItem("Help");
		exitmenu= new JMenuItem("Exit");
		menuBar1.add(menu1);
		setJMenuBar(menuBar1);

		menu1.add(helpmenu);
		menu1.add(exitmenu);

		tab1=new JTabbedPane();

		tab1.addTab("Encryption",null,panel2,"Encryption");
		tab1.addTab("Decryption",null,panel1,"Decryption");

		add(tab1);


		exitmenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.exit(0);
			}
		});

		helpmenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				try{
					Runtime runtime = Runtime.getRuntime();
					Process process = runtime.exec("rundll32 url.dll, FileProtocolHandler Help.pdf");
				}
				catch(Exception e){
					e.printStackTrace();}
			}
		});

		setDefaultCloseOperation(EXIT_ON_CLOSE);      
	}

	public void encryptFile(File inFile, File keyFile, File outFile, boolean isArmoredOutput) throws IOException,
	NoSuchProviderException, NoSuchAlgorithmException, PGPException {

		Security.addProvider(new BouncyCastleProvider());
		logtxt2.append("added BouncyCastleProvider.\n");
		OutputStream out = null;
		OutputStream cOut = null;
		logtxt2.append("Initialized outputstream.\n");
		PGPCompressedDataGenerator comData = null;

		logtxt2.append("PGP Compressed Data Generator has been initialized\n");
		try {
			// get public key
			logtxt2.append("Reading public key\n");
			PGPPublicKey encKey = readPublicKey(keyFile);

			int count = 0;
			/*		 for (java.util.Iterator iterator = encKey.getUserIDs(); iterator.hasNext();) {
					 count++;

					 } */

			out = new FileOutputStream(outFile);
			if (isArmoredOutput) {

				logtxt2.append("Armored Output=true\n");
				out = new ArmoredOutputStream(out);
			}

			// encryptFile and compress input file content
			logtxt2.append("encrypting the content......\n");
			PGPEncryptedDataGenerator cPk = new PGPEncryptedDataGenerator(PGPEncryptedData.CAST5, new SecureRandom(), Provider1);

			cPk.addMethod(encKey);

			logtxt2.append("encryptionconpleted.\n");
			logtxt2.append("Compressing input file content...\n"); 
			cOut = cPk.open(out, new byte[1 << 16]);

			comData = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);

			// write encrypted content to a file
			logtxt2.append("Compression completed.\n writing encrypted content to the output file.\n");
			PGPUtil.writeFileToLiteralData(comData.open(cOut), PGPLiteralData.BINARY, inFile, new byte[1 << 16]);

			logtxt2.append("File encrypted successfully. Filepath: "+outFile.getAbsolutePath()+"\n");
			label13.setForeground(Color.green);
			label13.setText("Encryption successfull.");
			JOptionPane.showMessageDialog(null,"Message content has been encrypted successfully. The output has been stored to "+outFile.getAbsolutePath(),"encryption successfull",JOptionPane.INFORMATION_MESSAGE);
		} 
		catch(Exception e4)
		{
			label13.setText("Encryption failed.");
			label13.setForeground(Color.red);
			JOptionPane.showMessageDialog(null,"Encryption failed. Error : "+ e4.getMessage(),"Encryption failed",JOptionPane.ERROR_MESSAGE);
			if (outFile.exists()){
				outFile.delete();
			}
			logtxt2.append(e4.toString()+"\n");


		}
		finally {
			if (comData != null) {
				comData.close();
			}
			if (cOut != null) {
				cOut.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	public boolean validateEncryptionKey(File keyFile) throws IOException, PGPException {

		PGPPublicKey encKey = readPublicKey(keyFile);
		return encKey.isEncryptionKey();
	}	

	private static PGPPublicKey readPublicKey(File keyFile) throws IOException, PGPException {

		PGPPublicKey key = null;
		InputStream in = null;
		try {
			in = PGPUtil.getDecoderStream(new FileInputStream(keyFile));
			PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(in);

			// iterate through the key rings.
			Iterator rIt = pgpPub.getKeyRings();
			while (key == null && rIt.hasNext()) {
				PGPPublicKeyRing kRing = (PGPPublicKeyRing) rIt.next();
				Iterator kIt = kRing.getPublicKeys();
				while (key == null && kIt.hasNext()) {
					PGPPublicKey k = (PGPPublicKey) kIt.next();
					if (k.isEncryptionKey()) {
						key = k;
					}
				}
			}
			if (key == null) {
				throw new IllegalArgumentException("Can't find encryption key in key ring: " + keyFile.getCanonicalPath());
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return key;
	}	

	private PGPPrivateKey findSecretKey(InputStream keyIn, long keyID, char[] pass)	throws IOException, PGPException, NoSuchProviderException    {
		logtxt.append("Finding secret keyring.......\n"); 
		PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(org.bouncycastle.openpgp.PGPUtil.getDecoderStream(keyIn)); 
		PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);
		logtxt.append("Found secret keyring.\n"); 
		if (pgpSecKey == null) {
			return null;
		}
		return pgpSecKey.extractPrivateKey(pass, "BC");

	}

	@SuppressWarnings("unchecked")
	public void decryptFile(File inFile, File outFile, File inKeyFile, char[] passwd) throws Exception {
		try {

			logtxt.append("Adding BouncyCastleProvider.......\n");
			Security.addProvider(new BouncyCastleProvider());
			logtxt.append("Added BouncyCastleProvider successfully.\n Initializing Input Stream/ Output streams.\n");

			in=new FileInputStream(inFile);
			keyIn=new FileInputStream(inKeyFile);
			out=new FileOutputStream(outFile);

			logtxt.append("Input/output stream have been initialized.\n");
			in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(in);
			PGPObjectFactory pgpF = new PGPObjectFactory(in);
			PGPEncryptedDataList enc;

			Object o = pgpF.nextObject();

			if (o instanceof  PGPEncryptedDataList) {
				enc = (PGPEncryptedDataList) o;
			} 
			else {
				enc = (PGPEncryptedDataList) pgpF.nextObject();
			}

			Iterator<PGPPublicKeyEncryptedData> it = enc.getEncryptedDataObjects(); 
			PGPPrivateKey sKey = null;
			PGPPublicKeyEncryptedData pbe = null;
			logtxt.append("Secret key extraction started.\n");
			int i=40;
			while (sKey == null && it.hasNext()) {
				pbe = it.next();

				sKey = findSecretKey(keyIn, pbe.getKeyID(), passwd);

			}

			if (sKey == null) {

				throw new IllegalArgumentException("Secret key for message not found.");    
			}

			InputStream clear = pbe.getDataStream(sKey, "BC");

			PGPOnePassSignatureList onePassSignatureList = null; ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
			PGPObjectFactory plainFact = new PGPObjectFactory(clear);
			logtxt.append("Reading object...\n");
			Object message = plainFact.nextObject();

			int decryptionFlag=0;
			while (message != null) {

				if (message instanceof  PGPCompressedData) {
					PGPCompressedData cData = (PGPCompressedData) message;
					PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream());
					logtxt.append("Reading next object...\n");
					message = pgpFact.nextObject();
				}


				if (message instanceof  PGPLiteralData) {
					PGPLiteralData ld = (PGPLiteralData) message;

					InputStream unc = ld.getInputStream();
					int ch;
					logtxt.append("Reading content to decrypt the message....\n");
					while ((ch = unc.read()) >= 0) {
						out.write(ch);
					}
					decryptionFlag=1;
					logtxt.append("Message decryption process finished..\n");
					message = null;
				} else if (message instanceof  PGPOnePassSignatureList) {
					logtxt.append("Reading next object...\n");
					message = plainFact.nextObject();

				} else {
					logtxt.append("Message is not a simple encrypted file - type unknown..\n");
					throw new PGPException("Message is not a simple encrypted file - type unknown.");
				}

			}



			if (pbe.isIntegrityProtected()) {
				if (!pbe.verify()) {
					logtxt.append("Error occurred:Message failed integrity check\n");
					throw new PGPException("Message failed integrity check");
				}
				else{
					if (decryptionFlag==1)	{
						logtxt.append("Decryption successfull.\n Output file stored to : "+text2.getText().toString()+".\n");
						jpswd.setText("");
						label6.setText("Decryption successfull.");
						label6.setForeground(Color.GREEN);
						JOptionPane.showMessageDialog(null,"Message content has been decrypted successfully. The output has been stored to "+outFile.getAbsolutePath(),"Decryption successfull",JOptionPane.INFORMATION_MESSAGE);
					}

				}
			}


		}
		catch(Exception e2)
		{

			jpswd.setText("");
			label6.setText("Decryption failed.");
			label6.setForeground(Color.red);


			if (outFile.exists()){
				outFile.delete();
			}
			logtxt.append(e2.toString()+"\n");
			JOptionPane.showMessageDialog(null,"Decryption failed. Error : "+ e2.getMessage(),"Decryption failed",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			in.close();
			keyIn.close();
			out.close();

		}
	}
	
	public static void main(String[] args) throws Exception {
		PGPUtils ex = new PGPUtils();
		ex.setVisible(true);
	}
}