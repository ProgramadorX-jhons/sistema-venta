package unam.dgtic.spv.core.model;

public class ArticuloParaVender extends Articulo {
    //La cantidad es distinta a al stock ya que se refiere a la cantidad que se esta vendiendo
    private Integer cantidad;

    public ArticuloParaVender(Integer id, String codigo, String nombre, Float precioVenta, Integer stock, Integer cantidad) {
        super(id, codigo, nombre, precioVenta, stock);
        this.cantidad = cantidad;
    }

    public void aumentarCantidad() {
        this.cantidad++;
    }

    public Integer getCantidad() {
        return cantidad;
    }
    public Float getTotal() {
        return this.getPrecioVenta() * this.cantidad;
    }
}
