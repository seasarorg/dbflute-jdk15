package com.example.dbflute.spring.dbflute.whitebox.allcommon;

import java.io.Serializable;
import java.util.List;

import org.seasar.dbflute.jdbc.Classification;
import org.seasar.dbflute.jdbc.ClassificationCodeType;
import org.seasar.dbflute.s2dao.valuetype.TnValueTypes;
import org.seasar.dbflute.unit.core.PlainTestCase;
import org.seasar.dbflute.util.DfTypeUtil;

import com.example.dbflute.spring.dbflute.allcommon.CDef;
import com.example.dbflute.spring.dbflute.allcommon.CDef.DefMeta;
import com.example.dbflute.spring.dbflute.allcommon.CDef.Flg;

/**
 * @author jflute
 * @since 0.9.5 (2009/04/08 Wednesday)
 */
public class WxCDefTest extends PlainTestCase {

    // ===================================================================================
    //                                                                            nameOf()
    //                                                                            ========
    public void test_nameOf() {
        assertEquals(CDef.Flg.True, CDef.Flg.nameOf("True"));
        assertNull(CDef.Flg.nameOf("true"));
        assertNull(CDef.Flg.nameOf("noexist"));
        assertNull(CDef.Flg.nameOf(null));
        assertNull(CDef.Flg.nameOf(""));
        DefMeta flgMeta = CDef.DefMeta.valueOf("Flg");
        assertEquals(CDef.Flg.True, flgMeta.nameOf("True"));
    }

    // ===================================================================================
    //                                                                           listAll()
    //                                                                           =========
    public void test_listAll_direct() {
        // ## Arrange ##
        // ## Act ##
        List<Flg> listAll = CDef.Flg.listAll();

        // ## Assert ##
        assertListNotEmpty(listAll);
        assertEquals(2, listAll.size());
        for (Flg flg : listAll) {
            log(flg);
        }
    }

    public void test_listAll_meta() {
        // ## Arrange ##
        // ## Act ##
        List<Classification> listAll = CDef.DefMeta.valueOf("Flg").listAll();

        // ## Assert ##
        assertListNotEmpty(listAll);
        assertEquals(2, listAll.size());
        for (Classification flg : listAll) {
            log(flg);
        }
    }

    // ===================================================================================
    //                                                                          codeType()
    //                                                                          ==========
    public void test_codeType() {
        // ## Arrange ##
        ClassificationCodeType stringType = ClassificationCodeType.String;
        ClassificationCodeType numberType = ClassificationCodeType.Number;

        // ## Act & Assert ##
        assertEquals(numberType, CDef.Flg.True.meta().codeType()); // as specified
        assertEquals(stringType, CDef.MemberStatus.Formalized.meta().codeType()); // as default
        assertEquals(TnValueTypes.CLASSIFICATION, TnValueTypes.getValueType(CDef.Flg.True));
    }

    // ===================================================================================
    //                                                                        Serializable
    //                                                                        ============
    public void test_serializable_basic() {
        // ## Arrange ##
        CDef.MemberStatus formalized = CDef.MemberStatus.Formalized;

        // ## Act ##
        byte[] binary = DfTypeUtil.toBinary(formalized);
        Serializable serializable = DfTypeUtil.toSerializable(binary);

        // ## Assert ##
        log(serializable);
        assertNotNull(serializable);
        assertEquals(formalized.toString(), serializable.toString());
    }
}
