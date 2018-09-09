package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lastmilesale.android.mobileapps.lastmile.Interfaces.ItemClickListener;
import com.lastmilesale.android.mobileapps.lastmile.Objects.Order;
import com.lastmilesale.android.mobileapps.lastmile.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public List<Order> orderList;
    private  String user_id;
    private String status,player_id;
    public SharedPreferences appFile;
    public SharedPreferences.Editor appFileEditor;


    public OrdersRecyclerAdapter(Context context, String user_id, List<Order> orderList, String status){
        super();
        this.orderList = orderList;
        this.context = context;
        this.user_id = user_id;
        this.status = status;
        this.appFile = context.getSharedPreferences(context.getResources().getString(R.string.app_file),0);
        this.appFileEditor = appFile.edit();
        this.player_id = appFile.getString(context.getResources().getString(R.string.player_id),"");
    }

@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row_layout, parent, false);
    return new ViewHolder(view);
}



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Order order = orderList.get(position);

        final ViewHolder iHolder = (ViewHolder) holder;

        iHolder.name.setText("Order ID: "+order.getId());
        iHolder.supplierName.setText(order.getSupplier_name()+":"+order.getProduct_name());
        iHolder.quantity.setText("Quantity: "+order.getNoOfCartons()+" Carton(s), "+order.getNoOfDozens()+" Dozen(s), & "+order.getNoOfPieces()+" Piece(s)");
        iHolder.statusLabel.setText(order.getOrder_status());
        iHolder.price.setText(order.getPrice());

        if(order.getStatus().equals("ontransit")){
            iHolder.confirmDeliveryBtn.setVisibility(View.VISIBLE);
        }

        Picasso.with(context.getApplicationContext()).load(order.getProductImage())
                .config(Bitmap.Config.ARGB_8888)
                .error(R.drawable.lastmile_logo_vector)
                .into(iHolder.productPhoto);

        iHolder.confirmDeliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iHolder.confirmDeliveryBtn.setVisibility(View.GONE);
                orderList.get(position).setOrder_status("delivered");
                notifyDataSetChanged();
                new SwitchOnTransitToDelivered(context,order.getId()).execute();
            }
        });

        iHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //do nothing
            }
        });
}


@Override
public int getItemCount() {
    int count = orderList.size();
 return count;
}

private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView name,supplierName,quantity,statusLabel,price;
    ImageView productPhoto;
    Button confirmDeliveryBtn;
    private ItemClickListener itemClickListener;


    public ViewHolder(View itemView) {

        super(itemView);

        name = (TextView) itemView.findViewById(R.id.product_name);
        supplierName = (TextView) itemView.findViewById(R.id.product_supplier);
        quantity = (TextView) itemView.findViewById(R.id.product_quantity);
        productPhoto = (ImageView) itemView.findViewById(R.id.product_image);
        statusLabel = (TextView) itemView.findViewById(R.id.order_status_label);
        price = (TextView) itemView.findViewById(R.id.product_price);
        confirmDeliveryBtn = (Button) itemView.findViewById(R.id.order_checkout_btn);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());

    }
    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }

}


    public void removeCheckedOutItem(int position){
        orderList.remove(position);
        notifyDataSetChanged();
    }




}
