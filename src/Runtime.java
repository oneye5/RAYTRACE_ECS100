import javax.swing.*;

public class Runtime
{
    static void Start()
    {
        Graphics.init();
    }
    static void Update()
    {
        Graphics.render();
        try {
            Thread.sleep((long) 1000.0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }




    static boolean runProgram = false;
    public static void startRuntime()
    {
        runProgram = true;
        Start();
        while(runProgram)
        {
            Update();
        }
    }
    public static void stopRuntime()
    {
        runProgram = false;
    }
    private static void run()
    {

    }
}
