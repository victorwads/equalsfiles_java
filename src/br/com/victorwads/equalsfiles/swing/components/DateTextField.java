/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.components;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.accessibility.Accessible;
import javax.swing.AbstractAction;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
// </editor-fold>

/**
 *
 * @author victo
 */
public class DateTextField extends JFormattedTextField implements Accessible {

	private final DefaultFormatterFactory DATA_MASK;
	private final DefaultFormatterFactory DATATIME_MASK;
	private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private final ArrayList<ChangeListener> listenners = new ArrayList<>();
	private boolean dateTime = false;

	public DateTextField() {
		format.setLenient(false);

		//Mascara
		MaskFormatter mD = null, mDT = null;
		try {
			mD = new MaskFormatter("##/##/####");
		} catch (Exception e) {
		}
		try {
			mDT = new MaskFormatter("##/##/#### ##:##:##");
		} catch (Exception e) {
		}
		DATA_MASK = new DefaultFormatterFactory(mD);
		DATATIME_MASK = new DefaultFormatterFactory(mDT);

		setFormatterFactory(DATA_MASK);
		setHorizontalAlignment(JTextField.CENTER);
		setFocusLostBehavior(JFormattedTextField.COMMIT);
		setFont(new Font("Monospaced", 0, 11));

		//Eventos
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent evt) {
				if (getDate() == null) {
					setText("");
				}
			}
		});
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP");
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN");
		getActionMap().put("UP", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				changeByKey(1);
			}
		});
		getActionMap().put("DOWN", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				changeByKey(-1);
			}
		});
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent fe) {
				setSelection();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				setSelection();
			}
		});
	}

	private void changeByKey(int val) {
		if (getDate() == null) {
			return;
		}
		int p = getCaretPosition();
		Calendar c = Calendar.getInstance();
		c.setTime(getDate());
		if (p > 16) {
			c.add(Calendar.SECOND, val);
		} else if (p > 13) {
			c.add(Calendar.MINUTE, val);
		} else if (p > 10) {
			c.add(Calendar.HOUR_OF_DAY, val);
		} else if (p > 5) {
			c.add(Calendar.YEAR, val);
		} else if (p > 2) {
			c.add(Calendar.MONTH, val);
		} else {
			c.add(Calendar.DAY_OF_MONTH, val);
		}
		setDate(c.getTime());
		setCaretPosition(p);
		setSelection();
	}

	private void setSelection() {
		if (getDate() == null) {
			return;
		}
		int p = getCaretPosition();
		if (p > 16) {
			setSelectionStart(17);
			setSelectionEnd(19);
		} else if (p > 13) {
			setSelectionStart(14);
			setSelectionEnd(16);
		} else if (p > 10) {
			setSelectionStart(11);
			setSelectionEnd(13);
		} else if (p > 5) {
			setSelectionStart(6);
			setSelectionEnd(10);
		} else if (p > 2) {
			setSelectionStart(3);
			setSelectionEnd(5);
		} else {
			setSelectionStart(0);
			setSelectionEnd(2);
		}
	}

	public boolean isDateTime() {
		return dateTime;
	}

	public void setDateTime(boolean dateTime) {
		if (dateTime) {
			setFormatterFactory(DATATIME_MASK);
		} else {
			setFormatterFactory(DATA_MASK);
		}
		this.dateTime = dateTime;
	}

	public Date getDate() {
		try {
			if (dateTime) {
				return format.parse(getText());
			} else {
				return format.parse(getText() + " 23:59:59");
			}
		} catch (ParseException ex) {
			return null;
		}
	}

	public void setDate(Date date) {
		if (dateTime) {
			setText(format.format(date));
		} else {
			setText(format.format(date).substring(0, 10));
		}
	}

	/**
	 *
	 * @param listenner
	 */
	public void addChangeListenner(ChangeListener listenner) {
		listenners.add(listenner);
	}

	@Override
	public void setText(String val) {
		super.setText(val);
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener l : listenners) {
			l.stateChanged(event);
		}
	}
}
