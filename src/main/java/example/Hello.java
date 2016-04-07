package example;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

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

            StopWatch stopWatch = new StopWatch("Hello World");





            stopWatch.start("Regions regions = Regions.fromName(us-west-2);");
            Regions regions = Regions.fromName("us-west-2");
            stopWatch.stop();

            stopWatch.start("Region.getRegion(regions);");
            Region.getRegion(regions);
            stopWatch.stop();

            stopWatch.start("Region.getRegion(Regions.US_WEST_2)");
            Region.getRegion(Regions.US_WEST_2);
            stopWatch.stop();

            logger.log(stopWatch.prettyPrint());

            String output = (lambdaUsed == 0)?"ColdStart":"WarmStart";
            output += " (" + randomString +") took:" + stopWatch.getTotalTimeMillis() + " ms to get region";
            return output;
        } finally {
            log.info("lambdaHello instance(" + randomString +") usage numbers: " + lambdaUsed);

            if(lambdaUsed > 3) {
                log.info("lambdaHello Lambda used 4 times, shutting down");
                System.exit(0);
            }
            lambdaUsed++;
        }
    }
}