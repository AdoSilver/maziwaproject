package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lastmilesale.android.mobileapps.lastmile.Interfaces.ItemClickListener;
import com.lastmilesale.android.mobileapps.lastmile.Objects.Product;
import com.lastmilesale.android.mobileapps.lastmile.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public List<Product> productList;
    private  String user_id;


public ProductsRecyclerAdapter(Context context, String user_id,List<Product> productList){
        super();
        this.productList = productList;
        this.context = context;
        this.user_id = user_id;
        }

@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row_layout, parent, false);
    return new ViewHolder(view);
}



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Product product = productList.get(position);

        final ViewHolder iHolder = (ViewHolder) holder;

        iHolder.name.setText(product.getName());

        iHolder.price.setText(product.getPrice());

        iHolder.details.setText(product.getQuantity());

        Picasso.with(context.getApplicationContext()).load(product.getProduct_image())
                .config(Bitmap.Config.ARGB_8888)
                .error(R.drawable.lastmile_logo_vector)
                .into(iHolder.productPhoto);

        iHolder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProductsActivity)context).clickedProduct = product;
                ((ProductsActivity)context).bottomSheetDialog.show();
            }
        });

        iHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                iHolder.addToCart.performClick();
            }
        });

}


@Override
public int getItemCount() {
return productList.size();
}

private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView name,price,details;
    ImageView productPhoto,addToCart;
    private ItemClickListener itemClickListener;


    public ViewHolder(View itemView) {

        super(itemView);

        name = (TextView) itemView.findViewById(R.id.product_name);
        details = (TextView) itemView.findViewById(R.id.product_content);
        price = (TextView) itemView.findViewById(R.id.product_price);
        productPhoto = (ImageView) itemView.findViewById(R.id.product_image);
        addToCart = (ImageView) itemView.findViewById(R.id.product_shop_btn);

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




}
