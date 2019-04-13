package com.tandai.orderfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class QuanAnAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<QuanAn> ListQuanAn;

    public QuanAnAdapter(Context context, int layout, List<QuanAn> listQuanAn) {
        this.context = context;
        this.layout = layout;
        ListQuanAn = listQuanAn;
    }

    @Override
    public int getCount() {
        return ListQuanAn.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        //anh xa view
        TextView tenQuan =(TextView) view.findViewById(R.id.tvtenQuan_line_retaurent);
        TextView diachi =(TextView) view.findViewById(R.id.tvdiachi_line_retaurent);
        TextView sdt = (TextView) view.findViewById(R.id.tvsdt_lin_restaurent);

        //set value
        QuanAn quan = ListQuanAn.get(position);
        tenQuan.setText("Quán "+quan.getTenQuan());
        diachi.setText(quan.getDiachi());
        sdt.setText("Sđt: "+ quan.getSdt());
        return view;
    }
}
