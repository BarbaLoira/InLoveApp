package inlove.barbaloira.inlove.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import inlove.barbaloira.inlove.R;
import inlove.barbaloira.inlove.model.PopupFlirt;

/**
 * Created by marco on 28/01/2018.
 */

public class PopupFlirtAdapter extends BaseAdapter {
    Context context;
    List<PopupFlirt> listPopupFlirts;


    public PopupFlirtAdapter(Context context, List<PopupFlirt> popupFlirts) {


        this.context = context;
        this.listPopupFlirts = popupFlirts;

    }


    @Override
    public int getCount() {
        return this.listPopupFlirts.size();
    }

    @Override
    public PopupFlirt getItem(int i) {
        return this.listPopupFlirts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LinearLayout v = (LinearLayout)
                    LinearLayout.inflate(this.context, R.layout.item_popup_flirt, null);

            ImageView civ_draw_flit = (ImageView) v.findViewById(R.id.civ_draw_flit);
            TextView tv_msg_flit = (TextView) v.findViewById(R.id.tv_msg_flit);


            civ_draw_flit.setImageResource(listPopupFlirts.get(i).getDraw_flit());
            tv_msg_flit.setText(listPopupFlirts.get(i).getMsg_flit());



            return v;
        } else {
            LinearLayout v = (LinearLayout) view;

            ImageView civ_draw_flit = (ImageView) v.findViewById(R.id.civ_draw_flit);
            TextView tv_msg_flit = (TextView) v.findViewById(R.id.tv_msg_flit);


            civ_draw_flit.setImageResource(listPopupFlirts.get(i).getDraw_flit());
            tv_msg_flit.setText(listPopupFlirts.get(i).getMsg_flit());




            return view;

        }


    }



}
