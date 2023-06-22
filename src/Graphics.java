import ecs100.*;

import java.awt.*;
import java.util.ArrayList;

public class Graphics
{
    public static Integer xRes = 200;
    public static Integer yRes = 200;
    public static float fovX = 60;
    public static float fovY;
    public static float aspectRatio;
    public static float yaw = 0.0f;
    public static float pitch = -5.0f;
    public static Vector3 camPos = new Vector3(-0.0f,5.0f,-15.f);
    static Scene scene;
    public static boolean reset = false;
    public static class Settings
    {
        public static final int samplesPerPixel = 1;
        public static final int maxBounces = 15;
        public static boolean accumulation = true;
    }
    //region init
    public static void init()
    {
        UI.println("init graphics");
        UI.println("loading scene");

        initScene();
        AccumulationData = new ArrayList<ArrayList<Vector3>>();
        aspectRatio = xRes/yRes;
        fovY = fovX * aspectRatio;
    }
    public static void initScene()
    {
        scene = new Scene();
        scene.geometry = new ArrayList<>();
        scene.geometry.add(FileLoader.loadOBJ());

        scene.pLights = new ArrayList<>();

        pointLight pLight = new pointLight();
        pLight.col = new Vector3(1.0f,1.0f,1.0f);
        pLight.lumens = 100.0f;
        pLight.pos = new Vector3(0.0f,10.0f,-10.0f);
        scene.pLights.add(pLight);


        scene.materials = new ArrayList<>();
        Material material = new Material();
        material.albedo = Color.white;
        material.metalic = 0.5f;
        material.smoothness = 0.5f;
        scene.materials.add(material);

        scene.skyColor = new Vector3(0.1f,0.1f,0.1f);
    }
    //endregion
    //region screen iteration
    public static void render()
    {
        if (reset)
        {
            AccumulationData = new ArrayList<>();
            reset = false;
            UI.clearGraphics();
        }
        UI.println("pos " + camPos.x + ", " + camPos.y + ", " + camPos.z + " ANGLE " + pitch + ", " + yaw);
        var testRay = Vector3.rayFromAngle(pitch,yaw);
        UI.println("testray " + testRay.x + ", " + testRay.y + ", " + testRay.z);

        UI.drawRect(0.0,0.0,xRes,yRes);
        for(Integer y = 0; y < yRes;y++)
        {
            for(Integer x = 0; x < xRes; x++)
            {
              perPixel(x,y);
            }
        }
        UI.println("end render");
        UI.println(pitch + " " + yaw);
    }
    //endregion
    static ArrayList<ArrayList<Vector3>> AccumulationData = new ArrayList<ArrayList<Vector3>>();
    public static void perPixel(int x,int y)
    {
        if(reset == true)
            return;

        if(x == 0) //clear line
        {
            UI.setColor(Color.green);
            UI.fillRect(x,yRes-y,xRes,1);
        }
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

        int count = 0;
        Vector3 out = new Vector3(0.0f,0.0f,0.0f);
        for(int i = 0; i < Settings.samplesPerPixel; i++)
        {
            currentDepth = 0;
            var primaryRay = calculatePrimary(directionVector, camPos);
            primaryRay.color.x = GraphicsMath.clamp(primaryRay.color.x, 0.0f, 1.0f);
            primaryRay.color.y = GraphicsMath.clamp(primaryRay.color.y, 0.0f, 1.0f);
            primaryRay.color.z = GraphicsMath.clamp(primaryRay.color.z, 0.0f, 1.0f);
            out = out.add(primaryRay.color);
            count++;
        }
        out = out.divide((float)count);

        out.x = GraphicsMath.clamp(out.x,0.0f,1.0f);
        out.y = GraphicsMath.clamp(out.y,0.0f,1.0f);
        out.z = GraphicsMath.clamp(out.z,0.0f,1.0f);

        //region accumulation
        if(Settings.accumulation)//display average color
        {
            //get pixel number
            int pixelNo = 0;
            pixelNo += xRes * y;
            pixelNo += x;

            if(pixelNo == 0)
            {
                //initialize pass on first render
                AccumulationData.add(new ArrayList<>());
                for (int yy = 0; yy < yRes; yy++)
                {
                    for (int xx = 0; xx < xRes; xx++)
                    {
                        AccumulationData.get(AccumulationData.size()-1).add(new Vector3(0.0f,0.0f,0.0f));
                    }
                }
            }

            //add pixel color to thingy

            AccumulationData.get(AccumulationData.size()-1).set(pixelNo,out);

            //find average of all pixels
            out = new Vector3(0.0f,0.0f,0.0f);
            for(int i = 0; i < AccumulationData.size();i++)
            {
                out = out.add(AccumulationData.get(i).get(pixelNo));
            }
            out = out.divide((float)AccumulationData.size());

            //clamp value
            out.x = GraphicsMath.clamp(out.x,0.0f,1.0f);
            out.y = GraphicsMath.clamp(out.y,0.0f,1.0f);
            out.z = GraphicsMath.clamp(out.z,0.0f,1.0f);
        }
        //endregion
        UI.setColor(new Color(out.x,out.y,out.z));
        UI.fillRect(x,yRes-y,1,1);
    }
    public static int currentDepth = 0;
    public static rayHit calculatePrimary(Vector3 angle, Vector3 pos)
    {
        if (currentDepth > Settings.maxBounces)
        {
            var out = new rayHit();
            out.hit = false;
            out.color = scene.skyColor;
            out.hitPosition = pos;
            return out;
        }
        currentDepth++;
        var primaryRay = fireRay(angle,pos);
        if (primaryRay.hit != true) //noHit
        {
            var out = new rayHit();
            out.hit = false;
            out.color = scene.skyColor;
            out.hitPosition = pos;
            return out;
        }
        //PRIMARY RAY HIT, NOW SEND SECCONDARY RAYS
        Material primaryMat = scene.materials.get(scene.geometry.get(primaryRay.meshIndex).materialIndex);
        Vector3 adjustedPos = primaryRay.hitPosition;
        adjustedPos = adjustedPos.add(primaryRay.normals[0].multiply(0.001f));



        //shadow rays ==================================================================================
        ArrayList<rayHit> shadowRays = new ArrayList<>();
        for(int i = 0; i < scene.pLights.size(); i++)
        {
            var light = scene.pLights.get(i);
            var dir = light.pos.subtract(primaryRay.hitPosition);

            var shadowRay = fireRay(dir,adjustedPos);
            shadowRays.add(shadowRay);
        }

        Vector3 rgb = new Vector3(0.0f,0.0f,0.0f);
        for (int i = 0; i < shadowRays.size();i++)
        {
            var ray = shadowRays.get(i);
            var light = scene.pLights.get(i);
            if(ray.hit) //hit surface, IS IN SHADOW
            {
               //calculate lighting in by surface
                var hit = calculatePrimary(ray.originalVector,ray.sourcePos);
                pointLight fakeLight = new pointLight();
                fakeLight.col = hit.color;
                fakeLight.lumens = 1;
                fakeLight.pos = hit.hitPosition;

                rgb = rgb.add(hit.color);

            }
            else //in direct light
            {
                var lightReflect = GraphicsMath.inLight(primaryRay,primaryMat,light,ray.originalVector);
                rgb = rgb.add(lightReflect);
            }
        }

        //reflection ray ==============================================================================================

        var reflectDir = GraphicsMath.reflectionDir(angle,primaryRay.normals[0],primaryMat.smoothness);
        var reflectRay = calculatePrimary(reflectDir,adjustedPos);
        rgb = rgb.add(reflectRay.color);

        // end ====================================================================================================
        var out = primaryRay;
        out.color = rgb;
        return out;
    }



