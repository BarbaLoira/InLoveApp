package inlove.barbaloira.inlove.Shared;

import java.util.ArrayList;
import java.util.List;

import inlove.barbaloira.inlove.R;
import inlove.barbaloira.inlove.model.PopupFlirt;

/**
 * Created by marco on 01/02/2018.
 */

public abstract class Util {


    public static List<PopupFlirt> returnDataIteFlirt() {
        List<PopupFlirt> popupFlirts = new ArrayList<>();


            popupFlirts.add(new PopupFlirt(0, R.drawable.popcorn, "Vamos ver netflix?"));

            popupFlirts.add(new PopupFlirt(1, R.drawable.music, "Ta afim de escutar uma musica?"));

            popupFlirts.add(new PopupFlirt(2, R.drawable.kiss_cute, "Que sorriso meigo você tem !"));

            popupFlirts.add(new PopupFlirt(3, R.drawable.kiss, "Vamos dar uns amassos?"));

            popupFlirts.add(new PopupFlirt(4, R.drawable.chat, "Bora pro chat !"));

        return popupFlirts;
    }


}
