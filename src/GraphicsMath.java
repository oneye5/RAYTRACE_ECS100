import java.awt.*;
import java.util.ArrayList;

class Scene
{
    ArrayList<pointLight> pLights;
    dirLight dLight;
    ArrayList<Mesh> geometry;
    ArrayList<Material> materials;
    Vector3 skyColor;
}

class dirLight
{
    Vector3 direction;
    float lumens;
    Color col;
}
class pointLight
{
    Vector3 pos;
    Vector3 col;
    float lumens;
    float radius;
}
class Material
{
    float smoothness;
    float metalic;
    Vector3 albedo;
    //addlater, opacity, normals,emission
}
class Vector3
    {
        public float x;
        public float y;
        public float z;
        public static Vector3 subtract(Vector3 x,Vector3 v)
        {
            Vector3 out = new Vector3(0.0f,0.0f,0.0f);
            out.x = x.x - v.x;
            out.y = x.y - v.y;
            out.z = x.z - v.z;
            return out;
        }
        public static Vector3 add(Vector3 x,Vector3 v)
        {
            Vector3 out = new Vector3(0.0f,0.0f,0.0f);
            out.x = x.x + v.x;
            out.y = x.y + v.y;
            out.z = x.z + v.z;
            return out;
        }

        public Vector3(Float X, Float Y,Float Z)
        {
            x = X;
            y = Y;
            z = Z;
        }
        public Vector3(String X,String Y,String Z)
        {
            x = Float.valueOf(X);
            y = Float.valueOf(Y);
            z = Float.valueOf(Z);
        }
        public static String toStr(Vector3 in)
        {
            String out = "Vector3(x: ";
            out += in.x;
            out+= " y: ";
            out+= in.y;
            out += " z: ";
            out += in.z + " )";
            return out;
        }
        public Vector3 add(Vector3 v)
        {
            Vector3 out = new Vector3(0.0f,0.0f,0.0f);
            out.x = x + v.x;
            out.y = y + v.y;
            out.z = z + v.z;
            return out;
        }
        public Vector3 subtract(Vector3 v)
        {
            Vector3 out = new Vector3(0.0f,0.0f,0.0f);
            out.x = x - v.x;
            out.y = y - v.y;
            out.z = z - v.z;
            return out;
        }
        public Vector3 multiply(Vector3 v)
        {
            Vector3 out = new Vector3(0.0f,0.0f,0.0f);
            out.x = x * v.x;
            out.y = y * v.y;
            out.z = z * v.z;
            return out;
        }
        public Vector3 multiply(Float n)
        {
            Vector3 out = new Vector3(0.0f,0.0f,0.0f);
            out.x = x * n;
            out.y = y * n;
            out.z = z * n;
            return out;
        }
        public Vector3 divide(Float n)
        {
            Vector3 out = new Vector3(0.0f,0.0f,0.0f);
            out.x = x / n;
            out.y = y / n;
            out.z = z / n;
            return out;
        }
        public Vector3 divide(Vector3 n)
        {
            Vector3 out = new Vector3(0.0f,0.0f,0.0f);
            out.x = x / n.x;
            out.y = y / n.y;
            out.z = z / n.z;
            return out;
        }

        static Vector3 normalize(Vector3 x)
        {
            float mag = (float)Math.sqrt((x.x * x.x)+(x.y * x.y) +( x.z * x.z));
            return x.divide(mag);
        }




        static Vector3 rayFromAngleRadians(float pitch, float yaw)
        {
            Float x;
            Float y;
            Float z;
            x = (float) (Math.sin(yaw) * (Math.cos(pitch)));
            y = (float) Math.sin(pitch);
            z = (float) (Math.cos(yaw) * (Math.cos(pitch)));
            return new Vector3(x, y, z);
        }
        static Vector3 rayFromAngle(Float pitch, Float yaw)
        {
            Float x;
            Float y;
            Float z;

            Float radPitch = (float) Math.toRadians(pitch);
            Float radYaw = (float) Math.toRadians(yaw);
            //UI.print("pitch = " + radPitch);
            //UI.print("yaw = " + radYaw);


            x = (float) (Math.sin(radYaw) * (Math.cos(radPitch)));
            y = (float) Math.sin(radPitch);
            z = (float) (Math.cos(radYaw) * (Math.cos(radPitch)));

            return new Vector3(x, y, z);
        }

        static Vector3 Subtract(Vector3 v1, Vector3 v2)
        {
            Vector3 out = new Vector3
                    (
                    v1.x-v2.x,
                    v1.y-v2.y,
                    v1.z-v2.z
                     );

            return out;
        }
        static Vector3 CrossProduct(Vector3 v1,Vector3 v2)
        {
            Vector3 v = new Vector3(0.0f,0.0f,0.0f);
            v.x = v1.y * v2.z - v1.z * v2.y;
            v.y = v1.z * v2.x - v1.x * v2.z;
            v.z = v1.x * v2.y - v1.y * v2.x;
            return v;
        }
        static float DotProduct(Vector3 a,Vector3 b)
        {
            float	out =
            a.x * (b.x) +
            a.y * (b.y) +
            a.z * (b.z);
            return out;
        }
        static Vector3 dirVectorBetween(Vector3 p1,Vector3 p2)
        {
            float magnitude = GraphicsMath.distance(p1,p2);
            return p2.subtract(p1).divide(magnitude);
        }

    }
