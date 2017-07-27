/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.components;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.awt.Dimension;
import java.awt.Insets;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author victo
 */
public class IconCheckBox extends JComponent implements Accessible {

	private final JCheckBox checkbox = new JCheckBox();
	private final JLabel label = new JLabel();

	private void fixPositions() {
		Insets insets = getInsets();
		Dimension lsize, csize;

		//Label
		lsize = label.getPreferredSize();
		label.setBounds(insets.left + 22, insets.top + 1, lsize.width, lsize.height);
		label.setVisible(true);
		checkbox.setIconTextGap(lsize.width + 10);

		csize = checkbox.getPreferredSize();
		checkbox.setBounds(insets.left, insets.top, csize.width, csize.height);
		checkbox.setVisible(true);
		checkbox.setOpaque(false);

		setPreferredSize(new Dimension(100, csize.height));
	}

	public IconCheckBox() {
		setLayout(null);
		add(label);
		add(checkbox);
		fixPositions();
	}

	public IconCheckBox(Icon icon) {
		this();
		setIcon(icon);
	}

	public IconCheckBox(String string) {
		this();
		setText(string);
	}

	public IconCheckBox(Icon icon, boolean bln) {
		this();
		setSelected(bln);
		setIcon(icon);
	}

	public IconCheckBox(String string, boolean bln) {
		this(string);
		setSelected(bln);
	}

	public IconCheckBox(String string, Icon icon) {
		this(string);
		setIcon(icon);
	}

	public IconCheckBox(String string, Icon icon, boolean bln) {
		this(string, bln);
		setIcon(icon);
	}

	public IconCheckBox(Action action) {
		checkbox.setAction(action);
	}

	public void setBorderPaintedFlat(boolean bln) {
		checkbox.setBorderPaintedFlat(bln);
	}

	public boolean isBorderPaintedFlat() {
		return checkbox.isBorderPaintedFlat();
	}

	public Icon getIcon() {
		return label.getIcon();
	}

	public void setIcon(Icon icon) {
		label.setIcon(icon);
		fixPositions();

	}

	public String getText() {
		return checkbox.getText();
	}

	public void setText(String text) {
		checkbox.setText(text);
		fixPositions();
	}

	public void setSelected(boolean bln) {
		checkbox.setSelected(bln);
	}

	public boolean isSelected() {
		return checkbox.isSelected();
	}

	@Override
	public AccessibleContext getAccessibleContext() {
		return checkbox.getAccessibleContext();
	}

	@Override
	public void setEnabled(boolean bln) {
		checkbox.setEnabled(bln);
		label.setEnabled(bln);
	}

	@Override
	public boolean isEnabled() {
		return checkbox.isEnabled();
	}

	@Override
	public String getUIClassID() {
		return checkbox.getUIClassID() + "Icon";
	}
}
