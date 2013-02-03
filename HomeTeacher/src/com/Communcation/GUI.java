package com.Communcation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GUI implements ActionListener, MouseListener, ListSelectionListener
{
	
	JFrame mainFrame;
	JPanel mainPanel;
	JPanel mainIconPanel;
	Ward ward;
	
	JLabel htsrchLbl;
	JLabel changesLbl;
	JLabel homeTeachersLbl;
	JLabel reportedLbl;
	JLabel sendBoth;
	JLabel sendEmail;
	JLabel sendText;
	JLabel rprtSrchLbl;
	JLabel settingsLbl;
	
	JTextField htSrchField;
	JTextField rprtSrchField;
	JList htList;
	DefaultListModel htModel;
	JList rprtList;
	DefaultListModel rprtModel;
	JList chngList;
	DefaultListModel chngModel;
	JTextArea infoDisplay;
	JTextArea emailText;
	JTextArea mainTxtText;
	JTextArea familyTxtText;
	
	JPanel messagePanel;
	
	Person[] allHTers;
	Person[] nonReportees;
	
	ImageIcon srchIcon;
	ImageIcon srchIconDwn;
	ImageIcon chngIcon;
	ImageIcon chngDwnIcon;
	ImageIcon rprtIcon;
	ImageIcon rprtDwnIcon;
	ImageIcon hmTchIcon;
	ImageIcon hmTchDwnIcon;
	ImageIcon bothIcon;
	ImageIcon bothDwnIcon;
	ImageIcon emailIcon;
	ImageIcon emailDwnIcon;
	ImageIcon textIcon;
	ImageIcon textDwnIcon;
	ImageIcon settingsIcon;
	ImageIcon settingsDwnIcon;
	
	ImageIcon loading;
	
	JComboBox months;
	
	SendMessage msg;
	
	MySpreadSheetListIntegration sprdSht = null;
	
	Color lightBlue = new Color(102, 166, 250);
	
	public GUI(Ward ward)
	{
		this.ward = ward;
		init();
	}
	
	public void init()
	{
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		mainPanel = new JPanel(new BorderLayout());
		
		initImageIcons();
		
		createMainIconPanel();
		
		JPanel panel = ward == null ? initSettingsView() : initHomeTeachersView();
		
		mainPanel.add(panel, BorderLayout.CENTER);
		mainPanel.add(mainIconPanel, BorderLayout.NORTH);
		
		mainFrame.add(mainPanel, BorderLayout.CENTER);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	void addHTersToList(DefaultListModel list, Person[] homeTeachers)
	{
		for(Person p : homeTeachers)
		{
			list.addElement(p);
		}
	}
	
	private void clearMainPanel()
	{
		mainPanel.removeAll();
		mainPanel.revalidate();
		
		mainPanel.add(mainIconPanel, BorderLayout.NORTH);
	}
	
	private void createMainIconPanel()
	{
		mainIconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,35,15));
		
		settingsLbl = new JLabel("Settings", settingsIcon,JLabel.CENTER);
		settingsLbl.setHorizontalTextPosition(JLabel.CENTER);
		settingsLbl.setVerticalTextPosition(JLabel.BOTTOM);
		settingsLbl.addMouseListener(this);
		
		homeTeachersLbl = new JLabel("Home Teachers Info",hmTchIcon,JLabel.CENTER);
		homeTeachersLbl.setHorizontalTextPosition(JLabel.CENTER);
		homeTeachersLbl.setVerticalTextPosition(JLabel.BOTTOM);
		homeTeachersLbl.addMouseListener(this);
		
		reportedLbl = new JLabel("Check Who Reported",rprtIcon,JLabel.CENTER);
		reportedLbl.setHorizontalTextPosition(JLabel.CENTER);
		reportedLbl.setVerticalTextPosition(JLabel.BOTTOM);
		reportedLbl.addMouseListener(this);
		
		changesLbl = new JLabel("Check for Changes",chngIcon,JLabel.CENTER);
		changesLbl.setHorizontalTextPosition(JLabel.CENTER);
		changesLbl.setVerticalTextPosition(JLabel.BOTTOM);
		changesLbl.addMouseListener(this);
		
		mainIconPanel.add(settingsLbl);
		mainIconPanel.add(homeTeachersLbl);
		mainIconPanel.add(reportedLbl);
		mainIconPanel.add(changesLbl);
	}
	
	private JPanel initHomeTeachersView()
	{
		Border border = BorderFactory.createLineBorder(Color.darkGray);
		TitledBorder title = BorderFactory.createTitledBorder("Home Teachers Info");
		title.setTitleJustification(TitledBorder.CENTER);
		title.setBorder(border);
		
		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compound = BorderFactory.createCompoundBorder(title, emptyBorder );
		compound = BorderFactory.createCompoundBorder(emptyBorder, compound);
		
		JPanel mainHTPanel = new JPanel(new BorderLayout());
		
		mainHTPanel.setBorder(compound);
		
		
		JPanel homeTeachersPanel = new JPanel(new BorderLayout());
		
		Border border2 = BorderFactory.createLineBorder(lightBlue);
		TitledBorder title2 = BorderFactory.createTitledBorder("Home Teachers");
		title2.setTitleJustification(TitledBorder.CENTER);
		title2.setBorder(border2);
		
		Border emptyBorder2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compound2 = BorderFactory.createCompoundBorder(emptyBorder2, title2);
		
		homeTeachersPanel.setBorder(compound2);
		
		allHTers = ward.getAllHomeTeachers();
		
		if(htModel == null)
		{
			htModel = new DefaultListModel();
		}
		
		if(htList == null)
		{
			htList = new JList();
			htList.setModel(htModel);
			htList.setCellRenderer(new PersonCellRenderer());
			htList.addListSelectionListener(this);
		}
		
		JScrollPane sc = new JScrollPane(htList);
		
		htModel.clear();
		addHTersToList(htModel, allHTers);
		
		if(infoDisplay == null)
		{
			infoDisplay = new JTextArea(30,20);
		}
		
		JScrollPane textScroller = new JScrollPane(infoDisplay);
		
		Border border3 = BorderFactory.createLineBorder(lightBlue);
		TitledBorder title3 = BorderFactory.createTitledBorder("Companionship Info");
		title3.setTitleJustification(TitledBorder.CENTER);
		title3.setBorder(border3);
		
		Border emptyBorder3 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compound3 = BorderFactory.createCompoundBorder(emptyBorder3, title3);
		
		textScroller.setBorder(compound3);
		
		mainHTPanel.add(textScroller, BorderLayout.CENTER);
		
		if(htSrchField == null)
		{
			htSrchField = new JTextField(12);
		}
		
		if(htsrchLbl == null)
		{
			htsrchLbl = new JLabel(srchIcon);
			htsrchLbl.addMouseListener(this);
		}
		
		JPanel searchPanel = new JPanel(new FlowLayout());
		searchPanel.add(htSrchField);
		searchPanel.add(htsrchLbl);
		
		homeTeachersPanel.add(searchPanel, BorderLayout.NORTH);
		homeTeachersPanel.add(sc, BorderLayout.CENTER);
		
		mainHTPanel.add(homeTeachersPanel, BorderLayout.WEST);
		
		return mainHTPanel;
	}
	
	private JPanel initSettingsView()
	{
		SettingsView settings = new SettingsView(this);		
		return settings.getView();
	}
	
	private JPanel initReportedView()
	{
		JPanel rprtMainPanel = new JPanel(new BorderLayout());
		
		Border border = BorderFactory.createLineBorder(Color.darkGray);
		TitledBorder title = BorderFactory.createTitledBorder("Check Who Reported");
		title.setTitleJustification(TitledBorder.CENTER);
		title.setBorder(border);
		
		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compound = BorderFactory.createCompoundBorder(title, emptyBorder );
		compound = BorderFactory.createCompoundBorder(emptyBorder, compound);
		
		rprtMainPanel.setBorder(compound);
		
		JPanel westPanel = new JPanel(new BorderLayout());
				
		Border rprtMnthBorder = BorderFactory.createLineBorder(lightBlue);
		TitledBorder rprtMnthTitle = BorderFactory.createTitledBorder("Reporting Month");
		rprtMnthTitle.setTitleJustification(TitledBorder.CENTER);
		rprtMnthTitle.setBorder(rprtMnthBorder);
		
		Border rprtMnthEmpty = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		Border rprtCompound = BorderFactory.createCompoundBorder(rprtMnthEmpty, rprtMnthTitle );
		
		if(months == null)
		{
			months = new JComboBox(Months.values());
			months.addActionListener(this);
		}
		
		months.setBorder(rprtCompound);
		
		westPanel.add(months, BorderLayout.NORTH);
		
		JPanel listPanel = new JPanel(new BorderLayout());
		
		Border border2 = BorderFactory.createLineBorder(lightBlue);
		TitledBorder title2 = BorderFactory.createTitledBorder("Haven't Reported");
		title2.setTitleJustification(TitledBorder.CENTER);
		title2.setBorder(border2);
		
		Border emptyBorder2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compound2 = BorderFactory.createCompoundBorder(emptyBorder2, title2);
		
		listPanel.setBorder(compound2);
		
		if(rprtModel == null)
		{
			rprtModel = new DefaultListModel();
		}
		
		if(rprtList == null)
		{
			rprtList = new JList();
			rprtList.setModel(rprtModel);
			rprtList.setCellRenderer(new PersonCellRenderer());
			rprtList.addListSelectionListener(this);
		}
		
		//TODO remove this when done testing messages
//		addHTersToList(rprtModel, allHTers);
		
		JScrollPane sc = new JScrollPane(rprtList);
		
		listPanel.add(sc, BorderLayout.CENTER);
		
		if(rprtSrchField == null)
		{
			rprtSrchField = new JTextField(12);
		}
		
		if(rprtSrchLbl == null)
		{
			rprtSrchLbl = new JLabel(srchIcon);
			rprtSrchLbl.addMouseListener(this);
		}
		
		JPanel searchPanel = new JPanel(new FlowLayout());
		searchPanel.add(rprtSrchField);
		searchPanel.add(rprtSrchLbl);		
		
		listPanel.add(searchPanel, BorderLayout.SOUTH);
		
		westPanel.add(listPanel, BorderLayout.CENTER);
		
		rprtMainPanel.add(westPanel, BorderLayout.WEST);
		
		rprtMainPanel.add(createMessagePanel(), BorderLayout.CENTER);
		
		return rprtMainPanel;
	}
	
	private JPanel createMessagePanel()
	{
		if(messagePanel == null)
		{
			messagePanel = new JPanel();
			messagePanel.setLayout(new BorderLayout());
		}
		else
		{
			messagePanel.removeAll();
			messagePanel.revalidate();
		}
		
		if(sendBoth == null)
		{
			sendBoth = new JLabel("Send Both",bothIcon,JLabel.CENTER);
			sendBoth.setHorizontalTextPosition(JLabel.CENTER);
			sendBoth.setVerticalTextPosition(JLabel.BOTTOM);
			sendBoth.addMouseListener(this);
		}
		
		if(sendEmail == null)
		{
			sendEmail = new JLabel("Send Email",emailIcon,JLabel.CENTER);
			sendEmail.setHorizontalTextPosition(JLabel.CENTER);
			sendEmail.setVerticalTextPosition(JLabel.BOTTOM);
			sendEmail.addMouseListener(this);
		}
		
		if(sendText == null)
		{
			sendText = new JLabel("Send Text",textIcon,JLabel.CENTER);
			sendText.setHorizontalTextPosition(JLabel.CENTER);
			sendText.setVerticalTextPosition(JLabel.BOTTOM);
			sendText.addMouseListener(this);
		}
		
		if(emailText == null)
		{
			emailText = new JTextArea();
			emailText.setLineWrap(true);
			emailText.setWrapStyleWord(true);
		}
		
		JScrollPane emailScroller = new JScrollPane(emailText);
		
		emailText.setText(DefaultMessages.notReportedEmail);
		
		Border emailBorder = BorderFactory.createLineBorder(lightBlue);
		TitledBorder emailTitle = BorderFactory.createTitledBorder("Email Message");
		emailTitle.setTitleJustification(TitledBorder.CENTER);
		emailTitle.setBorder(emailBorder);
		
		Border emailEmpty = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		Border emailCompound = BorderFactory.createCompoundBorder(emailTitle, emailEmpty );
		
		emailScroller.setBorder(emailCompound);
		
		if(mainTxtText == null)
		{
			mainTxtText = new JTextArea();
			mainTxtText.setLineWrap(true);
			mainTxtText.setWrapStyleWord(true);
		}
		
		JScrollPane textScroller = new JScrollPane(mainTxtText);
		
		Border textBorder = BorderFactory.createLineBorder(lightBlue);
		TitledBorder textTitle = BorderFactory.createTitledBorder("Text Message");
		textTitle.setTitleJustification(TitledBorder.CENTER);
		textTitle.setBorder(textBorder);
		
		Border textEmpty = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		Border textCompound = BorderFactory.createCompoundBorder(textTitle, textEmpty );
		
		textScroller.setBorder(textCompound);
		
		mainTxtText.setText(DefaultMessages.notReportedText);
		
		if(familyTxtText == null)
		{
			familyTxtText = new JTextArea();
			familyTxtText.setLineWrap(true);
			familyTxtText.setWrapStyleWord(true);
		}
		
		JScrollPane familyMessageScroller = new JScrollPane(familyTxtText);
		
		Border famBorder = BorderFactory.createLineBorder(lightBlue);
		TitledBorder famTitle = BorderFactory.createTitledBorder("Family Message");
		famTitle.setTitleJustification(TitledBorder.CENTER);
		famTitle.setBorder(famBorder);
		
		Border famEmpty = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		Border famCompound = BorderFactory.createCompoundBorder(famTitle, famEmpty );
		
		familyMessageScroller.setBorder(famCompound);
		
		familyTxtText.setText(DefaultMessages.notReportedFamText);
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new MigLayout("", "[450px,grow]", "[150px,grow][105.00px][81.00]"));
		
		middlePanel.add(emailScroller, "cell 0 0,grow");
		middlePanel.add(textScroller, "cell 0 1,grow");
		
		messagePanel.add(middlePanel, BorderLayout.CENTER);
		
		middlePanel.add(familyMessageScroller, "cell 0 2,grow");
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
		
		buttonsPanel.add(sendBoth);
		buttonsPanel.add(sendEmail);
		buttonsPanel.add(sendText);
		
		messagePanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		return messagePanel;
	}
	
	private String getSelectedMonth()
	{
		String mnth = null;
		
		if(months != null)
		{
			switch((Months)months.getSelectedItem())
			{
				case JANUARY:
					mnth = "January";
					break;
				case FEBRUARY:
					mnth = "February";
					break;
				case MARCH:
					mnth = "March";
					break;
				case APRIL:
					mnth = "April";
					break;
				case MAY:
					mnth = "May";
					break;
				case JUNE:
					mnth = "June";
					break;
				case JULY:
					mnth = "July";
					break;
				case AUGUST:
					mnth = "August";
					break;
				case SEPTEMBER:
					mnth = "September";
					break;
				case OCTOBER:
					mnth = "October";
					break;
				case NOVEMBER:
					mnth = "November";
					break;
				case DECEMBER:
					mnth = "December";
					break;
				default:
					mnth = "unknown";
					break;					
			}
		}
		
		return mnth;
	}
	
	private JPanel initChangesView()
	{
		JPanel p = new JPanel(new FlowLayout());
		p.add(new JLabel("Coming Soon"));
		return p;
	}
	
	private void initImageIcons()
	{
		srchIcon = createImageIcon("images/search.png","search");
		srchIconDwn = createImageIcon("images/searchDown.png","search"); 
		
		rprtIcon = createImageIcon("images/reported.png","report");
		rprtDwnIcon = createImageIcon("images/reportedDown.png","report");
		
		chngIcon = createImageIcon("images/changes.png","changes");
		chngDwnIcon = createImageIcon("images/changesDown.png","changes");
		
		hmTchIcon = createImageIcon("images/homeTeachers.png","homeTeachers");
		hmTchDwnIcon = createImageIcon("images/homeTeachersDown.png","homeTeachers");
		
		bothIcon = createImageIcon("images/send.png","send all");
		bothDwnIcon = createImageIcon("images/sendDown.png","send all");
		
		emailIcon = createImageIcon("images/email.png","send email");
		emailDwnIcon = createImageIcon("images/emailDown.png","send email");
		
		textIcon = createImageIcon("images/phone.png","send text");
		textDwnIcon = createImageIcon("images/phoneDown.png","send text");		
		
		settingsIcon = createImageIcon("images/settings.png","settings");
		settingsDwnIcon = createImageIcon("images/settingsDown.png","settings");
		
		loading = createImageIcon("images/loading.gif","loading");
	}
	
	protected ImageIcon createImageIcon(String path, String description) 
	{
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) 
		{
			return new ImageIcon(imgURL, description);
		} 
		else 
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	private Person[] filterList(String name, Person[] list)
	{		
		List<Person> persons = new ArrayList<Person>();
		
		if(name.equals(""))
		{
			return list;
		}
		
		for(Person p : list)
		{
			String nme = p.getFullName().toLowerCase();
			
			if(nme.startsWith(name.toLowerCase()))
			{
				persons.add(p);
			}
		}
		
		if(persons.size() > 0)
		{
			Person[] peops = new Person[persons.size()];
			
			for(int i=0; i<persons.size(); i++)
			{
				peops[i] = persons.get(i);
			}
			
			return peops;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent a) 
	{
		if(a.getSource() == months)
		{
			if(getSelectedMonth().equals("unknown") == false)
			{
				final JDialog d = new JDialog();  
		        MigLayout mig = new MigLayout("","10[grow][grow]10","10[grow]10");
				
				JPanel p1 = new JPanel(mig);
				p1.setBackground(Color.white);
		       
		        d.getContentPane().add(p1);  
		        d.setLocationRelativeTo(mainFrame);  
		        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);  
		        d.setModal(true);  
				
				p1.add(new JLabel("Checking Who Reported"), "cell 1 0");
				p1.add(new JLabel(loading), "cell 2 0");
				d.pack();  
				d.setResizable(false);
				
				SwingWorker<?,?> worker = new SwingWorker<Void,Void>()
					{  
			          	protected Void doInBackground() throws InterruptedException
			          	{  
			          		if(sprdSht == null)
							{
								try {
									sprdSht = new MySpreadSheetListIntegration(DefaultMessages.userName, DefaultMessages.password);
								} catch (AuthenticationException e) {
									e.printStackTrace();
								} catch (MalformedURLException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								} catch (ServiceException e) {
									e.printStackTrace();
								}
							}
							
							if(sprdSht != null)
							{
								List<String> reporters = sprdSht.reportedThisMonth(getSelectedMonth());
								List<Person> peops = new ArrayList<Person>();
								List<Person> nonReporters = new ArrayList<Person>();
								
								for(String r : reporters)
								{
									if(r != null)
									{
										Person p = null;
										String[] name = r.split(" ");
										if(name.length == 2)
										{
											p = ward.getWardMember(name[0], name[1]);
										}
										else if(name.length >= 3)
										{
											p = ward.getWardMember(name[0], name[name.length-1]);
										}
										else
										{
											p = ward.getWardMember(r);
										}
										
										if(p != null)
										{
											peops.add(p);
										}
									}
								}
								
								for(Person p : allHTers)
								{
									boolean found = false;
									for(Person rprtd : peops)
									{
										if(rprtd.isThisPerson(p))
										{
											found = true;
											break;
										}
									}
									
									if(found == false)
									{
										Companionship companionship = ward.getCompanionShip(p);
										Person comp = companionship.getCompanion(p);
										
										for(Person rprtd : peops)
										{
											if(rprtd.isThisPerson(comp))
											{
												found = true;
												break;
											}
										}
									}
									
									if(found == false)
									{
										nonReporters.add(p);
									}
								}
								
								nonReportees = new Person[nonReporters.size()];
								
								for(int i=0; i<nonReporters.size(); i++)
								{
									nonReportees[i] = nonReporters.get(i);
								}
								
								SwingUtilities.invokeLater(new Runnable(){

									@Override
									public void run() 
									{
										rprtModel.clear();
										addHTersToList(rprtModel, nonReportees);
									}
									
								});
							}
  
			          		return null;  
			          	}  
			  
			  
			          	protected void done()
			          	{  
			          		d.dispose();  
			          	}  
			        }; 
			        
			        worker.execute();  
			        try
			        {
			        	d.setVisible(true);
			        }
			        catch(Exception e)
			        {
			        	//do nothing
			        }
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		 
		if(e.getSource() == htsrchLbl)
		{
			htsrchLbl.setIcon(srchIconDwn);
		}
		else if(e.getSource() == homeTeachersLbl)
		{
			homeTeachersLbl.setIcon(hmTchDwnIcon);
		}
		else if(e.getSource() == changesLbl)
		{
			changesLbl.setIcon(chngDwnIcon);
		}
		else if(e.getSource() == reportedLbl)
		{
			reportedLbl.setIcon(rprtDwnIcon);
		}
		else if(e.getSource() == sendBoth)
		{
			sendBoth.setIcon(bothDwnIcon);
		}
		else if(e.getSource() == sendEmail)
		{
			sendEmail.setIcon(emailDwnIcon);
		}
		else if(e.getSource() == sendText)
		{
			sendText.setIcon(textDwnIcon);
		}
		else if(e.getSource() == settingsLbl)
		{
			settingsLbl.setIcon(settingsDwnIcon);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		
		if(e.getSource() == htsrchLbl)
		{
			htsrchLbl.setIcon(srchIcon);
			
			SwingUtilities.invokeLater(new Runnable()
			{

				@Override
				public void run() 
				{
					doFilter(htSrchField, htModel, allHTers);
				}
				
			});
			
		}
		else if(e.getSource() == rprtSrchLbl)
		{
			rprtSrchLbl.setIcon(srchIcon);
			
			SwingUtilities.invokeLater(new Runnable()
			{

				@Override
				public void run() 
				{
					doFilter(rprtSrchField, rprtModel, nonReportees);	
				}
				
			});
			
		}
		else if(e.getSource() == homeTeachersLbl)
		{
			homeTeachersLbl.setIcon(hmTchIcon);
			clearMainPanel();
			mainPanel.add(initHomeTeachersView(), BorderLayout.CENTER);
			mainFrame.pack();
		}
		else if(e.getSource() == changesLbl)
		{
			changesLbl.setIcon(chngIcon);
			clearMainPanel();
			mainPanel.add(initChangesView(), BorderLayout.CENTER);
//			mainFrame.pack();
		}
		else if(e.getSource() == reportedLbl)
		{
			reportedLbl.setIcon(rprtIcon);
			clearMainPanel();
			mainPanel.add(initReportedView(), BorderLayout.CENTER);
			mainFrame.setSize(800, 700);
		}
		else if(e.getSource() == settingsLbl)
		{
			settingsLbl.setIcon(settingsIcon);
			clearMainPanel();
			mainPanel.add(initSettingsView(), BorderLayout.CENTER);
		}
		else if(e.getSource() == sendBoth)
		{
			sendBoth.setIcon(bothIcon);
			
			final JDialog d = new JDialog();  
	        MigLayout mig = new MigLayout("","10[grow][grow]10","10[grow]10");
			
			JPanel p1 = new JPanel(mig);  
	       
	        d.getContentPane().add(p1);  
	        d.setLocationRelativeTo(mainFrame);  
	        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);  
	        d.setModal(true);  
			
			p1.add(new JLabel("Sending Emails and Texts"), "cell 1 0");
			p1.add(new JLabel(loading), "cell 2 0");
			d.pack();  
			d.setResizable(false);
			
			SwingWorker<?,?> worker = new SwingWorker<Void,Void>()
				{    
		          	protected Void doInBackground() throws InterruptedException
		          	{  
		          		if(rprtList != null)
						{
							if(rprtList.isSelectionEmpty())
							{		
								String emailTxt = emailText.getText();
								String textTxt = mainTxtText.getText();
								String famTxt = familyTxtText.getText();
								
								for(int i=0; i<rprtModel.getSize(); i++)
								{
									Person p = (Person)rprtModel.get(i);
									if(p != null)
									{
										String updatedEmailTxt = updateEmailForPerson(p,emailTxt);
										
										if(sendEmail(p,updatedEmailTxt) == false)
										{
											System.out.println("Error Sending email to " + p.getFullName());	
										}
										else
										{
											System.out.println("Success sending email to " + p.getFullName());
										}
										
										String updatedTextTxt = updateTextForPerson(p,textTxt);
										boolean success = false;
										if(sendText(p,updatedTextTxt) == false)
										{
											System.out.println("Error Sending text to " + p.getFullName());	
										}
										else
										{
											success = true;
											System.out.println("Success sending text to " + p.getFullName());
										}
										
										if(success)
										{
											Companionship comp = ward.getCompanionShip(p);
											int count = 0;
											for(Families fam : comp.getFamilies())
											{
												String famTextTxt = updateTextForPersonsFamilies(fam, famTxt);
												if(sendText(p,famTextTxt) == false)
												{
													System.out.println("Error Sending family text to " + p.getFullName());	
												}
												else
												{
													success = true;
													System.out.println("Success sending family text to " + p.getFullName());
												}
												if(++count > 4)
													break;
											}
										}
									}
								}
							}
							else 
							{					
								List<Person> pList = rprtList.getSelectedValuesList();
								
								if(pList != null)
								{
									String emailTxt = emailText.getText();
									String textTxt = mainTxtText.getText();
									String famTxt = familyTxtText.getText();
									
									for(Person p : pList)
									{
										String updatedEmailTxt = updateEmailForPerson(p,emailTxt);
										
										if(sendEmail(p,updatedEmailTxt) == false)
										{
											System.out.println("Error Sending email to " + p.getFullName());	
										}
										else
										{
											System.out.println("Success sending email to " + p.getFullName());
										}
										
										
										String updatedTextTxt = updateTextForPerson(p,textTxt);
										boolean success = false;
										if(sendText(p,updatedTextTxt) == false)
										{
											System.out.println("Error Sending text to " + p.getFullName());	
										}
										else
										{
											success = true;
											System.out.println("Success sending text to " + p.getFullName());
										}
										
										if(success)
										{
											Companionship comp = ward.getCompanionShip(p);
											int count = 0;
											for(Families fam : comp.getFamilies())
											{
												String famTextTxt = updateTextForPersonsFamilies(fam, famTxt);
												if(sendText(p,famTextTxt) == false)
												{
													System.out.println("Error Sending family text to " + p.getFullName());	
												}
												else
												{
													success = true;
													System.out.println("Success sending family text to " + p.getFullName());
												}
												
												if(++count > 4)
													break;
											}
										}
									}
								}
							}
						}
						
		          		return null;  
		          	}  
	  
	  
		          	protected void done()
		          	{  
		          		d.dispose();  
		          	}  
				}; 
	        
	        worker.execute();  
	        d.setVisible(true); 
		}
		else if(e.getSource() == sendEmail)
		{
			sendEmail.setIcon(emailIcon);
			
			final JDialog d = new JDialog();  
	        MigLayout mig = new MigLayout("","10[grow][grow]10","10[grow]10");
			
			JPanel p1 = new JPanel(mig);  
	       
	        d.getContentPane().add(p1);  
	        d.setLocationRelativeTo(mainFrame);  
	        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);  
	        d.setModal(true);  
			
			p1.add(new JLabel("Sending Email"), "cell 1 0");
			p1.add(new JLabel(loading), "cell 2 0");
			d.pack();  
			d.setResizable(false);
			
			SwingWorker<?,?> worker = new SwingWorker<Void,Void>()
				{    
		          	protected Void doInBackground() throws InterruptedException
		          	{  	
		          		if(rprtList != null)
						{
							if(rprtList.isSelectionEmpty())
							{	
								String emailTxt = emailText.getText();
								for(int i=0; i<rprtModel.getSize(); i++)
								{
									Person p = (Person)rprtModel.get(i);
									
									if(p != null)
									{
										String updatedEmailTxt = updateEmailForPerson(p,emailTxt);
										
										if(sendEmail(p,updatedEmailTxt) == false)
										{
											System.out.println("Error Sending email to " + p.getFullName());	
										}
										else
										{
											System.out.println("Success sending email to " + p.getFullName());
										}
									}
								}
							}
							else 
							{					
								List<Person> pList = rprtList.getSelectedValuesList();
								
								if(pList != null)
								{
									String emailTxt = emailText.getText();
									for(Person p : pList)
									{
										String updatedEmailTxt = updateEmailForPerson(p,emailTxt);
										
										if(sendEmail(p,updatedEmailTxt) == false)
										{
											System.out.println("Error Sending email to " + p.getFullName());	
										}
										else
										{
											System.out.println("Success sending email to " + p.getFullName());
										}
									}
								}
							}
						}
		          		return null;  
		          	}  
	  
	  
		          	protected void done()
		          	{  
		          		d.dispose();  
		          	}  
				}; 
	        
	        worker.execute();  
	        d.setVisible(true); 
		}
		else if(e.getSource() == sendText)
		{
			sendText.setIcon(textIcon);
			
			final JDialog d = new JDialog();  
	        MigLayout mig = new MigLayout("","10[grow][grow]10","10[grow]10");
			
			JPanel p1 = new JPanel(mig);  
	       
	        d.getContentPane().add(p1);  
	        d.setLocationRelativeTo(mainFrame);  
	        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);  
	        d.setModal(true);  
			
			p1.add(new JLabel("Sending Texts"), "cell 1 0");
			p1.add(new JLabel(loading), "cell 2 0");
			d.pack();  
			d.setResizable(false);
			
			SwingWorker<?,?> worker = new SwingWorker<Void,Void>()
				{    
		          	protected Void doInBackground() throws InterruptedException
		          	{  	
		          		if(rprtList != null)
						{
							if(rprtList.isSelectionEmpty())
							{		
								String textTxt = mainTxtText.getText();
								String famTxt = familyTxtText.getText();
								
								for(int i=0; i<rprtModel.getSize(); i++)
								{
									Person p = (Person)rprtModel.get(i);
									
									if(p != null)
									{
										
										String updatedTextTxt = updateTextForPerson(p,textTxt);
										boolean success = false;
										if(sendText(p,updatedTextTxt) == false)
										{
											System.out.println("Error Sending text to " + p.getFullName());	
										}
										else
										{
											success = true;
											System.out.println("Success sending text to " + p.getFullName());
										}
										
										if(success)
										{
											Companionship comp = ward.getCompanionShip(p);
											int count = 0;
											for(Families fam : comp.getFamilies())
											{
												String famTextTxt = updateTextForPersonsFamilies(fam, famTxt);
												if(sendText(p,famTextTxt) == false)
												{
													System.out.println("Error Sending family text to " + p.getFullName());	
												}
												else
												{
													success = true;
													System.out.println("Success sending family text to " + p.getFullName());
												}
												
												if(++count > 4)
													break;
											}
										}
									}
								}
							}
							else 
							{					
								List<Person> pList = rprtList.getSelectedValuesList();
								
								if(pList != null)
								{
									String textTxt = mainTxtText.getText();
									String famTxt = familyTxtText.getText();
									
									for(Person p : pList)
									{
										
										String updatedTextTxt = updateTextForPerson(p,textTxt);
										boolean success = false;
										if(sendText(p,updatedTextTxt) == false)
										{
											System.out.println("Error Sending text to " + p.getFullName());	
										}
										else
										{
											success = true;
											System.out.println("Success sending text to " + p.getFullName());
										}
										
										if(success)
										{
											Companionship comp = ward.getCompanionShip(p);
											int count = 0;
											for(Families fam : comp.getFamilies())
											{
												String famTextTxt = updateTextForPersonsFamilies(fam, famTxt);
												if(sendText(p,famTextTxt) == false)
												{
													System.out.println("Error Sending family text to " + p.getFullName());	
												}
												else
												{
													success = true;
													System.out.println("Success sending family text to " + p.getFullName());
												}
												
												if(++count > 4)
													break;
											}
										}
									}
								}
							}
						}
		          		return null;  
		          	}  
	  
	  
		          	protected void done()
		          	{  
		          		d.dispose();  
		          	}  
				}; 
	        
	        worker.execute();  
	        d.setVisible(true); 
		}
	}
	
	private boolean sendEmail(Person p, String message)
	{
		if(msg == null)
		{
			msg = new SendMessage(DefaultMessages.userName, DefaultMessages.password);
		}
		
		if(p.getGmailInfo() != null ) 
		{
			if(p.getGmailInfo().getEmail() != null)
			{
				GmailInfo gmail = p.getGmailInfo();
				msg.sendTo(new String[] {gmail.getFirstName() + " " + gmail.getLastName() + "<" + p.getGmailInfo().getEmail() + ">"});
				msg.setSubject("Home Teaching");
				msg.setMessage(message);
			
				if(msg.sendMessage())
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean sendText(Person p, String message)
	{
		if(msg == null)
		{
			msg = new SendMessage(DefaultMessages.userName, DefaultMessages.password);
		}
		
		if(p.getGmailInfo() != null ) 
		{
			if(p.getGmailInfo().getPhone() != null)
			{
				GmailInfo gmail = p.getGmailInfo();
				msg.sendTo(new String[] {gmail.getFirstName() + " " + gmail.getLastName() + "<" + gmail.getPhone() +">"});
				msg.setSubject("");
				msg.setMessage(message);
			
				if(msg.sendMessage())
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void doFilter(JTextField field, DefaultListModel model, Person[] list)
	{
		String enteredTxt = field.getText();
		
		if(enteredTxt != null)
		{
			Person[] filtered = filterList(enteredTxt, list);
			
			if(filtered != null)
			{
				model.clear();
				
				for(Person p : filtered)
				{
					model.addElement(p);
				}
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent v) 
	{
		if(v.getSource() == htList)
		{
			Person homeTeacher = (Person)htList.getSelectedValue();
			
			if(homeTeacher != null)
			{
				Companionship comp = ward.getCompanionShip(homeTeacher);
				
				if(comp != null)
				{
					infoDisplay.setText(comp.toString());
				}
				else
				{
					infoDisplay.setText("no info");
				}
			}
		}
		else if(v.getSource() == rprtList)
		{
			if(rprtList.isSelectionEmpty())
			{
				emailText.setText(DefaultMessages.notReportedEmail);
				mainTxtText.setText(DefaultMessages.notReportedText);
			}
			else
			{
				Person homeTeacher = (Person)rprtList.getSelectedValue();
				
				if(homeTeacher != null)
				{
					Companionship comp = ward.getCompanionShip(homeTeacher);
					
					if(comp != null)
					{
						emailText.setText(updateEmailForPerson(homeTeacher));
						mainTxtText.setText(updateTextForPerson(homeTeacher));
					}
					else
					{
						
					}
				}
			}
		}
	}
	
	private String updateEmailForPerson(Person p)
	{
		Companionship comp = ward.getCompanionShip(p);
		
		String personalizedMessage = "Error";
		String pattern = "([A-Za-z \\d\\.\\s,]*)<Last Name>" +
				"([A-Za-z \\d\\.\\s,]*)<Month>" +
				"([A-Za-z \\d\\.\\s,]*)<Reporting Link>" +
				"([A-Za-z \\d\\.\\s,]*)<Assignment>" +
				"([A-Za-z \\d\\.\\s,]*)";
		Pattern pat = Pattern.compile(pattern);
		Matcher m = pat.matcher(DefaultMessages.notReportedEmail);
		
		if( m.find() )
		{
			personalizedMessage = m.group(1) + p.getLastName() +
					m.group(2) + getSelectedMonth() +
					m.group(3) + createReportingLink(p) +
					m.group(4) + comp.toString() +
					m.group(5);
		}
		
		return personalizedMessage;
	}
	
	private String updateEmailForPerson(Person p, String message)
	{
		Companionship comp = ward.getCompanionShip(p);
		
		String personalizedMessage = message;
		String pattern = "([A-Za-z \\d\\.\\s,]*)<Last Name>" +
				"([A-Za-z \\d\\.\\s,]*)<Month>" +
				"([A-Za-z \\d\\.\\s,]*)<Reporting Link>" +
				"([A-Za-z \\d\\.\\s,]*)<Assignment>" +
				"([A-Za-z \\d\\.\\s,]*)";
		Pattern pat = Pattern.compile(pattern);
		Matcher m = pat.matcher(message);
		
		if( m.find() )
		{
			personalizedMessage = m.group(1) + p.getLastName() +
					m.group(2) + getSelectedMonth() +
					m.group(3) + createReportingLink(p) +
					m.group(4) + comp.toString() +
					m.group(5);
		}
		
		return personalizedMessage;
	}
	
	private String updateTextForPerson(Person p)
	{
		String message = "";
		
		String pattern = "([A-Za-z \\d\\.\\s,]*)<Month>" +
				"([A-Za-z \\d\\.\\s,]*)";
		Pattern pat = Pattern.compile(pattern);
		Matcher m = pat.matcher(DefaultMessages.notReportedText);
		
		if( m.find() )
		{
			message = m.group(1) + getSelectedMonth() +
					m.group(2);
		}
		
		return message;
	}
	
	private String updateTextForPerson(Person p, String txt)
	{
		String message = txt;
		
		String pattern = "([A-Za-z \\d\\.\\s,]*)<Month>" +
				"([A-Za-z \\d\\.\\s,]*)";
		Pattern pat = Pattern.compile(pattern);
		Matcher m = pat.matcher(txt);
		
		if( m.find() )
		{
			message = m.group(1) + getSelectedMonth() +
					m.group(2);
		}
		
		return message;
	}
	
	private String updateTextForPersonsFamilies(Families fam, String txt)
	{
		String message = txt;
		String pattern = "([A-Za-z \\d\\.\\s,]*)<Month>" +
				"([A-Za-z \\d\\.\\s,]*)<Family>(.*)";
		
		Pattern pat = Pattern.compile(pattern);
		Matcher m = pat.matcher(txt);
		
		if( m.find() )
		{
			message = m.group(1) + getSelectedMonth() +
					m.group(2) + fam.getHeadOfHouse().getLastName() + m.group(3);
		}
		
		return message;
	}
	
	public void updateWard(Ward ward)
	{
		this.ward = ward;
	}
	
	public void switchFromSettingsView()
	{
		clearMainPanel();
		mainPanel.add(initHomeTeachersView(), BorderLayout.CENTER);
		mainFrame.pack();
	}
	
	private String createReportingLink(Person p)
	{
		String link = DefaultMessages.reportingLink;
		
		Companionship companionship = ward.getCompanionShip(p);
		Person comp = null;
		
		for(Person peops : companionship.getCompanionship())
		{
			if(peops.isThisPerson(p) == false)
			{
				comp = peops;
			}
		}
		
		Person dl = null;
		
		ward:for(Quorum q : ward.getQuorums())
		{
			for(District d : q.getDistricts())
			{
				if(d.isInDistrict(p))
				{
					dl = d.getDistrictLeader();
					break ward;
				}
			}
		}
		
		String selectedDL = null;
		if(dl != null)
		{
			String names = dl.getFirstName();
			String last = dl.getLastName();
			for(String s : DefaultMessages.districtLeaders)
			{
				if(s.matches(".*" + last + ".*"))
				{
					selectedDL = s;
					break;
				}
					
			}
		}
		
		List<Families> fams = companionship.getFamilies();
		String[] famKeys = DefaultMessages.famKeys;
		
		link += DefaultMessages.monthKey + (getSelectedMonth() != null?getSelectedMonth():"") // key for month
				+ DefaultMessages.yourName + (p != null && p.getFullName()!=null?p.getFullName():"" ) // key for persons name
				+ DefaultMessages.compName + (comp != null && comp.getFullName() != null?comp.getFullName():"") // key for companions name
				+ DefaultMessages.yourDL + (selectedDL != null ? selectedDL : ""); // key for district leader
		
		for(int i=0; i<fams.size() && i<4; i++)
		{
			link += famKeys[i];
			if(fams.get(i).getHeadOfHouse().getFullName() != null)
			{
				link += ((fams.get(i) != null && 
						fams.get(i).getHeadOfHouse() != null && 
						fams.get(i).getHeadOfHouse().getFullName() != null)?fams.get(i).getHeadOfHouse().getFullName():"");
			}
		}
						
		return link.replaceAll(" ", "%20");
	}

}
