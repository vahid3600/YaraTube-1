package com.example.asus.yaratube.productlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.yaratube.R;
import com.example.asus.yaratube.data.model.Product;

import java.util.List;

import static com.example.asus.yaratube.util.Util.BASE_URL;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private List<Product> products;

    ProductListAdapter() {

    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View result = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
        return new ProductListViewHolder(result);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {

        holder.onBind(products.get(position));
    }

    @Override
    public int getItemCount() {

        if(products == null)
            return 0;
        return products.size();
    }

    class ProductListViewHolder extends RecyclerView.ViewHolder {

        private String productImageUrl;
        private ImageView productImage;
        private TextView productName;
        private TextView productDescription;

        ProductListViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.pro_image);
            productName = itemView.findViewById(R.id.pro_name);
            productDescription = itemView.findViewById(R.id.pro_desc);
        }

        void onBind(Product product) {

            if(product.getAvatar() != null) {
                productImageUrl = BASE_URL + '/' + product.getAvatar().getXxhdpi();
                Glide.with(itemView.getContext()).load(productImageUrl).into(productImage);
            }

            productName.setText(product.getName());
            productDescription.setText(product.getShortDescription());

        }
    }
}
