import ecs100.*;
public class Graphics
{
    public static final Integer xRes = 256;
    public static final Integer yRes = 256;
    public static final float fovX = 90;
    public static float fovY;
    public static float aspectRatio;
    public static float yaw;
    public static float pitch;

    public static void init()
    {
        UI.println("init graphics");
        UI.println("loading mesh");
        FileLoader.loadOBJ();

        aspectRatio = xRes/yRes;

    }
    public static void render()
    {
        yaw++;
        pitch++;


        var v = Vector3.rayFromAngle(pitch,yaw);



        for(Integer y = 0; y < yRes;y++)
        {
            for(Integer x = 0; x < xRes; x++)
            {
                float pitch;
                float yaw;
                float xMidPoint = xRes/2.0f;
                float yMidPoint = yRes/2.0f;

                float fovPerStepX = fovX/xRes;
                float fovPerStepY = fovY/yRes;

                pitch = y * fovPerStepY;
                yaw = x * fovPerStepX;

                pitch -= fovY/2.0;
                yaw -= fovX/2.0;

                var angle = Vector2.clipRot(pitch,yaw);

                var directionVector = Vector3.rayFromAngle(angle.x,angle.y);
                //calculate angle vector for ray in normal form

            }
        }
    }
    public static void fireRay(Vector3 angleVector,Vector3 pos)
    {

    }
}