    public static rayHit fireRay(Vector3 angleVector,Vector3 pos)
    {//primary ray on hit:
        //spawns shadow ray towards lights, reflection ray bouncey bounce
        rayHit closestRay = null;
        float shortestDist = -1; //returns the closest hit, if exists
        for (int meshIndex = 0; meshIndex < scene.geometry.size();meshIndex++)
        {
            Mesh m = scene.geometry.get(meshIndex);
            for (int i = 0; i < m.FaceVerticesIndex.size() - 3; i++) {
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

                Vector3[] triNorms = {m.Normals.get(nIndex0), m.Normals.get(nIndex1), m.Normals.get(nIndex2)};
                Vector3[] triVerts = {m.Vertices.get(vIndex0), m.Vertices.get(vIndex1), m.Vertices.get(vIndex2)};
                Vector2[] triUv = {m.UV.get(uvIndex0), m.UV.get(uvIndex1), m.UV.get(uvIndex2)};

                var result = GraphicsMath.rayIntersectsTri(pos, angleVector, triVerts);
                if (result == null)
                    continue;

                var out = new rayHit();
                out.normals = triNorms;
                out.uvs = triUv;
                out.vertices = triVerts;
                out.hitPosition = result;
                out.originalVector = angleVector;
                out.hit = true;

                float dist = GraphicsMath.distance(out.hitPosition, camPos); //maybe there is a better way, sqrt is expensssiiive
                if (shortestDist == -1) {
                    shortestDist = dist;
                    closestRay = out;
                } else {
                    if (shortestDist > dist) {
                        shortestDist = dist;
                        closestRay = out;
                    }
                }
            }
        }


        if (closestRay != null) //no hits exist
        {
            return closestRay;
        }


        var out = new rayHit();
        out.hit = false;
        out.originalVector = angleVector;
        return out;
    }
}

