import ecs100.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Graphics
{
    public static Integer xRes = 1280;
    public static Integer yRes = 720;
    public static float fovX = 60;
    public static float fovY;
    public static float aspectRatio;
    public static float yaw = 185.0f;
    public static float pitch = -5.0f;
    public static Vector3 camPos = new Vector3(0.0f, 2.0f, 10.0f);
    static Scene scene;
    public static boolean reset = false;
    public static class Settings
    {
        public static int samplesPerPixel = 1;
        public static int maxBounces = 10;
        public static boolean accumulation = true;
        public static float specularHardness = 1.0f;

        public static int shadowSamples = 3;
    }
    //region init
    public static void initAspectRatio()
    {
        aspectRatio = (float)yRes/(float)xRes;
        fovY = fovX * aspectRatio;
        UI.println("aspect " + aspectRatio + " fov x " + fovX + " fov y " + fovY);
    }
    public static void init()
    {
        UI.println("init graphics");
        UI.println("loading scene");

        initScene();
        AccumulationData = new ArrayList<ArrayList<Vector3>>();
        initAspectRatio();
    }
    public static void initScene() //pos 0.0, 2.0, 1.0 ANGLE -5.0, 185.0
    {
        scene = new Scene();
        scene.geometry = new ArrayList<>();

        scene.geometry.add(FileLoader.loadOBJ());

        var m = FileLoader.loadOBJ();
        m.materialIndex = 1;
        scene.geometry.add(m);


        scene.pLights = new ArrayList<>();

        pointLight pLight = new pointLight();
        pLight.col = new Vector3(1.0f,1.0f,1.0f);
        pLight.lumens = 1.0f;
        pLight.pos = new Vector3(20.0f,30.0f,30.0f);
        pLight.radius = 8.0f;
        scene.pLights.add(pLight);

        scene.materials = new ArrayList<>();
        Material material = new Material();
        material.albedo = new Vector3(1.0f,1.0f,1.0f);
        material.metalic = 0.0f;
        material.smoothness = 0.5f;
        scene.materials.add(material);

        material = new Material();
        material.albedo = new Vector3(1.0f,0.0f,0.0f);
        material.metalic = 1.0f;
        material.smoothness = 0.7f;
        scene.materials.add(material);

        material = new Material();
        material.albedo = new Vector3(1.0f,1.0f,1.0f);
        material.metalic = 0.0f;
        material.smoothness = 0.95f;
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
            initAspectRatio();
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

       //randomness for aa
        rPitch += fovPerStepX * Math.random()-0.5;
        rYaw += fovPerStepY * Math.random()-0.5;

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
        //region setup
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

        Vector3 outColor = new Vector3(0.0f,0.0f,0.0f);
        //endregion
        //shadow rays ==================================================================================
        for(int i = 0; i < scene.pLights.size(); i++)
        {
            for(int sample = 0; sample < Settings.shadowSamples; sample++)
            {

                var light = scene.pLights.get(i);

                Vector3 randomOffset = Vector3.rayFromAngle((float)Math.random()*360.0f,(float)Math.random()*360.0f);
                randomOffset = randomOffset.multiply(light.radius);

                var dir = Vector3.normalize(light.pos.add(randomOffset).subtract(primaryRay.hitPosition));
                var shadowRay = fireRay(dir, adjustedPos);

                //calculate shadow ray stuffs
                if (shadowRay.hit) {
                    continue;
                } else {
                    //calculate direct light

                    var dot = Vector3.DotProduct(primaryRay.normals[0], dir);
                    var distanceMulti = GraphicsMath.distance(primaryRay.hitPosition, light.pos);
                    distanceMulti = 1.0f / distanceMulti * distanceMulti;

                    outColor =
                            outColor.add(
                                    primaryMat.albedo.multiply(light.col).multiply(light.lumens).multiply(dot).multiply(distanceMulti).divide((float)Settings.shadowSamples)
                            );
                }
            }
        }
        //reflection ray ==============================================================================================
        var reflectDir = GraphicsMath.reflectionDir(angle,primaryRay.normals[0],primaryMat.smoothness);
        var reflectRay = calculatePrimary(reflectDir,adjustedPos);
        if(reflectRay.hit)
        {
        // Material mat = scene.materials.get(scene.geometry.get(reflectRay.meshIndex).materialIndex);
           outColor = outColor.add(reflectRay.color.multiply(primaryMat.albedo).multiply(primaryMat.smoothness));
        }
        else
            outColor = outColor.add(scene.skyColor);

        // end ====================================================================================================
        var out = primaryRay;
        out.color = outColor;
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
            for (int i = 0; i < m.FaceVerticesIndex.size(); i++) {
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
                out.meshIndex = meshIndex;


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

