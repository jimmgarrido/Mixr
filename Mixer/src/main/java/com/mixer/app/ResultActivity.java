package com.mixer.app;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {
   private static final String table = "Alcohol";
    //private static final String columns [] = {"name","type","" };
    private static final String Alcohol1 = "alc_1";
    private static final String Alcohol2 = "alc_2";
    private String selection1="", selection2="", selection3="", selection4="";
    private String anySelection = "alc_1=? OR alc_2=? OR alc_3=? OR alc_4=?";
    private String noneSelection = "alc_1=? AND alc_2 IS NULL";
    //private String allSelection;
    private String twoStatement = "(alc_1=? OR alc_1=?) AND (alc_2=? OR alc_2=?)";
    private String oneStatement;
    private String anyStatement;
    private DataBaseHelper drinkHelper = new DataBaseHelper(this);
    private SQLiteDatabase drinksDb;
    private Cursor dbResult;
    private int numResults, position, numTimes=0;
    TextView drinkName, line1, line2, line3, line4, line5;
    String line1Text, line2Text, line3Text, line4Text, line5Text;
    View view;
    Bitmap pic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle bundle = getIntent().getExtras();
        selection1 = bundle.getString(HomeActivity.SELECTION1);
        selection2 = bundle.getString(HomeActivity.SELECTION2);
        selection3 = bundle.getString(HomeActivity.SELECTION3);
        selection4 = bundle.getString(HomeActivity.SELECTION4);

        makeResults();
        drinkHelper.close();
        showResult(numTimes);
    }


    @Override
    protected void onStart(){
        super.onStart();
    }

    protected void onStop() {
        drinkHelper.close();
        if(pic != null) {
            pic.recycle();
        }
        super.onStop();
    }

    public void makeResults() {
        try {
            drinkHelper.openDataBase();
        } catch(SQLException sqle){
            throw sqle;
        }

        drinksDb = drinkHelper.getDatabase();

        if (selection3.equals("Any") || selection4.equals("Any")) {
            if (selection3.equals("Any")) {
                dbResult = drinksDb.query(table, null, null, null, null, null, null);
            }
            if (selection4.equals("Any")) {
                dbResult = drinksDb.query(table, null, anySelection, new String[]{selection3, selection3, selection3, selection3},
                        null, null, null);
            }
        } else if (selection4.equals("None")) {
            dbResult = drinksDb.query(table, null, noneSelection, new String[]{selection3}, null, null, null);

        } else if (selection1.equals("Any") && selection2.equals("Any")) {
            dbResult = drinksDb.query(table, null, null, null, null, null, null);
        }
        else {
            dbResult = drinksDb.query(table, null, twoStatement, new String[]{selection3,selection4,selection3,selection4}, null, null, null);
        }

        if (dbResult.getCount() > 0){
            numResults = dbResult.getCount();
        }
        else {
            numResults = -1;
        }

        numTimes = dbResult.getCount();
    }

    public void showResult(int counter) {
        int i =0, id;
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout)findViewById(R.id.Frame);
        ImageView drinkPic;
        int picHeight;

        //Bitmap pic;

        if (numResults > 0) {

            do {

                view = inflater.inflate(R.layout.card_style, null);
                drinkPic = (ImageView)view.findViewById(R.id.drinkPic);
                drinkName = (TextView)view.findViewById(R.id.drinkName);
                position = i;
                dbResult.moveToPosition(i);

                id = getResources().getIdentifier("com.mixer.app:drawable/" + dbResult.getString(1).toLowerCase().replaceAll("\\s",""), null, null);

                pic = decodeSampledBitmapFromResource(getResources(),id, 200, 160);
                //getImage();

                //drinkPic.setImageResource(id);
                drinkName.setText(dbResult.getString(1));
                drinkPic.setImageBitmap(pic);

                resetText();
                setText();

                if (line1Text.equals("") !=true) {
                    line1.setVisibility(View.VISIBLE);
                    line1.setText(line1Text);
                }
                if (line2Text.equals("") !=true){
                    line2.setVisibility(View.VISIBLE);
                    line2.setText(line2Text);
                }
                if (line3Text.equals("") !=true){
                    line3.setVisibility(View.VISIBLE);
                    line3.setText(line3Text);
                }
                if (line4Text.equals("") !=true){
                    line4.setVisibility(View.VISIBLE);
                    line4.setText(line4Text);
                }
                if (line5Text.equals("") !=true){
                    line5.setVisibility(View.VISIBLE);
                    line5.setText(line5Text);
                }

                layout.addView(view);
                i++;
            } while (i<counter);
        } else if (numResults == -1) {
            Toast toast = Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public int getRandom(int limit) {
        int number;
        do {
            number = (int) (Math.round(Math.random() *limit));
        } while (number < 0 || number >= limit);
        return number;
    }

    public void setText() {
        for ( int j=3; j<16;j++) {
            if (j>=3 && j<=5){
                if(dbResult.isNull(j) == false) {
                    line1Text = line1Text + dbResult.getString(j) + " ";
                }
            }
            if (j>=6 && j<=8){
                if(dbResult.isNull(j) == false) {
                    line2Text = line2Text + dbResult.getString(j) + " ";
                }
            }
            if (j>=9 && j<=11){
                if(dbResult.isNull(j) == false) {
                    line3Text = line3Text + dbResult.getString(j) + " ";
                }
            }
            if (j>=12 && j<=14){
                if(dbResult.isNull(j) == false) {
                    line4Text = line4Text + dbResult.getString(j) + " ";
                }
            }
            if (j==15){
                if(dbResult.isNull(j) == false) {
                    line5Text = dbResult.getString(j);
                }
            }
        }
    }

    public void resetText() {
        line1Text="";
        line2Text="";
        line3Text="";
        line4Text="";
        line5Text="";

        line1 = (TextView)view.findViewById(R.id.textView);
        line1.setVisibility(View.GONE);
        line2 = (TextView)view.findViewById(R.id.textView2);
        line2.setVisibility(View.GONE);
        line3 = (TextView)view.findViewById(R.id.textView3);
        line3.setVisibility(View.GONE);
        line4 = (TextView)view.findViewById(R.id.textView4);
        line4.setVisibility(View.GONE);
        line5 = (TextView)view.findViewById(R.id.textView5);
        line5.setVisibility(View.GONE);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 8;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
