package org.seasar.dbflute.cbean.chelper;

import org.seasar.dbflute.cbean.coption.ColumnConversionOption;

/**
 * @author jflute
 */
public class HpCalcElement {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected CalculationType _calculationType;
    protected Number _calculationValue;
    protected HpSpecifiedColumn _calculationColumn;
    protected ColumnConversionOption _columnConversionOption;
    protected boolean _preparedConvOption;

    // ===================================================================================
    //                                                                       Determination 
    //                                                                       =============
    public boolean hasCalculationValue() {
        return _calculationValue != null;
    }

    public boolean hasCalculationColumn() {
        return _calculationColumn != null;
    }

    // ===================================================================================
    //                                                                    Calculation Type 
    //                                                                    ================
    public static enum CalculationType {
        CONV("$$FUNC$$"), PLUS("+"), MINUS("-"), MULTIPLY("*"), DIVIDE("/");
        private String _operand;

        private CalculationType(String operand) {
            _operand = operand;
        }

        public String operand() {
            return _operand;
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public CalculationType getCalculationType() {
        return _calculationType;
    }

    public void setCalculationType(CalculationType calculationType) {
        this._calculationType = calculationType;
    }

    public Number getCalculationValue() {
        return _calculationValue;
    }

    public void setCalculationValue(Number calculationValue) {
        this._calculationValue = calculationValue;
    }

    public HpSpecifiedColumn getCalculationColumn() {
        return _calculationColumn;
    }

    public void setCalculationColumn(HpSpecifiedColumn calculationColumn) {
        this._calculationColumn = calculationColumn;
    }

    public ColumnConversionOption getColumnConversionOption() {
        return _columnConversionOption;
    }

    public void setColumnConversionOption(ColumnConversionOption columnConversionOption) {
        this._columnConversionOption = columnConversionOption;
    }

    public boolean isPreparedConvOption() {
        return _preparedConvOption;
    }

    public void setPreparedConvOption(boolean preparedConvOption) {
        this._preparedConvOption = preparedConvOption;
    }
}
