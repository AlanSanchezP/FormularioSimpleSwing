package formulario;

//Librerias Swing
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

//Librerias SQL
import java.sql.ResultSet;
import java.sql.SQLException;

//Librerias para el XML
import java.io.FileWriter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ventanaFormulario extends JFrame implements ActionListener {
    
    //Declarar variables
    private final JLabel etiquetaNombre, etiquetaApellidoP, etiquetaApellidoM, etiquetaSexo, etiquetaEmail;
    private final JTextField campoNombre, campoApellidoP, campoApellidoM, campoEmail; 
    private final JComboBox seleccionarSexo;
    private final JButton botonEnviar;
    private final Container contenedor;
    private crear_conexion conexion;
    
    public ventanaFormulario(){
        //Inicializar propiedades del JFrame
        contenedor=getContentPane();
        contenedor.setLayout(null);
        setTitle("Formulario Datos Personales");
        setSize(250, 360);
        setLocationRelativeTo(null);
        setResizable(false);
        
        //Inicializar variables e indicar coordenadas elementos Swing
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
        
        //Llenar JComboBox desde la base de datos
        try{
            conexion.conectar();
            ResultSet rsSexo = conexion.consulta("call sp_TraeGeneros()");
            System.out.println("Llenado exitoso de JComboBox.");
            
            while(rsSexo.next()){
                seleccionarSexo.addItem(rsSexo.getString("genero"));
            }
            //Cerrar conexion
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
        //Capturar clic en el boton
        if(e.getSource().equals(botonEnviar)){
            //Obtener datos de los campos
            String nombre = campoNombre.getText();
            String aPaterno = campoApellidoP.getText();
            String aMaterno = campoApellidoM.getText();
            int sexoID = seleccionarSexo.getSelectedIndex() + 1;
            String sexo = seleccionarSexo.getSelectedItem().toString();
            String email = campoEmail.getText();
            String usuarioID = "";
            
            //Validar campos vacios
            if(nombre.equals("") || aPaterno.equals("") || aMaterno.equals("") || email.equals("")){
                JOptionPane.showMessageDialog(contenedor,"Llena primero todos los campos.","Aviso",JOptionPane.WARNING_MESSAGE);
            }
            else{
                //Ingresar datos en la base
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
                            usuarioID = mensaje;
                            mensaje = "Registro exitoso. \nID de usuario registrado: " + usuarioID;
                            //Vaciar campos
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
                try{
                    //Crear elemento raiz del XML
                    Element elemento = new Element ("Datos-Usuario");
                    elemento.detach();
                    Document doc = new Document(elemento);
                    
                    //Creación de elementos en el XML
                    Element idElement= new Element("id-usuario");
                    idElement.setText(usuarioID);
                    doc.getRootElement().addContent(idElement);
                    
                    Element nombreElement= new Element("nombre");
                    nombreElement.setText(nombre);
                    doc.getRootElement().addContent(nombreElement);

                    Element apellidosElement= new Element("apellidos");
                    apellidosElement.addContent(new Element("apellido-paterno").setText(aPaterno));
                    apellidosElement.addContent(new Element("apellido-materno").setText(aMaterno));
                    doc.getRootElement().addContent(apellidosElement);
                    
                    Element emailElement= new Element("email");
                    emailElement.setText(email);
                    doc.getRootElement().addContent(emailElement);

                    Element sexoElement= new Element("sexo");
                    sexoElement.addContent(new Element("sexo-id").setText(sexoID + ""));
                    sexoElement.addContent(new Element("sexo-descripcion").setText(sexo));
                    doc.getRootElement().addContent(sexoElement);
                    
                    //Guardar el archivo XML
                    XMLOutputter xmlOutput = new XMLOutputter();

                    xmlOutput.setFormat(Format.getPrettyFormat());
                    xmlOutput.output(doc, new FileWriter("archivosXML/DatosUsuario_" + usuarioID +".xml"));

                    System.out.println("Archivo XML creado exitosamente.");

                } catch (Exception io) {
                    System.out.println(io.getMessage());
                }
            }
        }
    }
    
}