class Vector2
{
    public float x;
    public float y;

    public Vector2(Float X, Float Y)
    {
        x = X;
        y = Y;
    }
    public Vector2(String X,String Y)
    {
        x = Float.valueOf(X);
        y = Float.valueOf(Y);
    }
    public Vector2 add(Vector2 v)
    {
        Vector2 out = new Vector2(0.0f,0.0f);
        out.x = x + v.x;
        out.y = y + v.y;
        return out;
    }
    public Vector2 subtract(Vector2 v)
    {
        Vector2 out = new Vector2(0.0f,0.0f);
        out.x = x - v.x;
        out.y = y - v.y;
        return out;
    }
    public Vector2 multiply(Vector2 v)
    {
        Vector2 out = new Vector2(0.0f,0.0f);
        out.x = x * v.x;
        out.y = y * v.y;
        return out;
    }
    public Vector2 multiply(Float n)
    {
        Vector2 out = new Vector2(0.0f,0.0f);
        out.x = x * n;
        out.y = y * n;
        return out;
    }
    public Vector2 divide(Float n)
    {
        Vector2 out = new Vector2(0.0f,0.0f);
        out.x = x / n;
        out.y = y / n;
        return out;
    }
    public Vector2 divide(Vector2 n)
    {
        Vector2 out = new Vector2(0.0f,0.0f);
        out.x = x / n.x;
        out.y = y / n.y;
        return out;
    }




    static Vector2 clipRot(Vector2 v)
    {
        Vector2 out = new Vector2(0.0f,0.0f);
        if (v.x > 90.0f)
            out.x = 90.0f;
        if (v.x < -90.0f)
            out.x = -90.0f;

        if (v.y > 180.0f)
            out.y = v.y - 360.0f;
        if (v.y < -180.0f)
            out.y= v.y + 360.0f;

        return out;
    }
    static Vector2 clipRot(float x,float y)
    {
        Vector2 out = new Vector2(0.0f,0.0f);
        if (x > 90.0f)
            out.x = 90.0f;
        if (x < -90.0f)
            out.x = -90.0f;

        if (y > 180.0f)
            out.y = y - 360.0f;
        if (y < -180.0f)
            out.y= y + 360.0f;

        return out;
    }
}
     class Mesh
    {
        public String Name;
       public ArrayList<Vector3> Vertices;
       public ArrayList<Vector3> Normals;
       public ArrayList<Vector2> UV;


       public ArrayList<Integer> FaceVerticesIndex;
        public ArrayList<Integer> FaceUvIndex;
        public ArrayList<Integer> FaceNormalIndex;
        int materialIndex;

        public Mesh()
        {
            Vertices = new ArrayList<>();
            Normals = new ArrayList<>();
            UV = new ArrayList<>();
            FaceVerticesIndex = new ArrayList<>();
            FaceUvIndex = new ArrayList<>();
            FaceNormalIndex = new ArrayList<>();
        }
    }

