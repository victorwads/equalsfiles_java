/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.victorwads.equalsfiles.swing.components;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.accessibility.Accessible;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
// </editor-fold>

/**
 *
 * This Make a tree allowed to have CheckBox nodes with the
 * CheckBoxTree.DefaultNode class and not selectable nodes with the
 * NoSelectionNode class. This tree watch MouseClick and KeyPress for the Enter
 * key and Space key to toggle selected state of checkboxes nodes.
 *
 *
 * @author Victor Cleber Laureano victor.laureano@gmail.com
 */
public class CheckBoxTree extends JTree implements Accessible {

	// <editor-fold defaultstate="collapsed" desc="Classes">
	/**
	 * DefaultNode extends DefaultMutableTreeNode. This node will be rendered
	 * with a JCheckBox and will be allowed to be select by click, EspaceKey and
	 * EnterKey (when node is focused)
	 */
	public static class DefaultNode extends DefaultMutableTreeNode {

		private boolean selected = false;

		public DefaultNode(Object o) {
			super(o);
		}

		public DefaultNode(Object o, boolean selected) {
			this(o);
			this.selected = selected;
		}

		public DefaultNode(Object o, boolean selected, boolean allowsChildren) {
			super(o, allowsChildren);
			this.selected = selected;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		@Override
		public String toString() {
			return super.getUserObject().toString();
		}

	}

	/**
	 * DefaultNoSelectionNode extends DefaultMutableTreeNode. This node will be
	 * not allowed to be selected or focused on the tree view. On focus the
	 * renderer send focus to next node
	 */
	public static class NoSelectionNode extends DefaultMutableTreeNode {

		public NoSelectionNode(Object o) {
			super(o);
		}

		public NoSelectionNode(Object o, boolean bln) {
			super(o, bln);
		}

		@Override
		public String toString() {
			return "";
		}
	}

	private class Renderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

		private Component c;
		private final JCheckBox checkBox = new JCheckBox();
		private final JComponent container = new JComponent() {
		};

		public Renderer() {
			container.setVisible(true);
			container.setLayout(new FlowLayout(0, 0, 0));
			container.setOpaque(false);
			checkBox.setOpaque(false);
		}

		/**
		 *
		 * @param jtree Jtree object.
		 * @param o User Object.
		 * @param selecionado Will be true if the node is selected. Otherwise
		 * will be false.
		 * @param expandido Will be true if the node is expanded. Otherwise will
		 * be false.
		 * @param folha Will be true if the node is a leaf node. Otherwise will
		 * be false.
		 * @param row Node row number.
		 * @param hasFocus Will be true if the node is focused. Otherwise will
		 * be false.
		 * @return
		 */
		@Override
		public Component getTreeCellRendererComponent(JTree jtree, Object o, boolean selecionado, boolean expandido, boolean folha, int row, boolean hasFocus) {
			if (o instanceof DefaultNode) {
				DefaultNode node = (DefaultNode) o;
				selecionado = node.isSelected();
			} else if (o instanceof NoSelectionNode) {
				if (hasFocus) {
					lastFocusRow = row == 0 ? -1 : lastFocusRow;
					jtree.setSelectionRow(row + (row > lastFocusRow ? 1 : -1));
				}
				selecionado = false;
				hasFocus = false;
			}
			c = (Component) oldCellRender.getTreeCellRendererComponent(jtree, o, selecionado, expandido, folha, row, hasFocus);
			if (o instanceof DefaultNode) {
				checkBox.setSelected(selecionado);
				container.add(checkBox);
				container.add(c);
				c = container;
			}
			if (hasFocus) {
				lastFocusRow = row;
				lastFocusedPath = getPathForRow(row);
			}
			return c;
		}

	}
	// </editor-fold>

	private int lastFocusRow = -1;
	private TreePath lastFocusedPath = null;
	private TreeCellRenderer oldCellRender = new DefaultTreeCellRenderer();
	private final ArrayList<DefaultNode> selectedCheckBoxNodes = new ArrayList<>();

	/**
	 *
	 * Creates a CheckBoxTree swing Component
	 *
	 */
	public CheckBoxTree() {
		setRowHeight(0);
		super.setCellRenderer(new Renderer());
		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				toggleCheckBoxPath(getPathForLocation(evt.getX(), evt.getY()));
			}
		});
		super.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == 10 || evt.getKeyCode() == 32) {
					toggleCheckBoxPath(lastFocusedPath);
				}
			}
		});
	}

	/**
	 *
	 * Creates a CheckBoxTree swing Component. Receive a root TreeNode and
	 * setModel with a DefaultTreeModel with the root node.
	 *
	 * @param trenode
	 */
	public CheckBoxTree(TreeNode trenode) {
		this();
		setModel(new DefaultTreeModel(trenode));
	}

	// <editor-fold defaultstate="collapsed" desc="Methodos">
	private void addSelectedNode(DefaultNode node) {
		if (node.isSelected() && selectedCheckBoxNodes.indexOf(node) == -1) {
			selectedCheckBoxNodes.add(node);
		}
	}

	private boolean removeSelectedNode(DefaultNode node) {
		return selectedCheckBoxNodes.remove(node);
	}

	private void addSelecitedNodesFromNode(TreeNode node) {
		if (node instanceof DefaultNode) {
			addSelectedNode(((DefaultNode) node));
		}
		for (int i = 0; i < node.getChildCount(); i++) {
			addSelecitedNodesFromNode(node.getChildAt(i));
		}
	}

	/**
	 * Toggle selected checkbox on path (path.getLastPathComponent() instance of
	 * CheckBoxTree.DefaultNode).
	 *
	 * @param path The path to be toggled.
	 */
	public void toggleCheckBoxPath(TreePath path) {
		if (path == null) {
			return;
		}
		Object o = path.getLastPathComponent();
		if (o instanceof DefaultNode) {
			DefaultNode n = (DefaultNode) o;
			n.setSelected(!n.isSelected());
			if (n.isSelected()) {
				addSelectedNode(n);
			} else {
				removeSelectedNode(n);
			}
			treeDidChange();
		}
	}

	/**
	 * Return all selected CheckBoxTree.DefaultNode of the CheckBoxTree
	 *
	 * @return DefaultNode[] List of all selected CheckBoxTree.DefaultNode.
	 */
	public DefaultNode[] getSelectedCheckBoxNodes() {
		DefaultNode[] returns = new DefaultNode[selectedCheckBoxNodes.size()];
		for (int i = 0; i < selectedCheckBoxNodes.size(); i++) {
			returns[i] = selectedCheckBoxNodes.get(i);
		}
		return returns;
	}

	/**
	 * Expand All Nodes of the tree
	 */
	public void expandAll() {
		EventQueue.invokeLater(() -> {
			for (int i = 0; i < getRowCount(); i++) {
				expandRow(i);
			}
		});
	}

	/**
	 *
	 * @param model
	 */
	@Override
	public final void setModel(TreeModel model) {
		super.setModel(model);
		if (selectedCheckBoxNodes != null) {
			selectedCheckBoxNodes.clear();
			addSelecitedNodesFromNode((TreeNode) model.getRoot());
		}
	}

	/**
	 *
	 * @param renderer
	 */
	@Override
	public void setCellRenderer(TreeCellRenderer renderer) {
		oldCellRender = renderer;
	}
	// </editor-fold>
}
