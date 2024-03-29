/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juco_game;

import Elements.Images;
import Elements.barrier;
import Elements.element;
import Elements.fatal;
import Elements.healing;
import Elements.key;
import Elements.mapa;
import Elements.no_fatal;
import Elements.player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

/**
 *
 * @author L E D E S M A
 */
public class Lienzo extends javax.swing.JPanel implements Runnable {
    private mapa miMapa;
    private boolean activo;
    /**
     * Creates new form Lienxo
     */
    public Lienzo() {
        initComponents();
        this.activo = false;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DibujarFigura(g);
        DibujarVida(g);
    }
    public void DibujarVida(Graphics g){
        int posicion = 30;
            for(element ActualE :this.getMiMapa().getMisElementos()){
                if(ActualE instanceof player){
                    String nombre = ""+((player)ActualE).getId();
                    String vida = ""+((player)ActualE).getNivel_aire();
                    g.setColor(Color.BLUE);
                    g.drawString(""+nombre,830,posicion );
                    posicion+=20;
                    g.drawString("VIDA: "+vida,830,posicion );
                    posicion +=440;
                }
        }
    }
    
    public void DibujarFigura(Graphics g){
            for(element ActualE :this.getMiMapa().getMisElementos()){
                if(ActualE instanceof barrier){
                    Dibujarbarrier(g,(barrier)ActualE);
                }else if(ActualE instanceof Images){
                    dibujarImagen(g,(Images)ActualE);
                }
        }
    }
    public void Dibujarbarrier(Graphics g,barrier elemento){
        g.setColor(elemento.getColor());
        g.fillRect(elemento.getX(),elemento.getY(),elemento.getAncho(),elemento.getAlto());
        
        g.drawRect(elemento.getX(),elemento.getY(),elemento.getAncho(),elemento.getAlto());
        
    }
    public void dibujarImagen(Graphics g,Images laimagen){
    Toolkit t = Toolkit.getDefaultToolkit ();
    Image imagen = t.getImage (laimagen.getRuta());
    g.drawImage(imagen,
                laimagen.getX(),
                laimagen.getY(),
                laimagen.getAncho(),
                laimagen.getAlto(),
                this);
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @return the miMapa
     */
    public mapa getMiMapa() {
        return miMapa;
    }

    /**
     * @param miMapa the miMapa to set
     */
    public void setMiMapa(mapa miMapa) {
        this.miMapa = miMapa;
    }

    /**
     * @return the activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public void run() {
        while(this.activo){
            //Mover todas las figuras
            for(element actual: this.miMapa.getMisElementos()){
                 if (actual instanceof player){
                    if(!veriificarColisiones((player)actual)){
                        verificar_no_fatal_choque((player)actual);
                        if((((player) actual).getNivel_aire() == 0 )||(verificar_fatal_choque((player)actual))){
                            reset_player((player)actual);
                        }
                        if(verificar_healing_choque((player)actual)){
                            int aire = ((player)actual).getNivel_aire();
                            ((player)actual).setNivel_aire(aire+100);
                        }
                        verificar_key_choque((player)actual);
                        }
                        if (verificar_meta((player)actual)){
                            this.activo = false;
                            JOptionPane. showMessageDialog(this, "EL GANADOR ES: "+((player)actual).getId());
                        }
                    } 
                    if (actual instanceof fatal){
                        if(((fatal)actual).getX()>425 && ((fatal)actual).getX()<600){
                            if(((fatal)actual).getY()<250){
                                 mover_proteccion_llave_arriba((fatal)actual);
                            }else{
                                mover_proteccion_llave_abajo((fatal)actual);
                            }
                            
                        }else{
                        if (((fatal)actual).getX()>600){
                            if (((fatal)actual).getY()<265){
                                mover_balas_arriba((fatal)actual);
                            } else {
                                mover_balas_abajo((fatal)actual);
                            }
                        }else{
                            if (((fatal)actual).getY()<250){
                            if(((fatal)actual).isHorizontal()){
                                mover_horizontal(((fatal)actual));
                            }else{
                                mover_vertical(((fatal)actual));
                            } 
                        } else {
                            if (((fatal)actual).getId().equals("horizontal")){
                                moverX((fatal)actual);
                                validar_fronteras_horizontales((fatal)actual);
                            } else {
                                moverY((fatal)actual);
                                validar_fronteras_verticales((fatal)actual);
                            }
                        }
                        }
                        }
                    }
                actual.actualizarArea();
            }
            esperar(25);
            repaint();
        }
    }
    public void mover_balas_arriba(fatal actual){
        if (actual.getId().equals("primera")){
            if (actual.getY()<=230){
                actual.setY(actual.getY()+1);
            } else if (actual.getY()>230){
                actual.setY(25);
            }
        } else if (actual.getId().equals("segunda")){
            if (actual.getY()<=230){
                actual.setY(actual.getY()+2);
            } else if (actual.getY()>230){
                actual.setY(25);
            }
        } else if (actual.getId().equals("tercera")){
            if (actual.getY()<=230){
                actual.setY(actual.getY()+3);
            } else if (actual.getY()>230){
                actual.setY(25);
            }
        } else {
            if (actual.getY()<=230){
                actual.setY(actual.getY()+1);
            } else if (actual.getY()>230){
                actual.setY(25);
            }
        }
    }
    public boolean verificar_meta(player jugador){
        boolean respuesta = false;
        int i=0;
        while (i<this.miMapa.getMisElementos().size() && !respuesta) {
            if(this.miMapa.getMisElementos().get(i) instanceof barrier){
                //System.out.println(this.miMapa.getMisElementos().get(i).getArea());
                if((jugador.getArea().intersects(this.miMapa.getMisElementos().get(i).getArea()) &&(this.miMapa.getMisElementos().get(i).getId().equals("meta")) )){
                respuesta = true;
                this.activo = false;
                }
            }
            i++;
        }
        return respuesta;
    }
    public void mover_balas_abajo(fatal actual){
        if (actual.getId().equals("primera")){
            if (actual.getY()<=475){
                actual.setY(actual.getY()+1);
            } else if (actual.getY()>475){
                actual.setY(270);
            }
        } else if (actual.getId().equals("segunda")){
            if (actual.getY()<=475){
                actual.setY(actual.getY()+2);
            } else if (actual.getY()>475){
                actual.setY(270);
            }
        } else if (actual.getId().equals("tercera")){
            if (actual.getY()<=475){
                actual.setY(actual.getY()+3);
            } else if (actual.getY()>475){
                actual.setY(270);
            }
        } else {
            if (actual.getY()<=475){
                actual.setY(actual.getY()+1);
            } else if (actual.getY()>475){
                actual.setY(270);
            }
        }
    }
    public void mover_proteccion_llave_arriba(fatal actual){
       if(actual.getX() >= 445 && actual.getY() <= 95){
           actual.setHorizontal(true);
           moverX(actual);
       }
       
        if(actual.getX() > 445 && actual.getY() == 170){
           actual.setHorizontal(false);
           moverX(actual);
       }
        
        if(actual.getX() >= 545 && actual.getY() >= 95){
           actual.setVertical(true);
           moverY(actual);
       }
        if(actual.getX() == 445 && actual.getY() <= 170){
           actual.setVertical(false);
           moverY(actual);
       }
    }
    public void mover_proteccion_llave_abajo(fatal actual){
       if(actual.getX() >= 445 && actual.getY() <= 343){
           actual.setHorizontal(true);
           moverX(actual);
       }
       
        if(actual.getX() > 445 && actual.getY() == 418){
           actual.setHorizontal(false);
           moverX(actual);
       }
        
        if(actual.getX() >= 545 && actual.getY() >= 343){
           actual.setVertical(true);
           moverY(actual);
       }
        if(actual.getX() == 445 && actual.getY() <= 418){
           actual.setVertical(false);
           moverY(actual);
       }
    }

    public void moverX(fatal actual){
        if (actual.isHorizontal()) {
            actual.setX(actual.getX()+1);
        }else{
            actual.setX(actual.getX()-1);
        }
    }
    public void moverY(fatal actual){
        if (actual.isVertical()) {
            actual.setY(actual.getY()+1);
        }else{
            actual.setY(actual.getY()-1);
        }
    }
    public void validar_fronteras_horizontales(fatal actual){
        if(actual.getX()>=420){
            actual.setHorizontal(false);
        }else if(actual.getX()<=280){
            actual.setHorizontal(true);
        }   
    }
    public void validar_fronteras_verticales(fatal actual){
        if(actual.getY()<=308){
            actual.setVertical(true);
        }else if(actual.getY()>=435){
            actual.setVertical(false);
        }
    }
    public void reset_player(player actual){
        actual.setNivel_aire(200);
        actual.setX(actual.getRespawn()[0]);
        actual.setY(actual.getRespawn()[1]);
    }
    public void mover_vertical(fatal elemento){
        if(elemento.isAdelante()){
            if(elemento.getY()>=190){
              elemento.setAdelante(false);
            }else{
              elemento.setY(elemento.getY()+1);
        }
        }else{
            if(elemento.getY()<= 65){
                elemento.setAdelante(true);
            }else{
                elemento.setY(elemento.getY()-1);
            }
        }
    }
    public void mover_horizontal(fatal elemento){
        if(elemento.isAdelante()){
            if(elemento.getX()>=420){
              elemento.setAdelante(false);
            }else{
              elemento.setX(elemento.getX()+1);
        }
        }else{
            if(elemento.getX()<= 280){
                elemento.setAdelante(true);
            }else{
                elemento.setX(elemento.getX()-1);
            }
        }
    }
    
    public void esperar(int milisegundos){
        try {
                Thread.sleep(milisegundos);
            } catch (InterruptedException ex) {
                Logger.getLogger(Lienzo.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    public boolean veriificarColisiones(player jugador){
        boolean respuesta = false;
        //System.out.println(jugador.getArea());
        int i=0;
        while (i<this.miMapa.getMisElementos().size() && !respuesta) {
            if(this.miMapa.getMisElementos().get(i) instanceof barrier){
                //System.out.println(this.miMapa.getMisElementos().get(i).getArea());
                if((jugador.getArea().intersects(this.miMapa.getMisElementos().get(i).getArea()) &&(!this.miMapa.getMisElementos().get(i).getId().equals("meta")) )){
                respuesta = true;
                    reset_player(jugador);
                }
            }
            i++;
        }
        return respuesta;
    }
    public void sound (String Ruta){
        try{
        Clip  sonido;
        sonido = AudioSystem.getClip();
        sonido.open(AudioSystem.getAudioInputStream(new File(Ruta)));
        sonido.start();
        } catch(Exception e){
            System.out.println("Error sonido");
        }                
    }
    public boolean verificar_no_fatal_choque(player jugador){
        boolean respuesta = false;
        int i=0;
        while (i<this.miMapa.getMisElementos().size() && !respuesta) {
            if(this.miMapa.getMisElementos().get(i) instanceof no_fatal){
                if(jugador.getArea().intersects(this.miMapa.getMisElementos().get(i).getArea())){
                respuesta = true;
                int daño = ((no_fatal)this.miMapa.getMisElementos().get(i)).getCantidad_daño();
                jugador.setNivel_aire(jugador.getNivel_aire()-daño);
                sound("src/sounds/daño.wav");
                }
            }
            i++;
        }
        return respuesta;
}   
    public boolean verificar_fatal_choque(player jugador){
        boolean respuesta = false;
        int i=0;
        while (i<this.miMapa.getMisElementos().size() && !respuesta) {
            if(this.miMapa.getMisElementos().get(i) instanceof fatal){
                if(jugador.getArea().intersects(this.miMapa.getMisElementos().get(i).getArea())){
                    respuesta = true;
                }
            }
            i++;
        }
        return respuesta;
}
    public boolean verificar_healing_choque(player jugador){
        boolean respuesta = false;
        int i=0;
        int salud =-1;
        while (i<this.miMapa.getMisElementos().size() && !respuesta) {
            if(this.miMapa.getMisElementos().get(i) instanceof healing){
                if(jugador.getArea().intersects(this.miMapa.getMisElementos().get(i).getArea())){
                    respuesta = true;
                    sound("src/sounds/curacion.wav");
                    salud = i;
                }
            }
            i++;
        }
        if (salud != -1){
            this.miMapa.getMisElementos().remove(salud);
        }
        return respuesta;
    }
    public void verificar_key_choque(player jugador){
        boolean respuesta = false;
        int i=0;
        int keyy =-1;
        while (i<this.miMapa.getMisElementos().size() && !respuesta) {
            if(this.miMapa.getMisElementos().get(i) instanceof key){
                if(jugador.getArea().intersects(this.miMapa.getMisElementos().get(i).getArea())){
                    respuesta = true;
                    keyy = i;
                    if (((key)this.miMapa.getMisElementos().get(i)).getY()<200){
                        borrarBarreraArriba();
                    } else if (((key)this.miMapa.getMisElementos().get(i)).getY()>200) {
                        borrarBarreraAbajo();
                    }
                }
            }
            i++;
        }
        if (keyy != -1){
            this.miMapa.getMisElementos().remove(keyy);
        }
    }
    
    public void borrarBarreraArriba(){
       boolean bandera = false;
        int i=0;
        while (i<this.miMapa.getMisElementos().size() && !bandera) {
            if(this.miMapa.getMisElementos().get(i) instanceof barrier){
                if(this.miMapa.getMisElementos().get(i).getId().equals("door_up")){
                    bandera = true;
                    this.miMapa.getMisElementos().get(i).setY(150);
                }
            }
            i++;
        } 
    }
    
    public void borrarBarreraAbajo(){
       boolean bandera = false;
        int i=0;
        while (i<this.miMapa.getMisElementos().size() && !bandera) {
            if(this.miMapa.getMisElementos().get(i) instanceof barrier){
                if(this.miMapa.getMisElementos().get(i).getId().equals("door_down")){
                    //bandera = true;
                    this.miMapa.getMisElementos().get(i).setY(400);
                    break;
                }
            }
            i++;
        } 
    }
    
    public void start(){
        this.activo = true;
        new Thread(this).start();
    }
    
    public void detener(){
        this.activo = false;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
