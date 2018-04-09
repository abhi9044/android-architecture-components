package com.example.abhisheksingh.fireapp.Helpers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static com.example.abhisheksingh.fireapp.Helpers.Constants.baseUrl;

public class Utils {
    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static Boolean isBlank(String str)
    {
    if ((str == null) || (str.isEmpty())) {
        return  true;
    }
    else {
        return  false;
    }

    }
    
    public static void writeToFile(String data,File path,String name)
    {
        // Get the directory for the user's public pictures directory.
        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path,name);

        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
