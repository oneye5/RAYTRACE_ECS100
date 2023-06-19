import ecs100.UI;

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
    public void initUI()
    {
        UI.addButton("add x",this::addX);
        UI.addButton("sub x",this::subX);

        UI.addButton("add y",this::addY);
        UI.addButton("sub y",this::subY);

        UI.addButton("add z",this::addZ);
        UI.addButton("sub z",this::subZ);

        UI.addButton("add yaw",this::addYaw);
        UI.addButton("add pitch",this::addPitch);

        UI.addButton("add fov",this::addFov);
        UI.addButton("sub fov",this::subFov);
    }

    public void addX()
    {
        Graphics.camPos.x++;
        UI.println(Graphics.camPos.x);
    }
    public void subX()
    {
        Graphics.camPos.x--;
        UI.println(Graphics.camPos.x);
    }

    public void addY()
    {
        Graphics.camPos.y+=5;
        UI.println(Graphics.camPos.y);
    }
    public void subY()
    {
        Graphics.camPos.y-=5;
        UI.println(Graphics.camPos.y);
    }
    public void addZ()
    {
        Graphics.camPos.z+=5;
        UI.println(Graphics.camPos.z);
    }
    public void subZ()
    {
        Graphics.camPos.z-=5;
        UI.println(Graphics.camPos.z);
    }
    public void addYaw()
    {
        Graphics.yaw++;
        UI.println(Graphics.yaw);
    }
    public void addPitch()
    {
        Graphics.pitch++;
        UI.println(Graphics.pitch);
    }
    public void addFov()
    {
        Graphics.fovX++;
        UI.println(Graphics.fovX);
    }
    public void subFov()
    {
        Graphics.fovX--;
        UI.println(Graphics.fovX);
    }

}
