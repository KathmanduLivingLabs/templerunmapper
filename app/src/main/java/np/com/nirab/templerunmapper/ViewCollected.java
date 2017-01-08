package np.com.nirab.templerunmapper;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCollected extends AppCompatActivity{

    DatabaseHelper myDb;
    TextView nothingHere;
    private ArrayList<Long> mSelected = new ArrayList<Long>();
    ListView templeListView;
    EditText editUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_collected);
        nothingHere = (TextView) findViewById(R.id.nothing_message);
        templeListView = (ListView) findViewById(R.id.saved_feature_list);
        editUsername = (EditText) findViewById(R.id.editText_username);
        myDb = new DatabaseHelper(this);
        populateListView();
    }

    public void submitData(View view) throws JSONException {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

        if (ni == null || !ni.isConnected()) {
            Toast.makeText(ViewCollected.this,
                    "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else if (editUsername.getText().toString().equals("")) {
            Toast.makeText(ViewCollected.this,
                    "Please enter username", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadRecords();
        }
    }

    public void uploadRecords(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Write a message to the database
        Cursor c = myDb.getUnsentRows();
        c.moveToFirst();
        DatabaseReference myRef = database.getReference(editUsername.getText().toString());
        while (!c.isAfterLast()) {
            try {
                DatabaseReference newEntry = myRef.push();

                newEntry.child(DatabaseHelper.COL_RELIGION).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_RELIGION)));
                newEntry.child(DatabaseHelper.COL_NAME).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_NAME)));
                newEntry.child(DatabaseHelper.COL_WATER).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_WATER)));
                newEntry.child(DatabaseHelper.COL_TOILET).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_TOILET)));
                newEntry.child(DatabaseHelper.COL_WHEELCHAIR).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_WHEELCHAIR)));
                newEntry.child(DatabaseHelper.COL_NUM_BUILDINGS).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_NUM_BUILDINGS)));
                newEntry.child(DatabaseHelper.COL_ESTABLISHED).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_ESTABLISHED)));
                newEntry.child(DatabaseHelper.COL_OPENING_HOUR).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_OPENING_HOUR)));
                newEntry.child(DatabaseHelper.COL_LATITUDE).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_LATITUDE)));
                newEntry.child(DatabaseHelper.COL_LONGITUDE).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_LONGITUDE)));
                newEntry.child(DatabaseHelper.COL_ACCURACY).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_ACCURACY)));
                newEntry.child(DatabaseHelper.COL_ID).setValue(c.getString(c.getColumnIndex(DatabaseHelper.COL_ID)));
                myDb.updateRow(c.getString(c.getColumnIndex(DatabaseHelper.COL_ID)));
                templeListView.setAdapter(null);
                populateListView();
                c.moveToNext();


            }catch (Exception e){
                System.out.println(e);
            }
        }
        editUsername.setText("");
    }

    public void populateListView(){
        Cursor cursor = myDb.getUnsentRows();
        if (cursor.getCount() == 0){
            nothingHere.setVisibility(View.VISIBLE);
        }else {
            nothingHere.setVisibility(View.GONE);
            String[] temples = new String[]{DatabaseHelper.COL_NAME, DatabaseHelper.COL_LATITUDE, DatabaseHelper.COL_LONGITUDE};
            int[] listitemids = new int[]{R.id.list_item_name_text, R.id.list_item_latitude_text, R.id.list_item_longitude_text};
            SimpleCursorAdapter sca;
            sca = new SimpleCursorAdapter(getBaseContext(), R.layout.list_item, cursor, temples, listitemids);
            templeListView.setAdapter(sca);
        }
    }



    public JSONObject getAllDataAndGenerateJSON() throws JSONException {


        Cursor c = myDb.getUnsentRows();
        c.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray TempleArray = new JSONArray();



        int i = 0;
        while (!c.isAfterLast()) {


            JSONObject temple = new JSONObject();
            try {
                temple.put("Religion", c.getString(c.getColumnIndex(DatabaseHelper.COL_RELIGION)));
                temple.put("Name", c.getString(c.getColumnIndex(DatabaseHelper.COL_NAME)));
                temple.put("Water", c.getString(c.getColumnIndex(DatabaseHelper.COL_WATER)));
                temple.put("Toilet", c.getString(c.getColumnIndex(DatabaseHelper.COL_TOILET)));
                temple.put("Wheelchair", c.getString(c.getColumnIndex(DatabaseHelper.COL_WHEELCHAIR)));
                temple.put("Buildings", c.getString(c.getColumnIndex(DatabaseHelper.COL_NUM_BUILDINGS)));
                temple.put("Established", c.getString(c.getColumnIndex(DatabaseHelper.COL_ESTABLISHED)));
                temple.put("Opening", c.getString(c.getColumnIndex(DatabaseHelper.COL_OPENING_HOUR)));
                temple.put("Latitude", c.getString(c.getColumnIndex(DatabaseHelper.COL_LATITUDE)));
                temple.put("Longitude", c.getString(c.getColumnIndex(DatabaseHelper.COL_LONGITUDE)));
                temple.put("Accuracy", c.getString(c.getColumnIndex(DatabaseHelper.COL_ACCURACY)));
                temple.put("ID", c.getString(c.getColumnIndex(DatabaseHelper.COL_ID)));

                c.moveToNext();

                TempleArray.put(i, temple);
                i++;

            } catch (JSONException e) {

                e.printStackTrace();
            }



        }
        Root.put("Temples", TempleArray);
        return Root;
    }

}