class rayHit
{
    Vector3[] normals;
    Vector3[] vertices;
    Vector2[] uvs;
    int meshIndex;
    Vector3 hitPosition;
    Vector3 originalVector;
    boolean hit;
    Vector3 color;
    Vector3 sourcePos;
}
class GraphicsMath {
    public static Vector3 rayIntersectsTri(Vector3 origin, Vector3 directionVector, Vector3[] tri)//returns the hit point if exists, returns null if it does not
    {
        if(origin == null)
            origin = new Vector3(0.0f,0.0f,0.0f);


        //MOLLER TRUMBORE ALGORITHM
        final double moe = 0.0000001; //margin of error
        Vector3 edge1;
        Vector3 edge2;

        Vector3 dirEdgeCrossProd;
        Vector3 posDif;
        Vector3 posDifCross;

        double dotProd, dotProd2, u, v;

        edge1 = Vector3.Subtract(tri[1], tri[0]);// edge1.sub(vertex1, vertex0);
        edge2 = Vector3.Subtract(tri[2], tri[0]);

        dirEdgeCrossProd = Vector3.CrossProduct(directionVector, edge2);
        dotProd = Vector3.DotProduct(edge1, dirEdgeCrossProd);

        if (dotProd > -moe && dotProd < moe) {
            return null; //ray parallel to tri
        }

        dotProd2 = 1.0 / dotProd;
        posDif = Vector3.Subtract(origin, tri[0]);
        u = dotProd2 * (Vector3.DotProduct(posDif, dirEdgeCrossProd));

        if (u < 0.0 || u > 1.0) {
            return null;
        }

        posDifCross = Vector3.CrossProduct(posDif, edge1);
        v = dotProd2 * Vector3.DotProduct(directionVector, posDifCross);

        if (v < 0.0 || u + v > 1.0) {
            return null;
        }

        //FIND INTERSECTION POINT
        Vector3 out;
        double t = dotProd2 * Vector3.DotProduct(edge2, posDifCross);
        if (t > moe) // ray intersection
        {
            out = directionVector.multiply((float) t).add(origin);//.scaleAdd(t, rayVector, rayOrigin);
            return out;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return null;
        }
    }


    public static float distance(Vector3 a, Vector3 b) {
        var x = a.subtract(b);
        return (float) Math.sqrt((x.x * x.x) + (x.y * x.y) + (x.z * x.z));
    }

    public static Vector3 reflectionDir(Vector3 primaryRayDir, Vector3 surfaceNormal, float smoothness) {
        float dotProd = Vector3.DotProduct(primaryRayDir, surfaceNormal);
        Vector3 outDir = new Vector3(0.0f, 0.0f, 0.0f);

        outDir.x = primaryRayDir.x - 2 * dotProd * surfaceNormal.x;
        outDir.y = primaryRayDir.y - 2 * dotProd * surfaceNormal.y;
        outDir.z = primaryRayDir.z - 2 * dotProd * surfaceNormal.z;

        Vector3 rand = new Vector3(0.0f,0.0f,0.0f);
        rand.x = (float)Math.random() - 0.5f;
        rand.y = (float)Math.random() - 0.5f;
        rand.z = (float)Math.random() - 0.5f;

        if(Vector3.DotProduct(outDir,rand) < 0.0f)
            rand = rand.multiply(-1.0f);

        return outDir.multiply(smoothness).add( rand.multiply(1.0f - smoothness));
    }
    public static float clamp(float in,float min, float max)
    {
        float out = in;
        if(in > max)
            out = max;
        if(in < min)
            out = min;

        return out;
    }
    public  static float toFloat(int value)
    {
        return (float)value/255.0f;
    }
    static Vector3 inLight(rayHit primaryRay, Material hitMat, pointLight pLight, Vector3 dirFromPrimary)
    {
        final float specularHardness = 0.15f; //final value

        float distance = GraphicsMath.distance(primaryRay.hitPosition,pLight.pos);
        float inverseSquareLaw = pLight.lumens /(distance*distance);

        //calculate specular
        float normLightDotProd = Vector3.DotProduct(dirFromPrimary,primaryRay.normals[0]);
        if(normLightDotProd > 1)
            normLightDotProd = 1.0f;
        if (normLightDotProd < 0.0f)
            normLightDotProd = 0.0f;

        //diffuse light
        float lDifR,lDifG,lDifB;

        lDifR = normLightDotProd * pLight.col.x * hitMat.smoothness * inverseSquareLaw;
        lDifG = normLightDotProd * pLight.col.y * hitMat.smoothness * inverseSquareLaw;
        lDifB = normLightDotProd * pLight.col.z * hitMat.smoothness * inverseSquareLaw;

        //get half vector
        Vector3 halfDir = Vector3.normalize(primaryRay.originalVector.add(dirFromPrimary));

        float normHalfDotProd = Vector3.DotProduct(primaryRay.normals[0],halfDir);
        if(normHalfDotProd > 1)
            normHalfDotProd = 1.0f;
        if (normHalfDotProd < 0.0f)
            normHalfDotProd = 0.0f;

        float specularIntensity = (float)Math.pow(normHalfDotProd,specularHardness);

        float outR,outG,outB;
        //add all
        outR = (lDifR * specularIntensity) + (lDifR * hitMat.albedo.x);
        outG = (lDifG * specularIntensity) + (lDifG * hitMat.albedo.y);
        outB = (lDifB * specularIntensity) + (lDifB * hitMat.albedo.z);
        //System.out.println("r " + outR + " g " + outG + " b " + outB);
        return new Vector3(outR,outG,outB);
    }

