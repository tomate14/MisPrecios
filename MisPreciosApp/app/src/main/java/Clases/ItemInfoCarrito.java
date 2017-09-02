package Clases;

/**
 * Created by Maxi on 1/9/2017.
 */

public class ItemInfoCarrito {
    private String nombreSuper;
    private float importe;
    private int numPadre;

    public ItemInfoCarrito(String nombreSuper, float importe, int numero) {
        this.nombreSuper = nombreSuper;
        this.importe     = importe;
        this.numPadre    = numero;
    }

    public String getNombreSuper() {
        return nombreSuper;
    }

    public void setNombreSuper(String nombreSuper) {
        this.nombreSuper = nombreSuper;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public int getNumPadre() {
        return numPadre;
    }

    public void setNumPadre(int numPadre) {
        this.numPadre = numPadre;
    }

    @Override
    public String toString() {
        return "ItemInfoCarrito{" +
                "nombreSuper='" + nombreSuper + '\'' +
                ", importe=" + importe +
                '}';
    }
}
