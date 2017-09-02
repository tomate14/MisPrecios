package Mostrar_Productos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maxi.mispreciosapp.R;

import java.util.ArrayList;

import Clases.ProductoCarrito;

/**
 * Created by Maxi on 30/8/2017.
 */

public class ListViewAdapterPrecios extends ListViewAdapter {
    public ListViewAdapterPrecios(Context context, ArrayList<String> titulos, ArrayList<String> importe){
        this.importe = importe;
        this.titulos = titulos;
        this.context = context;
    }

    @Override
    public Object getItem(int position) {
        return this.titulos.get(position);
    }

    @Override
    public int getCount() {
        return this.titulos.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtTitle;
        TextView txtImporte;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_row, parent, false);

        // Locate the TextViews in listview_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.list_row_title);
        //imgImg = (ImageView) itemView.findViewById(R.id.list_row_image);

        // Capture position and set to the TextViews
        txtTitle.setText(titulos.get(position));
        txtImporte = (TextView) itemView.findViewById(R.id.list_row_importe);
        txtImporte.setText(importe.get(position));

        //imgImg.setImageResource(imagenes[position]);

        return itemView;
    }

    @Override
    public ArrayList<ProductoCarrito> getProductos() {
        return null;
    }

}
