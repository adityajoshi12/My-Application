package chat.labs.test.chat.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RequestQueue requestQueue;
    private Spinner spinner,spinner_sort,spinner_order;
    private CharSequence _query;
    private String spinner_selected_item,spinner_sort_item,spinner_order_item;
    private LinearLayoutManager linearLayoutManager;
    private List<Model> modelList;
    private DividerItemDecoration dividerItemDecoration;
    private MaterialSearchBar searchView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        searchView = findViewById(R.id.search);
        spinner = findViewById(R.id.spinner_entity);
        spinner_sort = findViewById(R.id.spinner_sort);
        spinner_order = findViewById(R.id.spinner_order);
        recyclerView = findViewById(R.id.recycler_list);
        modelList = new ArrayList<>();
        adapter = new CustomAdapter(getApplicationContext(), modelList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        searchView.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                _query =  text;
                modelList.clear();
                getresturant(_query, spinner_selected_item,spinner_sort_item,spinner_order_item);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        //spinner 1
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.entityid));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //spinner 2
        spinner_sort.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter_sort = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.sort));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sort.setAdapter(adapter_sort);

        //spinner 3
        spinner_order.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter_order = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.order));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_order.setAdapter(adapter_order);

    }

    private void getresturant(CharSequence query, String spinner_selected_item, String spinner_sort_item, String spinner_order_item) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String entity_type = "entity_type=";
        String q = "&q=";

        String sort = "&sort="+spinner_sort_item;
        String order = "&order="+spinner_order_item;
        String url = "https://developers.zomato.com/api/v2.1/search?" + entity_type + spinner_selected_item + q + query + sort+order;

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray restaurants_array = response.getJSONArray("restaurants");
                            Log.e("before", "ess");
                            int i;
                            for (i = 0; i < restaurants_array.length(); i++) {
                                Model model = new Model();

                                JSONObject rest = restaurants_array.getJSONObject(i);
                                JSONObject jsonObject = rest.getJSONObject("restaurant");
                                String name = jsonObject.getString("name");
                                Log.e("after", name);
                                model.set_name(name);
                                String imageurl = jsonObject.getString("featured_image");
                                model.setImageurl(imageurl);
                                JSONObject location = jsonObject.getJSONObject("location");
                                String address = location.getString("address");
                                model.setLocation(address);
                                JSONObject user_rating = jsonObject.getJSONObject("user_rating");
                                String aggregate_rating = user_rating.getString("aggregate_rating");
                                String rating_text = user_rating.getString("rating_text");
                                String id=jsonObject.getString("id");
                                model.setId(id);
                                model.setRating(aggregate_rating+"\n"+rating_text);
                                modelList.add(model);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("err", e.getMessage());
                            progressDialog.dismiss();

                        }
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })

        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("user-key", "c750173e8cf7e5fdc2c331cf897ee8c3");
                return headers;
            }
        };
        requestQueue.add(postRequest);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId())

        {
            case R.id.spinner_entity:

                spinner_selected_item = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_sort:
                String x=parent.getItemAtPosition(position).toString();
                if (x.equals("Cost"))
                    x="cost";
                else if (x.equals("Rating"))
                    x="rating";
                spinner_sort_item=x;
                break;
            case R.id.spinner_order:
                String x3=parent.getItemAtPosition(position).toString();
                if (x3.equals("Ascending")) {
                    x3="asc";
                }
                else
                    x3="desc";
                spinner_order_item = x3;
                break;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
