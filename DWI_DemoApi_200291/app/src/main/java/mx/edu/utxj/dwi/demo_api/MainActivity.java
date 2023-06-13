package mx.edu.utxj.dwi.demo_api;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button btnGuardar;
    private Button btnBuscar;
    private Button btnActualizar;
    private Button btnEliminar;
    private EditText etNombre;
    private EditText etAño;
    private EditText etTipo;
    private EditText etTemporada;

    private ListView lvDoramas;
    private RequestQueue colaPeticiones;
    private JsonArrayRequest jsonArrayRequest;
    private ArrayList<String> origenDatos = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String url = "http://10.10.62.17:3400/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGuardar = findViewById(R.id.btnSave);
        btnActualizar = findViewById(R.id.btnUpdate);
        btnBuscar = findViewById(R.id.btnSearch);
        btnEliminar = findViewById(R.id.btnDelete);
        etNombre = findViewById(R.id.etNombre);
        etAño = findViewById(R.id.etAño);
        etTipo = findViewById(R.id.etTipo);
        etTemporada = findViewById(R.id.etTemporada);
        lvDoramas = findViewById(R.id.lvDoramas);
        colaPeticiones = Volley.newRequestQueue(this);
        listDoramas();




        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest peticion = new JsonObjectRequest(
                        Request.Method.GET,
                        url + etNombre.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("status"))
                                    Toast.makeText(MainActivity.this, "Dorama no encontrado", Toast.LENGTH_SHORT).show();
                                else {
                                    try {
                                        etAño.setText(response.getString("año"));
                                        etTipo.setText(response.getString("tipo"));
                                        etTemporada.setText(String.valueOf(response.getInt("temporada")));
                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Dorama no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                colaPeticiones.add(peticion);
            }
        });





        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject dorama = new JSONObject();
                try {
                    dorama.put("nombre",etNombre.getText().toString());
                    dorama.put("año",etAño.getText().toString());
                    dorama.put("tipo",etTipo.getText().toString());
                    dorama.put("temporada",Float.parseFloat(etTemporada.getText().toString()));
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url +"insert/",
                        dorama,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Dorama insertado")) {
                                        Toast.makeText(MainActivity.this, "Dorama insertado con EXITO!", Toast.LENGTH_SHORT).show();
                                        etNombre.setText("");
                                        etAño.setText("");
                                        etTipo.setText("");
                                        etTemporada.setText("");
                                        adapter.clear();
                                        lvDoramas.setAdapter(adapter);
                                        listDoramas();
                                    }
                                }catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                colaPeticiones.add(jsonObjectRequest);
            }
        });


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNombre.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Primero busque ;(", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject doramas = new JSONObject();
                    try {
                        doramas.put("nombre", etNombre.getText().toString());

                        if (!etAño.getText().toString().isEmpty()) {
                            doramas.put("año", Float.parseFloat(etAño.getText().toString()));
                        }

                        if (!etTipo.getText().toString().isEmpty()) {
                            doramas.put("tipo", etTipo.getText().toString());
                        }

                        if (!etTemporada.getText().toString().isEmpty()) {
                            doramas.put("temporada", Float.parseFloat(etTemporada.getText().toString()));
                        }

                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    JsonObjectRequest actualizar = new JsonObjectRequest(
                            Request.Method.PUT,
                            url + "actualizar/" + etNombre.getText().toString(),
                            doramas,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Dorama actualizado")) {
                                            Toast.makeText(MainActivity.this, "Dorama actualizado", Toast.LENGTH_SHORT).show();

                                        } else if (response.getString("status").equals("No encontrado")) {
                                            Toast.makeText(MainActivity.this, "Dorama no encontrado", Toast.LENGTH_SHORT).show();
                                        }


                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                 }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                    );
                    colaPeticiones.add(actualizar);

                    listDoramas();
                    adapter.notifyDataSetChanged();


                    etNombre.setText("");
                    etAño.setText("");
                    etTipo.setText("");
                    etTemporada.setText("");
                }
            }
        });








        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNombre.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese el nombre del dorama", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.DELETE,
                            url + "borrar/" + etNombre.getText().toString(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Dorama eliminado")) {
                                            Toast.makeText(MainActivity.this, "Dorama eliminado", Toast.LENGTH_SHORT).show();
                                        } else if (response.getString("status").equals("Not Found")) {
                                            Toast.makeText(MainActivity.this, "Dorama no encontrado", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    colaPeticiones.add(jsonObjectRequest);

                    listDoramas();
                    adapter.notifyDataSetChanged();


                    etNombre.setText("");
                    etAño.setText("");
                    etTipo.setText("");
                    etTemporada.setText("");
                }
            }
        });


    }










    protected void listDoramas(){

        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        origenDatos.clear();
                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        for(int i = 0;i < response.length(); i++){
                            try {
                                String nombre =response.getJSONObject(i).getString("nombre");
                                String año = response.getJSONObject(i).getString("año") ;
                                String tipo = response.getJSONObject(i).getString("tipo");
                                origenDatos.add(nombre +"::" +año+"::"+tipo);
                            } catch (JSONException e) {
                            }
                        }
                        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, origenDatos);
                        lvDoramas.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
        );
        colaPeticiones.add(jsonArrayRequest);
    }
}