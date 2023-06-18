import ecs100.*;

import java.awt.*;
import java.util.ArrayList;

public class Graphics
{
    public static final Integer xRes = 256;
    public static final Integer yRes = 256;
    public static final float fovX = 60;
    public static float fovY;
    public static float aspectRatio;
    public static float yaw = 0;
    public static float pitch = 90;
    public static Vector3 camPos = new Vector3(0.0f,6.0f,10.f);

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
        //yaw++;
        //pitch++;
        camPos.z--;
        UI.println(camPos.z);
        UI.clearGraphics();
        UI.drawRect(0.0,0.0,xRes,yRes);

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



                var angle = new Vector2(pitch,yaw);
                //calculate angle vector for ray in normal form

                var directionVector = Vector3.rayFromAngle(angle.x,angle.y);
                var result = fireRay(directionVector,camPos);

                if (result == null)
                    continue;

                float r,g,b;
                r = result.normals[0].x * 0.5f;
                r += 0.5;
                g = result.normals[0].y * 0.5f;
                g += 0.5;
                b = result.normals[0].z * 0.5f;
                b += 0.5;

                Color col = new Color(r,g,b);

                UI.setColor(col);
                UI.fillRect(x,y,1,1);
            }
        }
        UI.println("end render");
    }
    public static rayHit fireRay(Vector3 angleVector,Vector3 pos)
    {
        Mesh m = geometry.get(0);

        for (int i = 0; i < m.FaceVerticesIndex.size()-3;i++)
        {
            Integer vIndex0 = m.FaceVerticesIndex.get(i);
            Integer nIndex0 = m.FaceNormalIndex.get(i);
            Integer uvIndex0 = m.FaceUvIndex.get(i);

            i++;
            Integer uvIndex1 = m.FaceUvIndex.get(i);
            Integer nIndex1 = m.FaceNormalIndex.get(i);
            Integer vIndex1 = m.FaceVerticesIndex.get(i);

            i++;
            Integer vIndex2 = m.FaceVerticesIndex.get(i);
            Integer nIndex2 = m.FaceNormalIndex.get(i);
            Integer uvIndex2 = m.FaceUvIndex.get(i);

            Vector3[] triNorms = {m.Normals.get(nIndex0),m.Normals.get(nIndex1),m.Normals.get(nIndex2)};
            Vector3[] triVerts = {m.Vertices.get(vIndex0),m.Vertices.get(vIndex1),m.Vertices.get(vIndex2)};
            Vector2[] triUv = {m.UV.get(uvIndex0),m.UV.get(uvIndex1),m.UV.get(uvIndex2)};

            var result = GraphicsMath.rayIntersectsTri(angleVector,pos,triVerts);
            if (result == null)
                continue;

            var out = new rayHit();
            out.normals = triNorms;
            out.uvs = triUv;
            out.vertices = triVerts;
            out.hitPosition = result;
            return out;
        }
        return null;
    }
}
