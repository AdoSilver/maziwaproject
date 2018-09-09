package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lastmilesale.android.mobileapps.lastmile.Objects.Product;
import com.lastmilesale.android.mobileapps.lastmile.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddToCartFragment extends DialogFragment {

    ImageView productImage;
    TextView quantityAvailable,productName,orderedTextView;
    Button cancelBtn,doneBtn,addToCartBtn,goToCartBtn;
    EditText piecesRequiredEdit;
    View rootView;
    Product product;
    String quantity_in_pieces;


    public AddToCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_to_cart, container, false);
        productImage = (ImageView) rootView.findViewById(R.id.product_image);
        quantityAvailable = (TextView) rootView.findViewById(R.id.product_content);
        productName = (TextView) rootView.findViewById(R.id.product_name);
        cancelBtn = (Button) rootView.findViewById(R.id.add_to_cart_cancel_btn);
        doneBtn = (Button) rootView.findViewById(R.id.add_to_cart_done_btn);
        addToCartBtn = (Button) rootView.findViewById(R.id.add_to_cart_btn);
        goToCartBtn = (Button) rootView.findViewById(R.id.go_to_cart_btn);
        piecesRequiredEdit = (EditText) rootView.findViewById(R.id.no_of_pieces);
        orderedTextView = (TextView) rootView.findViewById(R.id.no_of_pieces_ordered);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        product = ((ProductsActivity)getActivity()).clickedProduct;

        productName.setText(product.getName());
        quantityAvailable.setText(product.getQuantity());
        Picasso.with(getActivity().getApplicationContext()).load("http://lastmilesale.com/api/products/supplier/8")
                .config(Bitmap.Config.ARGB_8888)
                .error(R.drawable.lastmile_logo_vector)
                .into(productImage);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_in_pieces = piecesRequiredEdit.getText().toString();
//                if(quantity_in_pieces.length() != 0){
//                    doneBtn.setVisibility(View.VISIBLE);
//                    cancelBtn.setVisibility(View.INVISIBLE);
//                    new AddProductToCart(getActivity(),product.getId(),quantity_in_pieces,"cart").execute();
//                    orderedTextView.setVisibility(View.VISIBLE);
//                    piecesRequiredEdit.setVisibility(View.INVISIBLE);
//                    orderedTextView.setText(quantity_in_pieces+" pieces added to cart");
//                    addToCartBtn.setVisibility(View.INVISIBLE);
//                    goToCartBtn.setVisibility(View.VISIBLE);
//                }else{
//                    Toast.makeText(getActivity(),"Fill quantity you wish to add in cart",Toast.LENGTH_SHORT).show();
//                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        goToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CartActivity.class);
                startActivity(i);
                dismiss();
            }
        });



        return rootView;
    }

}
