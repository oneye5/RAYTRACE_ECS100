import ecs100.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Graphics
{
    public static Integer xRes = 50;
    public static Integer yRes = 50;
    public static float fovX = 60;
    public static float fovY;
    public static float aspectRatio;
    public static float yaw = 0;
    public static float pitch = 0;
    public static Vector3 camPos = new Vector3(0.0f,0.0f,10.f);

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
        UI.println("pos " + camPos.x + ", " + camPos.y + ", " + camPos.z + " ANGLE " + pitch + ", " + yaw);
        var testRay = Vector3.rayFromAngle(pitch,yaw);
        UI.println("testray " + testRay.x + ", " + testRay.y + ", " + testRay.z);

        UI.drawRect(0.0,0.0,xRes,yRes);
        for(Integer y = 0; y < yRes;y++)
        {
            for(Integer x = 0; x < xRes; x++)
            {
                //clear pixel
                UI.setColor(Color.gray);
                UI.fillRect(x,yRes-y,1,1);
                float rPitch;
                float rYaw;

                float fovPerStepX = fovX/xRes;
                float fovPerStepY = fovY/yRes;

                rPitch = y * fovPerStepY;
                rYaw = x * fovPerStepX;

                rPitch -= fovY/2.0;
                rYaw -= fovX/2.0;

                rPitch += pitch;
                rYaw += yaw;

                //calculate angle vector for ray in normal form
                var directionVector = Vector3.rayFromAngle(rPitch,rYaw);
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
                UI.fillRect(x,yRes-y,1,1);
            }
        }
        UI.println("end render");
        UI.println(pitch + " " + yaw);
    }
    public static rayHit fireRay(Vector3 angleVector,Vector3 pos)
    {
        int closest = 0;
        float shortestDist = -1; //returns the closest hit, if exists

        Mesh m = geometry.get(0);
        ArrayList<rayHit> hits = new ArrayList<>();
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

            var result = GraphicsMath.rayIntersectsTri(pos,angleVector,triVerts);
            if (result == null)
                continue;

            var out = new rayHit();
            out.normals = triNorms;
            out.uvs = triUv;
            out.vertices = triVerts;
            out.hitPosition = result;
            hits.add(out);

            float dist = GraphicsMath.distance(out.hitPosition,camPos); //maybe there is a better way, sqrt is expensssiiive
            if(shortestDist == -1)
            {
                shortestDist = dist;
            }
            else
            {
                if(shortestDist > dist)
                {
                    shortestDist = dist;
                    closest = hits.size()-1;
                }
            }
        }
        if(hits.size() == 0)
        return null; //no hits exist

        return hits.get(closest);
    }
}
