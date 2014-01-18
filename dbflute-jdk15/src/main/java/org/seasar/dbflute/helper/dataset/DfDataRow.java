package org.seasar.dbflute.helper.dataset;

import java.util.ArrayList;
import java.util.List;

import org.seasar.dbflute.helper.dataset.states.DfDtsRowState;
import org.seasar.dbflute.helper.dataset.states.DfDtsRowStates;
import org.seasar.dbflute.helper.dataset.types.DfDtsColumnType;
import org.seasar.dbflute.helper.dataset.types.DfDtsColumnTypes;

/**
 * {Created with reference to S2Container's utility and extended for DBFlute}
 * @author jflute
 * @since 0.8.3 (2008/10/28 Tuesday)
 */
public class DfDataRow {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final DfDataTable _table;
    protected final int _rowNumber;
    protected final List<Object> _values = new ArrayList<Object>();
    protected DfDtsRowState _state = DfDtsRowStates.UNCHANGED;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfDataRow(DfDataTable table, int rowNumber) {
        _table = table;
        _rowNumber = rowNumber;
    }

    // ===================================================================================
    //                                                                      Value Handling
    //                                                                      ==============
    public Object getValue(int index) {
        return _values.get(index);
    }

    public Object getValue(String columnName) {
        final DfDataColumn column = _table.getColumn(columnName);
        return _values.get(column.getColumnIndex());
    }

    public void addValue(String columnName, Object value) {
        final DfDataColumn column = _table.getColumn(columnName);
        _values.add(column.convert(value));
        modify();
    }

    // *unused on DBFlute
    // public void setValue(int index, Object value) {
    //     final DataColumn column = _table.getColumn(index);
    //     _values.set(index, column.convert(value));
    //     modify();
    // }

    private void modify() {
        if (_state.equals(DfDtsRowStates.UNCHANGED)) {
            _state = DfDtsRowStates.MODIFIED;
        }
    }

    public void remove() {
        _state = DfDtsRowStates.REMOVED;
    }

    public DfDataTable getTable() {
        return _table;
    }

    public int getRowNumber() {
        return _rowNumber;
    }

    public DfDtsRowState getState() {
        return _state;
    }

    public void setState(DfDtsRowState state) {
        _state = state;
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DfDataRow)) {
            return false;
        }
        final DfDataRow other = (DfDataRow) o;
        for (int i = 0; i < _table.getColumnSize(); ++i) {
            final String columnName = _table.getColumnName(i);
            final Object value = _values.get(i);
            final Object otherValue = other.getValue(columnName);
            final DfDtsColumnType ct = DfDtsColumnTypes.getColumnType(value);
            if (ct.equals(value, otherValue)) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(100);
        sb.append("{");
        for (int i = 0; i < _values.size(); ++i) {
            sb.append(getValue(i));
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append('}');
        return sb.toString();
    }
}
