package com.longsight;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

/**
 * Test program that helps you test AWS Lambda java with cold starts.
 * After 4 runs of this lambda, it will call System.exit to make the next call Cold.
 * We have noticed that calles like Region.getRegion( US_WEST_2 ) takes a long time.
 * (This is not specific to one region, all regions take 1.5s, due to metadata loading)
 *
 * config handler to: com.longsight.Hello::myHandler
 * @Author Peter Dietz (peter@longsight.com)
 */
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

            StopWatch stopWatch = new StopWatch("Performance of com.amazonaws.regions.Region.getRegion");

            // If first (metadata not loaded): 1729ms, otherwise (when region metadata loaded): 0ms
            stopWatch.start("Region.getRegion(Regions.US_EAST_1)");
            Region.getRegion(Regions.US_EAST_1);
            stopWatch.stop();

            // 1ms
            stopWatch.start("Regions regions = Regions.fromName(us-west-2);");
            Regions regions = Regions.fromName("us-west-2");
            stopWatch.stop();

            // If first (metadata not loaded): 1739ms  , otherwise (when region metadata loaded): 0ms
            stopWatch.start("Region.getRegion(regions);");
            Region.getRegion(regions);
            stopWatch.stop();


            logger.log(stopWatch.prettyPrint());

            String output = (lambdaUsed == 0)?"ColdStart":"WarmStart";
            output += " (" + randomString +") took:" + stopWatch.getTotalTimeMillis() + " ms to get region";
            return output;
        } finally {
            log.info("lambdaHello instance(" + randomString +") usage numbers: " + lambdaUsed);

            if(lambdaUsed > 3) {
                log.info("lambdaHello Lambda used 4 times, shutting down so next call will be cold start");
                System.exit(0);
            }
            lambdaUsed++;
        }
    }
}