package com.example.Varsani.Clients;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Adapters.AdapterOrdersItems;
import com.example.Varsani.Clients.Models.OrderItemModal;
import com.example.Varsani.R;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewHistory extends AppCompatActivity {

    private TextView tvOrderId, tvOrderCost, tvMpesaCode, tvOrderDate, tvOrderStatus, tvItemCost, tvShippingCost;
    private  TextView tv_item_name,tv_item_quantity;
    private String orderID;
    private Button btn_mark_order,btn_mark_order2 ;
    private EditText quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize TextViews
        tvOrderId = findViewById(R.id.tv_order_id);
        tvOrderCost = findViewById(R.id.tv_order_cost);
        tvMpesaCode = findViewById(R.id.tv_mpesa_code);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvOrderStatus = findViewById(R.id.tv_order_status);
        tvItemCost = findViewById(R.id.tv_item_cost);
        tvShippingCost = findViewById(R.id.tv_shipping_cost);
        tv_item_name = findViewById(R.id.tv_item_name);
        tv_item_quantity = findViewById(R.id.tv_item_quantity);
        btn_mark_order=findViewById(R.id.btn_mark_order);
        btn_mark_order2=findViewById(R.id.btn_mark_order2);

        btn_mark_order.setVisibility(View.GONE);
        btn_mark_order2.setVisibility(View.GONE);

        // Get data from Intent
        orderID = getIntent().getStringExtra("orderID");
        String orderCost = getIntent().getStringExtra("orderCost");
        String mpesaCode = getIntent().getStringExtra("mpesaCode");
        String orderDate = getIntent().getStringExtra("orderDate");
        String orderStatus = getIntent().getStringExtra("orderStatus");
        String itemCost = getIntent().getStringExtra("itemCost");
        String shippingCost = getIntent().getStringExtra("shippingCost");

        // Set the values to TextViews
        tvOrderId.setText("Order ID: " + orderID);
        tvOrderCost.setText("Total Amount: KES " + orderCost);
        tvMpesaCode.setText("Mpesa Code: " + mpesaCode);
        tvOrderDate.setText("Order Date: " + orderDate);
        tvOrderStatus.setText("Order Status: " + orderStatus);
        //tvItemCost.setText("Item Cost: KES " + itemCost);
        tvShippingCost.setText("Shipping Cost: KES " + shippingCost);

        if(orderStatus.equals("Confirm delivery")){
            btn_mark_order.setVisibility(View.VISIBLE);
            btn_mark_order2.setVisibility(View.VISIBLE);
        }
        btn_mark_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlert(v);
            }
        });

        btn_mark_order2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qtyPrompts();
            }
        });

        getOrderItems();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void getOrderItems(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.URL_GET_ORDER_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("Response"," "+response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if(status.equals("1")){
                                JSONArray jsonArray=jsonObject.getJSONArray("items");
                                for(int i=0; i <jsonArray.length();i++){
                                    JSONObject jsn=jsonArray.getJSONObject(i);
                                    String itemName = jsn.getString("itemName");
                                    String itemPrice = jsn.getString("itemPrice");
                                    String quantity = jsn.getString("quantity");
                                    String subTotal = jsn.getString("subTotal");

                                    tv_item_quantity.setText("Quantity: " + quantity);
                                    tv_item_name.setText("Items: " + itemName);
                                    tvItemCost.setText("Item Cost: " + itemPrice);

                                }
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("orderID",orderID);
                return params;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void getAlert(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm");

        builder.setNegativeButton("Close",null);
        builder.setPositiveButton("Confirm delivery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                markDelivered();

                return;
            }
        });
        builder.show();

    }
    public void qtyPrompts(){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.promptsreject, null);

        //Create the AlertDialog with a reference to edit it later
        final AlertDialog dialog = new AlertDialog.Builder(this)
//                .setView(v)
                .setCancelable(false)
                .setPositiveButton("Reject", null) //Set to null. We override the onclick
                .setNegativeButton("Close", null)
                .create();
        dialog.setView(promptsView);


        quantity =  promptsView.findViewById(R.id.edt_quantity);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(quantity.getText().toString())) {
                            quantity.setError( "Enter reason" );
                        }else{
                            dialog.dismiss();
                            markRejected();
                        }
                    }
                });
            }
        });
        dialog.show();
    }
    public void markDelivered(){
        btn_mark_order.setVisibility(View.GONE);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.URL_MARK_DELIVERED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if(status.equals("1")){
                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{
                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            btn_mark_order.setVisibility(View.GONE);
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                btn_mark_order.setVisibility(View.VISIBLE);

            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String>params=new HashMap<>();
                params.put("orderID",orderID);
                return  params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void markRejected(){
        btn_mark_order2.setVisibility(View.GONE);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.URL_MARK_REJECTED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if(status.equals("1")){
                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{
                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            btn_mark_order2.setVisibility(View.GONE);
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                btn_mark_order2.setVisibility(View.VISIBLE);

            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String>params=new HashMap<>();
                params.put("orderID",orderID);
                return  params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
