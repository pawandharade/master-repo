package src.com.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import org.apache.commons.io.FilenameUtils;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.Color;
import java.awt.SystemColor;

import src.com.structure.csv.builder.CSVBuilder;
import src.com.structure.csv.data.enums.MessageTypes;
import src.com.util.Utilities;

public class MessageBuilder{

	/**
	 * 
	 */
	private JFrame frmMessagebuilder;
	private static Dimension dim;
	private JTextField textFieldInput;
	private JFileChooser fcInput;
	private JLabel lblInput, lblMessageType;
	private JButton btnBrowseInput, btnGenerateMessageStructure;
	private JComboBox comboBoxMessageType;
	private String strInputFilename, strFileExtension;
	public static final String CSV = "CSV";
	public static final String INHOUSE = "INHOUSE";
	public static final String XLSX_FILE_EXTENSION = "xlsx";
	public static final String XLS_FILE_EXTENSION = "xls";
	private JTextField textFieldMessageName;
	private JTextField textMessageDescription;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem exitMenuItem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dim = Toolkit.getDefaultToolkit().getScreenSize();
					MessageBuilder window = new MessageBuilder();
					window.frmMessagebuilder.setLocation(dim.width/2-window.frmMessagebuilder.getSize().width/2, window.frmMessagebuilder.getSize().height/2-window.frmMessagebuilder.getSize().height/2);
					window.frmMessagebuilder.setLocationRelativeTo(null);
					window.frmMessagebuilder.setVisible(true);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public MessageBuilder() throws FileNotFoundException, IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		strInputFilename = "";
		frmMessagebuilder = new JFrame();
		frmMessagebuilder.setBounds(100, 100, 650, 300);
		frmMessagebuilder.setIconImage(Toolkit.getDefaultToolkit().getImage(MessageBuilder.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
		frmMessagebuilder.setResizable(false);
		frmMessagebuilder.setTitle("MessageBuilder");
		
		frmMessagebuilder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		lblInput = new JLabel("Upload Message Specification : ");
		lblInput.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		textFieldInput = new JTextField();
		textFieldInput.setColumns(10);
		textFieldInput.setDropTarget(new DropTarget() {
			private static final long serialVersionUID = 1L;

			public synchronized void drop(DropTargetDropEvent evt) {
	            try {
	                evt.acceptDrop(DnDConstants.ACTION_COPY);
	                @SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt
	                        .getTransferable().getTransferData(
	                                DataFlavor.javaFileListFlavor);
	                for (File file : droppedFiles) {
	                	textFieldInput.setText(file.getAbsolutePath());
	                	strInputFilename = textFieldInput.getText();
	                }
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	    });
		
		btnBrowseInput = new JButton("...");
		btnBrowseInput.setToolTipText("Browse...");
		btnBrowseInput.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	fcInput = new JFileChooser();
            	int returnVal = fcInput.showOpenDialog(MessageBuilder.this.frmMessagebuilder);
    			
    			if (returnVal == JFileChooser.APPROVE_OPTION) {
    		        File file = fcInput.getSelectedFile();
    		        textFieldInput.setText(file.getAbsolutePath());
    		        strInputFilename = textFieldInput.getText();
    		    } 
            }
        });
		
		lblMessageType = new JLabel("Select Message Type : ");
		lblMessageType.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		comboBoxMessageType = new JComboBox(MessageTypes.values());
		
		btnGenerateMessageStructure = new JButton("Generate Message Structure");
		
		JLabel lblMessageStructureName = new JLabel("Message Structure Name : ");
		lblMessageStructureName.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		textFieldMessageName = new JTextField();
		textFieldMessageName.setFont(new Font("Tahoma", Font.BOLD, 11));
		textFieldMessageName.setColumns(10);
		
		JLabel lblDescriptionAboutMessage = new JLabel("Description About Message : ");
		lblDescriptionAboutMessage.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		textMessageDescription = new JTextField();
		textMessageDescription.setColumns(10);
		
		JLabel labelStatusMessage = new JLabel("");
		
		JLabel lblStatusMessage = new JLabel("Status : ");
		lblStatusMessage.setFont(new Font("Tahoma", Font.BOLD, 11));
		
	    final JTextArea textAreaStatusMessage = new JTextArea();
		textAreaStatusMessage.setEditable(false);
		textAreaStatusMessage.setBackground(SystemColor.control);
		GroupLayout groupLayout = new GroupLayout(frmMessagebuilder.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblMessageStructureName)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblInput, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblMessageType)
										.addComponent(lblDescriptionAboutMessage))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(comboBoxMessageType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(textMessageDescription, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
												.addComponent(textFieldMessageName, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
												.addGroup(groupLayout.createSequentialGroup()
													.addComponent(textFieldInput, GroupLayout.PREFERRED_SIZE, 393, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(btnBrowseInput, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
												.addComponent(btnGenerateMessageStructure))
											.addGap(100)))))
							.addGap(22))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(labelStatusMessage)
								.addComponent(lblStatusMessage, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textAreaStatusMessage, GroupLayout.PREFERRED_SIZE, 562, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblInput)
						.addComponent(textFieldInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowseInput, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMessageType)
						.addComponent(comboBoxMessageType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldMessageName, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMessageStructureName))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textMessageDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDescriptionAboutMessage))
					.addGap(18)
					.addComponent(btnGenerateMessageStructure)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(20)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(labelStatusMessage)
								.addComponent(lblStatusMessage)))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textAreaStatusMessage, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)))
					.addContainerGap())
		);
		
		btnGenerateMessageStructure.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e){
            	 strInputFilename = textFieldInput.getText();
            	 strFileExtension = FilenameUtils.getExtension(strInputFilename);
            	 if ((!strInputFilename.equals(null)) && (!textFieldMessageName.getText().trim().equals(null)) && (strFileExtension.equals(XLS_FILE_EXTENSION) || strFileExtension.equals(XLSX_FILE_EXTENSION) )){
            		 if (comboBoxMessageType.getSelectedItem().toString().equals(CSV)){
            			 CSVBuilder builder = new CSVBuilder();
            			 try {
							 String status = builder.constructCSVMessageStructure(strInputFilename,textFieldMessageName.getText(), textMessageDescription.getText());
							 if (status.equals("SUCCESS")){
								 
								 textAreaStatusMessage.setText("CSV message structure has been created successfully. \nFilename : " + Utilities.getFileDirectoryPath(strInputFilename) + "msg_" + textFieldMessageName.getText() + ".xml");
										
										    
							 }else{
								 JOptionPane.showMessageDialog(frmMessagebuilder,
										    "An error occurred during transformation.",
										    "Error Occurred",
										    JOptionPane.ERROR_MESSAGE);
							 }
						} catch (Exception e1) {
							e1.printStackTrace();
						}
            			 
            		 }else if (comboBoxMessageType.getSelectedItem().toString().equals(INHOUSE)){
            		 }
				 }else{
					 
					 if(strInputFilename.trim().equals("")){
						 JOptionPane.showMessageDialog(frmMessagebuilder,
								    "Please provide the message structure specification.",
								    "Specification Missing",
								    JOptionPane.ERROR_MESSAGE);
					 }
					 
					 if(textFieldMessageName.getText().trim().equals("")){
						 JOptionPane.showMessageDialog(frmMessagebuilder,
								    "Please provide the message structure name.",
								    "Message Name Missing",
								    JOptionPane.ERROR_MESSAGE);
					 }
				 }
            }
        });
		frmMessagebuilder.getContentPane().setLayout(groupLayout);
		
		menuBar = new JMenuBar();
	    fileMenu = new JMenu("File");
	    exitMenuItem = new JMenuItem("Exit");
	    exitMenuItem.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		System.exit(0);
	    	}
	    });
	    
	    fileMenu.add(exitMenuItem);
	    menuBar.add(fileMenu);
		frmMessagebuilder.setJMenuBar(menuBar);
	}
}
