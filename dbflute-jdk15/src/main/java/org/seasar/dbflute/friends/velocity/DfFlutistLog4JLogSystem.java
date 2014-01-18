package org.seasar.dbflute.friends.velocity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.SimpleLog4JLogSystem;
import org.seasar.dbflute.friends.log4j.DfFlutistRollingFileAppender;
import org.seasar.dbflute.util.DfTypeUtil;

/**
 * The log system using Log4j for DBFlute.
 * @author jflute
 * @since 0.9.5.1 (2009/06/23 Tuesday)
 */
public class DfFlutistLog4JLogSystem extends SimpleLog4JLogSystem {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Log instance for DBFlute log. */
    private static final Log _log = LogFactory.getLog(DfFlutistLog4JLogSystem.class);

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfFlutistLog4JLogSystem() {
    }

    // ===================================================================================
    //                                                                 Initialize Override
    //                                                                 ===================
    public void init(RuntimeServices rs) {
        final String logfile = "./log/velocity.log";
        try {
            logger = Logger.getLogger(getClass().getName());
            logger.setAdditivity(false);
            logger.setLevel(Level.DEBUG);

            final DfFlutistRollingFileAppender appender = createOriginalRollingFileAppender(logfile);
            appender.setMaxBackupIndex(2);
            appender.setMaximumFileSize(100000);
            logger.addAppender(appender);

            logVelocityMessage(0, ""); // as begin mark.
            logVelocityMessage(0, DfTypeUtil.toClassTitle(this) + " initialized using logfile '" + logfile + "'");
        } catch (Exception e) {
            _log.warn("PANIC : error configuring " + DfTypeUtil.toClassTitle(this) + " : ", e);
        }
    }

    protected DfFlutistRollingFileAppender createOriginalRollingFileAppender(String logfile) throws Exception {
        return new DfFlutistRollingFileAppender(new PatternLayout("%d - %m%n"), logfile, true);
    }
}
