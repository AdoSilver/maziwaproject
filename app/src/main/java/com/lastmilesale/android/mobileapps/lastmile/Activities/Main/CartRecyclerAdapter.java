package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

public class CartRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public List<Order> orderList;
    private  String user_id;
    private String status;
    private SharedPreferences appFile;
    private SharedPreferences.Editor appFileEdit;
    private int cart_count;


    public CartRecyclerAdapter(Context context, String user_id, List<Order> orderList){
        super();
        this.orderList = orderList;
        this.context = context;
        this.user_id = user_id;
        this.status = status;
        this.appFile =  context.getSharedPreferences(context.getResources().getString(R.string.app_file),0);
        this.appFileEdit = this.appFile.edit();
        try {
            this.cart_count = Integer.parseInt(appFile.getString(context.getResources().getString(R.string.user_cart_count), "0"));
        }catch (NumberFormatException ie){ie.printStackTrace();}
    }

@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row_layout, parent, false);
    return new ViewHolder(view);
}



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Order order = orderList.get(position);

        final ViewHolder iHolder = (ViewHolder) holder;

        iHolder.name.setText("Order ID: "+order.getId());
        iHolder.price.setText(order.getPrice());
        iHolder.supplierName.setText(order.getSupplier_name()+":"+order.getProduct_name());
        iHolder.quantity.setText("Quantity: "+order.getNoOfCartons()+" Carton(s), "+order.getNoOfDozens()+" Dozen(s), & "+order.getNoOfPieces()+" Piece(s)");

        Picasso.with(context.getApplicationContext()).load(order.getProductImage())
                .config(Bitmap.Config.ARGB_8888)
                .error(R.drawable.lastmile_logo_vector)
                .into(iHolder.productPhoto);



        iHolder.actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ((CartActivity)context).checkedOutPosition = position;
                    ((CartActivity)context).selectedOrder = order;
                    ((CartActivity)context).isCheckOutAll = false;
                    ((CartActivity)context).bottomSheetDialog.show();

            }
        });

        final PopupMenu nPopupMenu = new PopupMenu(context, iHolder.menuBtn);
        nPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_cart_item:
                        new DeleteCartItemTask(context, order.getId()).execute();
                        ((CartActivity) context).orderList.remove(position);
                        //removeCheckedOutItem(position);
                        notifyDataSetChanged();
                        //Toast.makeText(context, " ", Toast.LENGTH_SHORT).show();
                        cart_count --;
                        appFileEdit.putString(context.getResources().getString(R.string.user_cart_count),cart_count+"").commit();
                       break;
                 }
                return false;
            }
        });

        nPopupMenu.getMenuInflater().inflate(R.menu.cart_menu, nPopupMenu.getMenu());

        iHolder.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nPopupMenu.show();
            }
        });

        iHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //do nothing
                iHolder.actionBtn.performClick();
            }
        });

}


@Override
public int getItemCount() {
    int count = orderList.size();
    return count;
}

private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView name,supplierName,price,quantity;
    ImageView productPhoto,menuBtn;
    Button actionBtn;
    private ItemClickListener itemClickListener;


    public ViewHolder(View itemView) {

        super(itemView);

        name = (TextView) itemView.findViewById(R.id.product_name);
        supplierName = (TextView) itemView.findViewById(R.id.product_supplier);
        quantity = (TextView) itemView.findViewById(R.id.product_quantity);
        price = (TextView) itemView.findViewById(R.id.product_price);
        productPhoto = (ImageView) itemView.findViewById(R.id.product_image);
        actionBtn = (Button) itemView.findViewById(R.id.order_checkout_btn);
        menuBtn = (ImageView) itemView.findViewById(R.id.order_menu);

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

    public void removeAllItems(){
        orderList.clear();
        notifyDataSetChanged();
    }




}
