package com.volleyarray.voray;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.ProgressBar;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonArrayRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // creating variables for
    // our ui components.
    private RecyclerView courseRV;

    // variable for our adapter
    // class and array list
    private CourseAdapter adapter;
    private ArrayList<CourseModal> courseModalArrayList;

    // below line is the variable for url from
    // where we will be querying our data.
    String url = "https://jsonkeeper.com/b/WO6S";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing our variables.
        courseRV = findViewById(R.id.idRVCourses);
        progressBar = findViewById(R.id.idPB);

        // below line we are creating a new array list
        courseModalArrayList = new ArrayList<>();
        getData();

        // calling method to
        // build recycler view.
        buildRecyclerView();
    }

    private void getData() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                courseRV.setVisibility(View.VISIBLE);
                for (int i = 0; i < response.length(); i++) {
                    // creating a new json object and
                    // getting each object from our json array.
                    try {
                        // we are getting each json object.
                        JSONObject responseObj = response.getJSONObject(i);

                        // now we get our response from API in json object format.
                        // in below line we are extracting a string with
                        // its key value from our json object.
                        // similarly we are extracting all the strings from our json object.
                        String courseName = responseObj.getString("courseName");
                        String courseTracks = responseObj.getString("courseTracks");
                        String courseMode = responseObj.getString("courseMode");
                        String courseImageURL = responseObj.getString("courseimg");
                        courseModalArrayList.add(new CourseModal(courseName, courseImageURL, courseMode, courseTracks));
                        buildRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new CourseAdapter(courseModalArrayList, MainActivity.this);

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        courseRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        courseRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        courseRV.setAdapter(adapter);
    }
}