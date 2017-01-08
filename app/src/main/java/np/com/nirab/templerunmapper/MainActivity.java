package np.com.nirab.templerunmapper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void recordDetails(View view){
        Intent recordIntent = new Intent(this, RecordDetailsActivity.class);
        startActivity(recordIntent);
    }

    public void viewCollected(View view){
        Intent viewIntent = new Intent(this, ViewCollected.class);
        startActivity(viewIntent);
    }
}
