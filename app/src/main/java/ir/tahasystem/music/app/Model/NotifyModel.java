package ir.tahasystem.music.app.Model;

import java.io.Serializable;
import java.util.List;

public class NotifyModel implements Serializable {

    static final long serialVersionUID =8996890340799609057L;

    public VersionModel ver;
    public List<Kala> notify;


    public List<Kala> getNotify() {
        return notify;
    }

    public VersionModel getVer() {
        return ver;
    }
}
