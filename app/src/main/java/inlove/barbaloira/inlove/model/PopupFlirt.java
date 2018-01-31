package inlove.barbaloira.inlove.model;

/**
 * Created by marco on 28/01/2018.
 */

public class PopupFlirt {


    private int id;
    private int draw_flit;
    private String msg_flit;

    public PopupFlirt(int id, int draw_flit, String msg_flit) {
        this.id = id;
        this.draw_flit = draw_flit;
        this.msg_flit = msg_flit;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDraw_flit() {
        return draw_flit;
    }

    public void setDraw_flit(int draw_flit) {
        this.draw_flit = draw_flit;
    }

    public String getMsg_flit() {
        return msg_flit;
    }

    public void setMsg_flit(String msg_flit) {
        this.msg_flit = msg_flit;
    }
}
