package inlove.barbaloira.inlove.model;

/**
 * Created by marco on 03/02/2018.
 */

public class FlirtLove {
    private String idFan; // id de quem mandou
    private int idItemPopupFlirt;

    public FlirtLove(){}

    public FlirtLove(String idFan, int idItemPopupFlirt) {
        this.idFan = idFan;
        this.idItemPopupFlirt = idItemPopupFlirt;
    }

    public String getIdFan() {
        return idFan;
    }

    public void setIdFan(String idFan) {
        this.idFan = idFan;
    }

    public int getIdItemPopupFlirt() {
        return idItemPopupFlirt;
    }

    public void setIdItemPopupFlirt(int idItemPopupFlirt) {
        this.idItemPopupFlirt = idItemPopupFlirt;
    }
}
