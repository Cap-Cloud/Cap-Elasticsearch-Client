package com.cap.plugins.common.component.table;

import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.RowSorter.SortKey;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class DefaultTableCellHeaderRenderer extends DefaultTableCellRenderer implements UIResource {
    private boolean horizontalTextPositionSet;
    private Icon sortArrow;
    private DefaultTableCellHeaderRenderer.EmptyIcon emptyIcon = new DefaultTableCellHeaderRenderer.EmptyIcon();

    public DefaultTableCellHeaderRenderer() {
        this.setHorizontalAlignment(0);
    }

    public void setHorizontalTextPosition(int textPosition) {
        this.horizontalTextPositionSet = true;
        super.setHorizontalTextPosition(textPosition);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Icon sortIcon = null;
        boolean isPaintingForPrint = false;
        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                Color fgColor = null;
                Color bgColor = null;
                if (hasFocus) {
                    fgColor = DefaultLookup.getColor(this, this.ui, "TableHeader.focusCellForeground");
                    bgColor = DefaultLookup.getColor(this, this.ui, "TableHeader.focusCellBackground");
                }

                if (fgColor == null) {
                    fgColor = header.getForeground();
                }

                if (bgColor == null) {
                    bgColor = header.getBackground();
                }

                this.setForeground(fgColor);
                this.setBackground(bgColor);
                this.setFont(header.getFont());
                isPaintingForPrint = header.isPaintingForPrint();
            }

            if (!isPaintingForPrint && table.getRowSorter() != null) {
                if (!this.horizontalTextPositionSet) {
                    this.setHorizontalTextPosition(10);
                }

                SortOrder sortOrder = getColumnSortOrder(table, column);
                if (sortOrder != null) {
                    switch (sortOrder) {
                        case ASCENDING:
                            sortIcon = DefaultLookup.getIcon(this, this.ui, "Table.ascendingSortIcon");
                            break;
                        case DESCENDING:
                            sortIcon = DefaultLookup.getIcon(this, this.ui, "Table.descendingSortIcon");
                            break;
                        case UNSORTED:
                            sortIcon = DefaultLookup.getIcon(this, this.ui, "Table.naturalSortIcon");
                    }
                }
            }
        }

        this.setText(value == null ? "" : value.toString());
        this.setIcon(sortIcon);
        this.sortArrow = sortIcon;
        Border border = null;
        if (hasFocus) {
            border = DefaultLookup.getBorder(this, this.ui, "TableHeader.focusCellBorder");
        }

        if (border == null) {
            border = DefaultLookup.getBorder(this, this.ui, "TableHeader.cellBorder");
        }

        this.setBorder(border);
        return this;
    }

    public static SortOrder getColumnSortOrder(JTable table, int column) {
        SortOrder rv = null;
        if (table != null && table.getRowSorter() != null) {
            List<? extends SortKey> sortKeys = table.getRowSorter().getSortKeys();
            if (sortKeys.size() > 0 && ((SortKey) sortKeys.get(0)).getColumn() == table.convertColumnIndexToModel(column)) {
                rv = ((SortKey) sortKeys.get(0)).getSortOrder();
            }

            return rv;
        } else {
            return rv;
        }
    }

    public void paintComponent(Graphics g) {
        boolean b = DefaultLookup.getBoolean(this, this.ui, "TableHeader.rightAlignSortArrow", false);
        if (b && this.sortArrow != null) {
            this.emptyIcon.width = this.sortArrow.getIconWidth();
            this.emptyIcon.height = this.sortArrow.getIconHeight();
            this.setIcon(this.emptyIcon);
            super.paintComponent(g);
            Point position = this.computeIconPosition(g);
            this.sortArrow.paintIcon(this, g, position.x, position.y);
        } else {
            super.paintComponent(g);
        }

    }

    private Point computeIconPosition(Graphics g) {
        FontMetrics fontMetrics = g.getFontMetrics();
        Rectangle viewR = new Rectangle();
        Rectangle textR = new Rectangle();
        Rectangle iconR = new Rectangle();
        Insets i = this.getInsets();
        viewR.x = i.left;
        viewR.y = i.top;
        viewR.width = this.getWidth() - (i.left + i.right);
        viewR.height = this.getHeight() - (i.top + i.bottom);
        SwingUtilities.layoutCompoundLabel(this, fontMetrics, this.getText(), this.sortArrow, this.getVerticalAlignment(), this.getHorizontalAlignment(), this.getVerticalTextPosition(), this.getHorizontalTextPosition(), viewR, iconR, textR, this.getIconTextGap());
        int x = this.getWidth() - i.right - this.sortArrow.getIconWidth();
        int y = iconR.y;
        return new Point(x, y);
    }

    private class EmptyIcon implements Icon, Serializable {
        int width = 0;
        int height = 0;

        private EmptyIcon() {
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
        }

        public int getIconWidth() {
            return this.width;
        }

        public int getIconHeight() {
            return this.height;
        }
    }
}