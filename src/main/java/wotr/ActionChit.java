package wotr;

/* renamed from: ActionChit */
public class ActionChit extends Chit implements Recoverable {
    public ActionChit(Area startLoc, String type, String pic) {
        super(startLoc, type, pic);
    }

    public ActionChit(Area startLoc, String type, String pic, int nation) {
        super(startLoc, type, pic, nation);
    }
}