    static Vector3 calculateReceivedLight(Vector3 receivingPos,Vector3 receivingNorm,Material receivingMaterial,Vector3 senderPos,
                                          Vector3 senderColor,Vector3 senderNormal,float specularHardness,Vector3 viewerPos,Material senderMaterial)
    {
        float dist = GraphicsMath.distance(receivingPos,senderPos);
        float distanceMultiplier = 1.0f/(dist*dist);

        Vector3 dirToSender = Vector3.normalize(receivingPos.subtract(senderPos));

        float receiveNorm_dirDot = Vector3.DotProduct(dirToSender,receivingNorm);
        float sendNorm_dirDot = Vector3.DotProduct(dirToSender.multiply(-1.0f),senderNormal);

        //calculate sending light
        return null;

    }

}
/*
 static Vector3 calculateReceivedLight(Vector3 receivingPos,Vector3 receivingNorm,Material receivingMaterial,Vector3 senderPos,
                                          Vector3 senderColor,Vector3 senderNormal,float specularHardness,Vector3 viewerPos,Material senderMaterial)
    {
        // Calculate light direction and distance
        Vector3 lightDirection = Vector3.normalize(Vector3.subtract(senderPos, receivingPos));
        if(senderNormal == null)
            senderNormal = lightDirection.multiply(-1.0f);

        float distance = GraphicsMath.distance(senderPos, receivingPos);

        // Calculate diffuse component
        float NdotL = Math.max(Vector3.DotProduct(receivingNorm, lightDirection), 0.0f);
        float senderNormalDot = Vector3.DotProduct(lightDirection.multiply(-1.0f),senderNormal);

        Vector3 diffuseColor = receivingMaterial.albedo.multiply(senderColor).multiply(NdotL).multiply(GraphicsMath.clamp(senderNormalDot,0.0f,1.0f)).divide(distance * distance);

        if(senderMaterial != null)
        {
            diffuseColor = diffuseColor.multiply(senderMaterial.albedo).multiply(senderMaterial.smoothness);
        }

        // Calculate specular component
        Vector3 viewDirection = Vector3.normalize(Vector3.subtract(viewerPos, receivingPos));
        Vector3 halfwayVector = Vector3.normalize(Vector3.add(lightDirection, viewDirection));
        float NdotH = Math.max(Vector3.DotProduct(receivingNorm, halfwayVector), 0.0f);
        float specularIntensity = (float) Math.pow(NdotH, specularHardness);
        Vector3 specularColor = senderColor.multiply(specularIntensity).multiply(receivingMaterial.smoothness).divide(distance * distance);


        // Apply material properties

        Vector3 surfaceColor = diffuseColor.add(specularColor).multiply(receivingMaterial.albedo);

        return surfaceColor;
    }
 */