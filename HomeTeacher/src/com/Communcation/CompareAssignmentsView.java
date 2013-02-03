package com.Communcation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JScrollPane;
import com.jgoodies.forms.layout.Sizes;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CompareAssignmentsView implements ActionListener, MouseListener, ListSelectionListener
{
	GUI parent = null;
	Ward latest = null;
	Ward old = null;
	List<Companionship> comps = new ArrayList<Companionship>();
	AccountInfo ai;
	private JTextField oldTextField;
	private JTextField updatedTextField;
	private JTextField emailChangesField;
	private DefaultListModel changeModel;
	private JList changeList;
	private JTextField searchField;
	private JLabel searchLbl;
	private Person[] changes;
	
	public CompareAssignmentsView(GUI parent, Ward latest)
	{
		this.parent = parent;
		this.latest = latest;
		ai = AccountInfo.getAccountInfo();
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel initView()
	{
		JPanel comparePanel = new JPanel();
		comparePanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.bounded(Sizes.PREFERRED, Sizes.constant("147dlu", true), Sizes.constant("200dlu", true)), 0),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		Border border = BorderFactory.createLineBorder(Color.darkGray);
		TitledBorder title = BorderFactory.createTitledBorder("Update Latest Assignments");
		title.setTitleJustification(TitledBorder.CENTER);
		title.setBorder(border);
		
		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compound = BorderFactory.createCompoundBorder(title, emptyBorder );
		compound = BorderFactory.createCompoundBorder(emptyBorder, compound);
		
		comparePanel.setBorder(compound);
		
		JPanel htPanel = new JPanel();
		comparePanel.add(htPanel, "2, 2, 1, 5, fill, fill");
		htPanel.setLayout(new BorderLayout(0, 0));
		
		Border border2 = BorderFactory.createLineBorder(parent.lightBlue);
		TitledBorder title2 = BorderFactory.createTitledBorder("Home Teachers");
		title2.setTitleJustification(TitledBorder.CENTER);
		title2.setBorder(border2);
		
		Border emptyBorder2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compound2 = BorderFactory.createCompoundBorder(emptyBorder2, title2);
		
		htPanel.setBorder(compound2);
		
		
		if(changeModel == null)
		{
			changeModel = new DefaultListModel();
		}
		
		if(changeList == null)
		{
			changeList = new JList();
			changeList.setModel(changeModel);
			changeList.setCellRenderer(new PersonCellRenderer());
			changeList.addListSelectionListener(this);
		}
		
		JScrollPane namesScrollPane = new JScrollPane(changeList);
		
		htPanel.add(namesScrollPane);
		
		if(searchField == null)
		{
			searchField = new JTextField(12);
		}
		
		if(searchLbl == null)
		{
			searchLbl = new JLabel(parent.srchIcon);
			searchLbl.addMouseListener(this);
		}
		
		JPanel searchPanel = new JPanel(new FlowLayout());
		searchPanel.add(searchField);
		searchPanel.add(parent.htsrchLbl);
		
		htPanel.add(searchPanel, BorderLayout.SOUTH);
		
		JScrollPane updatedScrollPane = new JScrollPane();
		comparePanel.add(updatedScrollPane, "4, 2, fill, fill");
		
		JScrollPane previousScrollPane = new JScrollPane();
		comparePanel.add(previousScrollPane, "4, 4, fill, fill");
		
		JPanel emailButtonPanel = new JPanel();
		comparePanel.add(emailButtonPanel, "4, 6, fill, fill");
		
		JButton btnNewButton = new JButton("Email Changes");
		emailButtonPanel.add(btnNewButton);
		
		old = getOldAssignment();
		
		if(old != null)
		{
			
		}
		
		return comparePanel;
	}
	
	private Ward getOldAssignment()
	{
		Ward oldAssignments = null;
		
		try
		{
			String userName = ai.emailAccount;
			String password = ai.password;
			
			DocumentList doc = new DocumentList(userName, password);
			
			doc.downLoadFile(ai.comparePdfName, "pdf", ai.comparePdfName);
			String pdfInfo = doc.readInFamiles(ai.comparePdfName);
			
			if(pdfInfo != null)
			{
				PDFParser pdf = new PDFParser(pdfInfo);
				
				oldAssignments = pdf.createWard();
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(parent.mainFrame, "Could not properly retrieve " + ai.comparePdfName + ", comparison could not be made.");
		}
		
		return oldAssignments;
	}
	
	private HashMap findDifferences(Ward newAssignment, Ward oldAssignment)
	{
		HashMap<String,Companionship> differences = new HashMap<String,Companionship>();
		Person[] newHomeTeachers = newAssignment.getAllHomeTeachers();
		Person[] oldHomeTeachers = oldAssignment.getAllHomeTeachers();
		
		for(Person p : newHomeTeachers)
		{
			Companionship newComp = newAssignment.getCompanionShip(p);
			Companionship oldComp = oldAssignment.getCompanionShip(p);
			
			if(newComp == null)
			{
				continue;
			}
			
			if(oldComp == null)
			{
				differences.put(p.getFullName(), newComp);
				continue;
			}
			
			if(newComp.getFamilies().size() != oldComp.getFamilies().size())
			{
				differences.put(p.getFullName(), newComp);
			}
			else
			{
				boolean diffs = false;
				List<Families> newFamilies = newComp.getFamilies();
				List<Families> oldFamilies = oldComp.getFamilies();
				
				for(Families f : newFamilies)
				{
					
				}
			}
		}
	
		return differences;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		if(e.getSource() == changeList)
		{
			if(changeList.isSelectionEmpty())
			{
				oldTextField.setText("need to add message");
				updatedTextField.setText("need to add message");
				emailChangesField.setText("need to add message");
			}
			else
			{
				Person homeTeacher = (Person)changeList.getSelectedValue();
				
				if(homeTeacher != null)
				{
					// TODO: need to add logic for populating the info
//					Companionship comp = ward.getCompanionShip(homeTeacher);
//					
//					if(comp != null)
//					{
//						emailText.setText(updateEmailForPerson(homeTeacher));
//						mainTxtText.setText(updateTextForPerson(homeTeacher));
//					}
//					else
//					{
//						
//					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
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
		if(e.getSource() == searchLbl)
		{
			searchLbl.setIcon(parent.srchIconDwn);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if(e.getSource() == searchLbl)
		{
			searchLbl.setIcon(parent.srchIcon);
			
			SwingUtilities.invokeLater(new Runnable()
			{

				@Override
				public void run() 
				{
					parent.doFilter(searchField, changeModel, changes);
				}
				
			});
			
		}
	}
}
