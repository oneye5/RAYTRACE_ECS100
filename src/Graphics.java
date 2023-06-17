import ecs100.*;

import java.util.ArrayList;

public class Graphics
{
    public static final Integer xRes = 256;
    public static final Integer yRes = 256;
    public static final float fovX = 90;
    public static float fovY;
    public static float aspectRatio;
    public static float yaw;
    public static float pitch;
    public static Vector3 camPos = new Vector3(0.0f,0.0f,0.f);

    public static ArrayList<Mesh> geometry = new ArrayList<>();

    public static void init()
    {
        UI.println("init graphics");
        UI.println("loading mesh");
        geometry.add(FileLoader.loadOBJ());

        aspectRatio = xRes/yRes;

        fovY = fovX * aspectRatio;
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
                fireRay(directionVector,camPos);
                //calculate angle vector for ray in normal form

            }
        }
    }
    public static void fireRay(Vector3 angleVector,Vector3 pos)
    {
        Mesh m = geometry.get(0);

        for (int i = 0; i < m.FaceVerticesIndex.size();i += 3)
        {
            Integer vIndex0 = m.FaceVerticesIndex.get(i);
            Integer vIndex1 = m.FaceVerticesIndex.get(i+1);
            Integer vIndex2 = m.FaceVerticesIndex.get(i+2);

            Integer nIndex0 = m.FaceNormalIndex.get(i);
            Integer nIndex1 = m.FaceNormalIndex.get(i+1);
            Integer nIndex2 = m.FaceNormalIndex.get(i+2);

            Integer tIndex0 = m.FaceTextureIndex.get(i);
            Integer tIndex1 = m.FaceTextureIndex.get(i+1);
            Integer tIndex2 = m.FaceTextureIndex.get(i+2);



            Vector3[] triNorms = {m.Normals.get(nIndex0),m.Normals.get(nIndex1),m.Normals.get(nIndex2)};
            Vector3[] triVerts = {m.Vertices.get(vIndex0),m.Vertices.get(vIndex1),m.Vertices.get(vIndex2)};

        }
    }
}
