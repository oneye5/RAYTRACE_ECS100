import ecs100.UI;

import java.util.ArrayList;
import java.util.Vector;

class Vector3
    {
        public float x;
        public float y;
        public float z;

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







        static Vector3 rayFromAngle(Float pitch, Float yaw)
        {
            UI.println("in = " + pitch + " " + yaw);
            Float x;
            Float y;
            Float z;

            Float radPitch = (float) Math.toRadians(pitch);
            Float radYaw = (float) Math.toRadians(yaw);



            x = (float) (Math.cos(radYaw) * (Math.cos(radPitch)));
            y = (float) Math.sin(radPitch);
            z = (float) (Math.sin(radYaw) * (Math.cos(radPitch)));

            return new Vector3(x, y, z);
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
        public ArrayList<Integer> FaceTextureIndex;
        public ArrayList<Integer> FaceNormalIndex;

        public Mesh()
        {
            Vertices = new ArrayList<>();
            Normals = new ArrayList<>();
            UV = new ArrayList<>();
            FaceVerticesIndex = new ArrayList<>();
            FaceTextureIndex = new ArrayList<>();
            FaceNormalIndex = new ArrayList<>();
        }
    }


class GraphicsMath
{
    //public static Vector3 rayIntersectsTri(Vector3 origin,Vector3 directionVector,Vector3[] tri)//returns the hit point if exists, returns null if it does not
    //{

    //}
}
/*
public class MollerTrumbore {

    private static final double EPSILON = 0.0000001;

    public static boolean rayIntersectsTriangle(Point3d rayOrigin,
                                                Vector3d rayVector,
                                                Triangle inTriangle,
                                                Point3d outIntersectionPoint) {
        Point3d vertex0 = inTriangle.getVertex0();
        Point3d vertex1 = inTriangle.getVertex1();
        Point3d vertex2 = inTriangle.getVertex2();
        Vector3d edge1 = new Vector3d();
        Vector3d edge2 = new Vector3d();
        Vector3d h = new Vector3d();
        Vector3d s = new Vector3d();
        Vector3d q = new Vector3d();
        double a, f, u, v;
        edge1.sub(vertex1, vertex0);
        edge2.sub(vertex2, vertex0);
        h.cross(rayVector, edge2);
        a = edge1.dot(h);

        if (a > -EPSILON && a < EPSILON) {
            return false;    // This ray is parallel to this triangle.
        }

        f = 1.0 / a;
        s.sub(rayOrigin, vertex0);
        u = f * (s.dot(h));

        if (u < 0.0 || u > 1.0) {
            return false;
        }

        q.cross(s, edge1);
        v = f * rayVector.dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return false;
        }

        // At this stage we can compute t to find out where the intersection point is on the line.
        double t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            outIntersectionPoint.set(0.0, 0.0, 0.0);
            outIntersectionPoint.scaleAdd(t, rayVector, rayOrigin);
            return true;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return false;
        }
    }
}
 */