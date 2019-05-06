
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Sammy
 */
@ManagedBean
@SessionScoped

public class NavigationBean implements Serializable {

    private static final long serialVersionUID = 1520318172495977648L;

    public String toGirisyap() {
        return "/girisyap.xhtml";
    }

    public String redirectToGirisyap() {
        return "/girisyap.xhtml?faces-redirect=true";
    }

    public String toIndex() {
        return "/index.xhtml";
    }

    public String redirectToIndex() {
        return "/index.xhtml?faces-redirect=true";
    }

    public String redirectToRandevu() {
        return "/secured/randevu.xhtml?faces-redirect=true";
    }

    public String toRandevu() {
        return "/secured/randevu.xhtml";
    }

}
