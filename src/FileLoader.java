import ecs100.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileLoader
{
    public static Mesh loadOBJ()
    {
        /*
        OBJ FORMAT DOCUMENTATION ---------------------------------------

        prefixes
        v - vert {v 1.0000 1.0000 -1.00000}
        vn - norm {vn 0.0 1.0 -1.0}
        o - obj name {o cube}
        f - face vertex index {f 1/2/3 1/2/3 1/2/3} vertex index/uv index/normal index
        vp - free form {vp 1 1 1}
        vt - uvs {vt 1,0}
        # - comment {#comment wooohoo}





         */
        Mesh out = new Mesh();
        String fileName = UIFileChooser.open();
        File file = new File(fileName);
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            UI.println("failed loading file");
            return null;
        }

        while(scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            UI.println(line);

            Scanner sublineScanner = new Scanner(line);
            String prefix = sublineScanner.next();

            switch(prefix)
            {
                case "v":
                {
                    String x = sublineScanner.next();
                    String y = sublineScanner.next();
                    String z = sublineScanner.next();
                    Vector3 vector = new Vector3(x, y, z);
                    out.Vertices.add(vector);
                    break;
                }
                case "vn": {
                    String x = sublineScanner.next();
                    String y = sublineScanner.next();
                    String z = sublineScanner.next();
                    Vector3 vector = new Vector3(x, y, z);
                    out.Normals.add(vector);
                    break;
                }
                case "o":
                {
                    out.Name = sublineScanner.next();
                    break;
                }
                case "f":
                {
                    String arg1 = sublineScanner.next();
                    String arg2 = sublineScanner.next();
                    String arg3 = sublineScanner.next();

                    String[] splitArg1 = arg1.split("/");
                    String[] splitArg2 = arg2.split("/");
                    String[] splitArg3 = arg3.split("/");

                    out.FaceVerticesIndex.add(Integer.valueOf(splitArg1[0]) -1);
                    out.FaceVerticesIndex.add(Integer.valueOf(splitArg2[0]) -1);
                    out.FaceVerticesIndex.add(Integer.valueOf(splitArg3[0]) -1);

                    out.FaceUvIndex.add(Integer.valueOf(splitArg1[1]) -1);
                    out.FaceUvIndex.add(Integer.valueOf(splitArg2[1]) -1);
                    out.FaceUvIndex.add(Integer.valueOf(splitArg3[1]) -1);

                    out.FaceNormalIndex.add(Integer.valueOf(splitArg3[2]) -1);
                    out.FaceNormalIndex.add(Integer.valueOf(splitArg3[2]) -1);
                    out.FaceNormalIndex.add(Integer.valueOf(splitArg3[2]) -1);
                    break;
                }
                case "vp":
                    UI.println("free form mesh not supported, skipping this import");
                    break;
                case "vt":
                {
                    String x = sublineScanner.next();
                    String y = sublineScanner.next();
                    Vector2 vector = new Vector2(x, y);
                    out.UV.add(vector);
                    break;
                }
                case "#":
                    UI.println("comment in file = " + sublineScanner.next());
                    break;
            }
            sublineScanner.close();
        }
        scanner.close();





        return out;
    }
}
