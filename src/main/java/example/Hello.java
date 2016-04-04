package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Hello {
    private static final Log log = LogFactory.getLog(Hello.class);

    private static int lambdaUsed = 0;
    private static String randomString = null;

    static {
        randomString = RandomStringUtils.randomAlphabetic(5);
    }

    public String myHandler(Context context) {
        try {
            LambdaLogger logger = context.getLogger();
            logger.log(lambdaUsed + "(" + randomString + ")");
            return lambdaUsed + " (" + randomString +")";
        }  finally {
            if(lambdaUsed == 0) {
                log.info("lambdaHello instance("+ randomString + ") First Use");
            }
            log.info("lambdaHello instance(" + randomString +") usage numbers: " + lambdaUsed);


            if(lambdaUsed > 3) {
                log.info("lambdaHello Lambda used 4 times, shutting down");
                System.exit(0);
            }
            lambdaUsed++;
        }
    }
}