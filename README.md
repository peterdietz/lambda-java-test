# Testing Cold Start Performance of AWS Lambda in JVM
Lamda is a cool server-less method to run functions.
A warm java/jvm function that is simple can return in 1ms, this is A-MA-ZING
However, a cold-start for JVM can sometimes take quite a while, especially if you do a ton of init.
This is not the fault of JVM being crap, you have way too much init going on.
- Spring
- Initializing lots of services

## Build it:
`/gradlew clean build`

Create a JVM lambda with handler: `com.longsight.Hello::myHandler`

Deploy `build/distributions/hello.zip` to Lambda

## Performance Woes - com.amazonaws.regions.Region.getRegion takes 1.5s (1500ms)

Deploy this, click test, and get output of:
`"ColdStart (zrjhu) took:1749 ms to get region"`

And the logs have more details, including our StopWatch.

```
START RequestId: ee9aa55d-fcf4-11e5-8f2a-eb1e21b442fb Version: $LATEST
0(zrjhu)StopWatch 'Performance of com.amazonaws.regions.Region.getRegion': running time (millis) = 1749
-----------------------------------------
ms     %     Task name
-----------------------------------------
01749  100%  Region.getRegion(Regions.US_EAST_1)
00000  000%  Regions regions = Regions.fromName(us-west-2);
00000  000%  Region.getRegion(regions);
Apr 07, 2016 7:14:23 PM com.longsight.Hello myHandler
INFO: lambdaHello instance(zrjhu) usage numbers: 0
END RequestId: ee9aa55d-fcf4-11e5-8f2a-eb1e21b442fb
REPORT RequestId: ee9aa55d-fcf4-11e5-8f2a-eb1e21b442fb	Duration: 1829.58 ms	Billed Duration: 1900 ms
```

## Question for AWS Engineers.
Why does Region.getRegion(Regions.US_EAST_1) take 1.5s+ ?

Actually I don't want to know why it takes 1.5s, I just want a way for it to complete in 0ms from cold start.