/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package formulario;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Alumno
 */
public class ventanaFormulario extends JFrame implements ActionListener {
    
    private final JLabel etiquetaNombre, etiquetaApellidoP, etiquetaApellidoM, etiquetaSexo, etiquetaPregunta;
    private final JTextField campoNombre, campoApellidoP, campoApellidoM; 
    private final JComboBox seleccionarSexo;
    private final JRadioButton opcion1, opcion2;
    private final ButtonGroup pregunta;
    private JButton botonEnviar;
    private final Container contenedor;
    
    public ventanaFormulario(){
        contenedor=getContentPane();
        contenedor.setLayout(null);
        setTitle("Formulario");
        setSize(250, 360);
        setLocationRelativeTo(null);
        setResizable(false);
        
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
        
        etiquetaSexo = new JLabel("Sexo");
        etiquetaSexo.setBounds(20,170,210,20);
        contenedor.add(etiquetaSexo);
        
        seleccionarSexo = new JComboBox();
        seleccionarSexo.setBounds(20,190,210,35);
        contenedor.add(seleccionarSexo);
        seleccionarSexo.addItem("Masculino");
        seleccionarSexo.addItem("Femenino");
        seleccionarSexo.addItem("Otro");
        
        etiquetaPregunta = new JLabel("¿En serio quieres enviar estos datos?");
        etiquetaPregunta.setBounds(20,230,210,20);
        contenedor.add(etiquetaPregunta);
        
        pregunta = new ButtonGroup();
        opcion1 = new JRadioButton("Si");
        opcion1.setBounds(20,250,80,20);
        pregunta.add(opcion1);
        contenedor.add(opcion1);
        opcion2 = new JRadioButton("No");
        opcion2.setBounds(100,250,80,20);
        pregunta.add(opcion2);
        contenedor.add(opcion2);
        
        botonEnviar = new JButton("Enviar Datos");
        botonEnviar.setBounds(50,280,150,35);
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
            String sexo = seleccionarSexo.getSelectedItem().toString();
            String respuesta = campoNombre.getText();
            String texto = "Nombre: " + nombre + " \nApellidos: " + aPaterno + " " + aMaterno + "\nSexo :" + sexo + "\nRespuesta: " + respuesta;
            
            JOptionPane.showMessageDialog(contenedor,texto,"Despliegue de datos",JOptionPane.WARNING_MESSAGE);
        }
    }
    
}
