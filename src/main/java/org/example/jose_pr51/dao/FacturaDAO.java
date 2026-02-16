package org.example.jose_pr51.dao;

import java.sql.*;

public class FacturaDAO {

    public int crearFacturaCompleta(int idCliente, String fechaEmision, String periodoInicio, String periodoFin,
                                    double cPunta, double pPunta,
                                    double cLlano, double pLlano,
                                    double cValle, double pValle) {

        String sqlFactura = "INSERT INTO FACTURA(id_cliente, fecha_emision, periodo_inicio, periodo_fin) VALUES(?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO DETALLE_FACTURA(id_factura, tramo, consumo_kwh, precio_kwh) VALUES(?, ?, ?, ?)";

        int idFacturaGenerada = -1;

        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, idCliente);
            pstmt.setString(2, fechaEmision);
            pstmt.setString(3, periodoInicio);
            pstmt.setString(4, periodoFin);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idFacturaGenerada = rs.getInt(1);
            }

            if (idFacturaGenerada != -1) {
                insertarDetalle(conn, sqlDetalle, idFacturaGenerada, "punta", cPunta, pPunta);
                insertarDetalle(conn, sqlDetalle, idFacturaGenerada, "llano", cLlano, pLlano);
                insertarDetalle(conn, sqlDetalle, idFacturaGenerada, "valle", cValle, pValle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idFacturaGenerada;
    }

    private void insertarDetalle(Connection conn, String sql, int idFactura, String tramo, double consumo, double precio) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFactura);
            pstmt.setString(2, tramo);
            pstmt.setDouble(3, consumo);
            pstmt.setDouble(4, precio);
            pstmt.executeUpdate();
        }
    }
}