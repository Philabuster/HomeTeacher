package com.Communcation;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CompanionshipCellRenderer extends JLabel implements ListCellRenderer<Companionship>
{
	public CompanionshipCellRenderer() {
        setOpaque(true);
    }


	@Override
	public Component getListCellRendererComponent(
			JList<? extends Companionship> list, Companionship comps,
			int index, boolean isSelected, boolean cellHasFocus) {

		String names = "";
		for(Person p : comps.getCompanionship())
		{
			names += p.getFullName() + " ";
		}
		
		setText(names.trim());

        Color background;
        Color foreground;

        // check if this cell represents the current DnD drop location
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            background = Color.BLUE;
            foreground = Color.WHITE;

        // check if this cell is selected
        } else if (isSelected) {
            background = Color.RED;
            foreground = Color.WHITE;

        // unselected, and not the DnD drop location
        } else {
            background = Color.WHITE;
            foreground = Color.BLACK;
        };

        setBackground(background);
        setForeground(foreground);

        return this;
		
	}

}
