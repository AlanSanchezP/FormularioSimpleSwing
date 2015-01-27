/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package formulario;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Alumno
 */
public class ventanaFormulario extends JFrame implements ActionListener {
    
    private final JLabel etiquetaNombre, etiquetaApellidoP, etiquetaApellidoM, etiquetaSexo, etiquetaEmail;
    private final JTextField campoNombre, campoApellidoP, campoApellidoM, campoEmail; 
    private final JComboBox seleccionarSexo;
    private JButton botonEnviar;
    private final Container contenedor;
    private crear_conexion conexion;
    
    public ventanaFormulario(){
        contenedor=getContentPane();
        contenedor.setLayout(null);
        setTitle("Formulario Datos Personales");
        setSize(250, 360);
        setLocationRelativeTo(null);
        setResizable(false);
        
        conexion = new crear_conexion();
        
        etiquetaNombre = new JLabel("Nombre");
        etiquetaNombre.setBounds(20,5,210,20);
        contenedor.add(etiquetaNombre);
        
        campoNombre = new JTextField();
        campoNombre.setBounds(20,25,210,35);
        contenedor.add(campoNombre);
        
        etiquetaApellidoP = new JLabel("Apellido Paterno");
        etiquetaApellidoP.setBounds(20,60,210,20);
        contenedor.add(etiquetaApellidoP);
        
        campoApellidoP = new JTextField();
        campoApellidoP.setBounds(20,80,210,35);
        contenedor.add(campoApellidoP);
        
        etiquetaApellidoM = new JLabel("Apellido Materno");
        etiquetaApellidoM.setBounds(20,115,210,20);
        contenedor.add(etiquetaApellidoM);
        
        campoApellidoM = new JTextField();
        campoApellidoM.setBounds(20,135,210,35);
        contenedor.add(campoApellidoM);
        
        etiquetaEmail = new JLabel("Email");
        etiquetaEmail.setBounds(20,170,210,20);
        contenedor.add(etiquetaEmail);
        
        campoEmail = new JTextField();
        campoEmail.setBounds(20,190,210,35);
        contenedor.add(campoEmail);
        
        etiquetaSexo = new JLabel("Sexo");
        etiquetaSexo.setBounds(20,225,210,20);
        contenedor.add(etiquetaSexo);
        
        seleccionarSexo = new JComboBox();
        seleccionarSexo.setBounds(20,245,210,35);
        contenedor.add(seleccionarSexo);
        
        try{
            conexion.conectar();
            ResultSet rsSexo = conexion.consulta("call sp_TraeGeneros()");
            System.out.println("Traidos Los Generos Correctamente");
            
            while(rsSexo.next()){
                seleccionarSexo.addItem(rsSexo.getString("genero"));
            }
            rsSexo.close();
            conexion.cerrar();

        } catch(SQLException exx){
            System.out.println(exx);
        }
        
        botonEnviar = new JButton("Enviar Datos");
        botonEnviar.setBounds(50,290,150,35);
        contenedor.add(botonEnviar);
        
        botonEnviar.addActionListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) throws UnsupportedOperationException{ 
        if(e.getSource().equals(botonEnviar)){
            String nombre = campoNombre.getText();
            String aPaterno = campoApellidoP.getText();
            String aMaterno = campoApellidoM.getText();
            int sexoID = seleccionarSexo.getSelectedIndex() + 1;
            String email = campoEmail.getText();
            try{
                conexion.conectar();
                ResultSet rsSexo = conexion.consulta("call sp_CreaUsuario('" + nombre + "', '" +
                                                                               aPaterno + "', '" + 
                                                                               aMaterno + "', " + 
                                                                               sexoID + ", '" + 
                                                                               email + "')");
                
                if(rsSexo.next()){
                    String mensaje = rsSexo.getString("Mensaje");
                    if(!mensaje.equals("Operación cancelada. Email registrado previamente")){
                        mensaje = "Registro exitoso. ID de usuario registrado: " + mensaje;
                        campoNombre.setText("");
                        campoApellidoP.setText("");
                        campoApellidoM.setText("");
                        seleccionarSexo.setSelectedIndex(0);
                        campoEmail.setText("");
                    }
                    JOptionPane.showMessageDialog(contenedor,mensaje,"Aviso",JOptionPane.WARNING_MESSAGE);
                }
                rsSexo.close();
                conexion.cerrar();

            } catch(SQLException exx){
                System.out.println(exx);
            }
        }
    }
    
}
