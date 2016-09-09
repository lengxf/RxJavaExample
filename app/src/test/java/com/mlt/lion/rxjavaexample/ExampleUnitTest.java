package com.mlt.lion.rxjavaexample;

import org.junit.Test;

import java.io.Console;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    public void StartBackgroundWork() {
        Console.WriteLine("Shows use of Start to start on a background thread:");
        var o = Observable.Start(() =>
                {
                        //This starts on a background thread.
                        Console.WriteLine("From background thread. Does not block main thread.");
        Console.WriteLine("Calculating...");
        Thread.Sleep(3000);
        Console.WriteLine("Background work completed.");
        }).Finally(() => Console.WriteLine("Main thread completed."));
        Console.WriteLine("\r\n\t In Main Thread...\r\n");
        o.Wait();   // Wait for completion of background operation.
    }


}