package inlove.barbaloira.inlove.repository;

import java.util.ArrayList;
import java.util.List;

import inlove.barbaloira.inlove.R;
import inlove.barbaloira.inlove.model.PopupFlirt;

/**
 * Created by marco on 28/01/2018.
 */

public class RepositoryPopupFlirt {
    private static ArrayList<PopupFlirt> popupFlirtList = new ArrayList<>();


    public static ArrayList<PopupFlirt> returnData() {
        for (int i = 0; i < 5; i++) {
            popupFlirtList.add(new PopupFlirt(i, R.drawable.item_popup_in_love, "me apaixonei por vc !!!"));
        }
        return popupFlirtList;
    }

}

