package org.example.jose_pr51.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.example.jose_pr51.dao.ClienteDAO;
import org.example.jose_pr51.dao.FacturaDAO;
import org.example.jose_pr51.dao.ConexionDB;
import org.example.jose_pr51.model.Cliente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Tooltip;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;

import javafx.util.Duration;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Control;

public class MainController {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private FacturaDAO facturaDAO = new FacturaDAO();
    private ObservableList<Cliente> listaClientes;

    @FXML private TextField txtNombre, txtApellidos, txtNif;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombre, colApellidos, colNif;

    @FXML private ComboBox<Cliente> comboClientesFactura;
    @FXML private DatePicker dpEmision, dpInicio, dpFin;
    @FXML private TextField txtConsumoPunta, txtPrecioPunta;
    @FXML private TextField txtConsumoLlano, txtPrecioLlano;
    @FXML private TextField txtConsumoValle, txtPrecioValle;

    @FXML private TextField txtIdFactura;

    @FXML private BarChart<String, Number> barChartAnalisis;

    @FXML private ComboBox<Cliente> comboClientesImprimir;
    @FXML private ComboBox<FacturaResumen> comboFacturasImprimir;

    @FXML private WebView webViewAyuda;
    @FXML private WebView webViewManual;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colNif.setCellValueFactory(new PropertyValueFactory<>("nif"));

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNombre.setText(newSel.getNombre());
                txtApellidos.setText(newSel.getApellidos());
                txtNif.setText(newSel.getNif());
            }
        });

        comboClientesFactura.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente c) {
                return (c == null) ? null : c.getNombre() + " " + c.getApellidos();
            }
            @Override
            public Cliente fromString(String string) { return null; }
        });

        comboClientesImprimir.setConverter(new StringConverter<Cliente>() {
            @Override public String toString(Cliente c) { return (c == null) ? null : c.getNombre() + " " + c.getApellidos(); }
            @Override public Cliente fromString(String s) { return null; }
        });

        cargarDatos();
        comboClientesImprimir.setItems(listaClientes);

        comboClientesImprimir.setOnAction(e -> {
            Cliente seleccionado = comboClientesImprimir.getValue();
            if (seleccionado != null) {
                cargarFacturasDeCliente(seleccionado.getId());
                comboFacturasImprimir.setDisable(false);
            } else {
                comboFacturasImprimir.getItems().clear();
                comboFacturasImprimir.setDisable(true);
            }
        });

        dpEmision.setValue(LocalDate.now());
        dpInicio.setValue(LocalDate.now().minusMonths(1));
        dpFin.setValue(LocalDate.now());

        configurarTooltip(txtNombre, "Introduce el nombre del cliente.");
        configurarTooltip(txtApellidos, "Introduce los apellidos del cliente.");
        configurarTooltip(txtNif, "Introduce el NIF (8 números y letra).");
        configurarTooltip(comboClientesFactura, "Selecciona el cliente para la factura.");
        configurarTooltip(txtConsumoPunta, "Consumo en periodo Punta (kWh).");
        configurarTooltip(txtConsumoLlano, "Consumo en periodo Llano (kWh).");
        configurarTooltip(txtConsumoValle, "Consumo en periodo Valle (kWh).");

        if (webViewAyuda != null) {
            try {
                WebEngine webEngine = webViewAyuda.getEngine();
                URL url = getClass().getResource("/ayuda.html");
                if (url != null) webEngine.load(url.toExternalForm());
            } catch (Exception e) { e.printStackTrace(); }
        }

        // Cargar Manual de Usuario (manual.html) - NUEVO BLOQUE
        if (webViewManual != null) {
            try {
                WebEngine webEngine = webViewManual.getEngine();
                URL url = getClass().getResource("/manual.html");
                if (url != null) webEngine.load(url.toExternalForm());
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void configurarTooltip(javafx.scene.control.Control control, String texto) {
        Tooltip tooltip = new Tooltip(texto);
        tooltip.setShowDelay(Duration.seconds(0.5));
        control.setTooltip(tooltip);
    }

    private void cargarDatos() {
        listaClientes = FXCollections.observableArrayList(clienteDAO.obtenerTodos());
        tablaClientes.setItems(listaClientes);
        comboClientesFactura.setItems(listaClientes);
        tablaClientes.refresh();
    }

    @FXML
    protected void onGuardarClienteClick() {
        if (!txtNombre.getText().isEmpty() && !txtNif.getText().isEmpty()) {
            clienteDAO.insertar(new Cliente(0, txtNombre.getText(), txtApellidos.getText(), txtNif.getText()));
            cargarDatos();
            limpiarFormularioCliente();
        }
    }

    @FXML
    protected void onModificarClienteClick() {
        Cliente sel = tablaClientes.getSelectionModel().getSelectedItem();
        if (sel != null) {
            clienteDAO.actualizar(new Cliente(sel.getId(), txtNombre.getText(), txtApellidos.getText(), txtNif.getText()));
            cargarDatos();
            limpiarFormularioCliente();
        }
    }

    @FXML
    protected void onEliminarClienteClick() {
        Cliente sel = tablaClientes.getSelectionModel().getSelectedItem();
        if (sel != null) {
            clienteDAO.eliminar(sel.getId());
            cargarDatos();
            limpiarFormularioCliente();
        }
    }

    private void limpiarFormularioCliente() {
        txtNombre.clear(); txtApellidos.clear(); txtNif.clear();
        tablaClientes.getSelectionModel().clearSelection();
    }

    @FXML
    protected void onGuardarFacturaClick() {
        Cliente cliente = comboClientesFactura.getValue();
        if (cliente == null) {
            mostrarAlerta("Error", "Debes seleccionar un cliente del desplegable.");
            return;
        }

        try {
            int nuevoId = facturaDAO.crearFacturaCompleta(
                    cliente.getId(),
                    dpEmision.getValue().toString(),
                    dpInicio.getValue().toString(),
                    dpFin.getValue().toString(),
                    Double.parseDouble(txtConsumoPunta.getText()), Double.parseDouble(txtPrecioPunta.getText()),
                    Double.parseDouble(txtConsumoLlano.getText()), Double.parseDouble(txtPrecioLlano.getText()),
                    Double.parseDouble(txtConsumoValle.getText()), Double.parseDouble(txtPrecioValle.getText())
            );

            if (nuevoId != -1) {
                mostrarAlerta("Éxito", "Factura creada correctamente. ID: " + nuevoId);
                txtIdFactura.setText(String.valueOf(nuevoId));
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Revisa que los consumos y precios sean números (usa punto para decimales).");
        }
    }

    @FXML
    protected void onGenerarInformeClick() {
        FacturaResumen facturaSeleccionada = comboFacturasImprimir.getValue();

        if (facturaSeleccionada == null) {
            mostrarAlerta("Error", "Por favor, selecciona una factura del desplegable.");
            return;
        }

        int idFactura = facturaSeleccionada.id;

        try {
            Connection conn = ConexionDB.conectar();

            String nombreArchivo = "Factura_" + idFactura + ".pdf";
            Cliente cli = comboClientesImprimir.getValue();
            if (cli != null) {
                String clienteLimpio = (cli.getNombre() + "_" + cli.getApellidos()).replaceAll("[^a-zA-Z0-9]", "_");
                String fechaLimpia = facturaSeleccionada.fecha.replaceAll("[^a-zA-Z0-9.-]", "_");
                nombreArchivo = "Factura_" + clienteLimpio + "_" + fechaLimpia + ".pdf";
            }

            Map<String, Object> params = new HashMap<>();
            params.put("ParametroIDFactura", idFactura);

            JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/informes/FacturaClientes.jrxml"));
            JasperPrint print = JasperFillManager.fillReport(reporte, params, conn);

            JasperViewer.viewReport(print, false);
            JasperExportManager.exportReportToPdfFile(print, nombreArchivo);

            mostrarAlerta("Éxito", "Factura generada: " + nombreArchivo);

            if (conn != null) conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Fallo al generar PDF: " + e.getMessage());
        }
    }

    @FXML
    protected void onCargarAnalisisClick() {
        barChartAnalisis.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Consumo Medio");
        try {
            Connection conn = ConexionDB.conectar();
            ResultSet rs = conn.createStatement().executeQuery("SELECT tramo, AVG(consumo_kwh) as media FROM DETALLE_FACTURA GROUP BY tramo");
            while (rs.next()) series.getData().add(new XYChart.Data<>(rs.getString("tramo"), rs.getDouble("media")));
            barChartAnalisis.getData().add(series);
            if (conn != null) conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarFacturasDeCliente(int idCliente) {
        ObservableList<FacturaResumen> listaFacturas = FXCollections.observableArrayList();
        String sql = "SELECT f.id_factura, f.fecha_emision, SUM(d.consumo_kwh * d.precio_kwh) as total " +
                "FROM FACTURA f " +
                "LEFT JOIN DETALLE_FACTURA d ON f.id_factura = d.id_factura " +
                "WHERE f.id_cliente = " + idCliente + " " +
                "GROUP BY f.id_factura ORDER BY f.fecha_emision DESC";

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaFacturas.add(new FacturaResumen(
                        rs.getInt("id_factura"),
                        rs.getString("fecha_emision"),
                        rs.getDouble("total")
                ));
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        comboFacturasImprimir.setItems(listaFacturas);
    }

    public static class FacturaResumen {
        int id;
        String fecha;
        double total;

        public FacturaResumen(int id, String fecha, double total) {
            this.id = id;
            this.fecha = fecha;
            this.total = total;
        }

        @Override
        public String toString() {
            return String.format("Fac #%d | %s | %.2f €", id, fecha, total);
        }
    }




}