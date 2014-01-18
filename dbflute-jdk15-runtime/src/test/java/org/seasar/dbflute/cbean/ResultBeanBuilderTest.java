package org.seasar.dbflute.cbean;

import org.seasar.dbflute.mock.MockColumnInfo;
import org.seasar.dbflute.unit.core.PlainTestCase;

/**
 * @author jflute
 * @since 0.9.5 (2009/05/27 Wednesday)
 */
public class ResultBeanBuilderTest extends PlainTestCase {

    public void test_buildEmptyListResultBean() {
        // ## Arrange ##
        SimplePagingBean pb = new SimplePagingBean();
        pb.fetchFirst(30);
        pb.getSqlClause().registerOrderBy("aaa", true, new MockColumnInfo());
        ResultBeanBuilder<String> tgt = createTarget();

        // ## Act ##
        ListResultBean<String> actualList = tgt.buildEmptyListResultBean(pb);

        // ## Assert ##
        assertEquals(0, actualList.size());
        assertEquals(0, actualList.getSelectedList().size());
        assertEquals("dummy", actualList.getTableDbName());
        assertTrue(actualList.getOrderByClause().isSameAsFirstElementColumnName("aaa"));
    }

    protected ResultBeanBuilder<String> createTarget() {
        return new ResultBeanBuilder<String>("dummy");
    }
}
