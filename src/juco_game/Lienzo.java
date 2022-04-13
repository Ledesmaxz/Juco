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
import Elements.mapa;
import Elements.no_fatal;
import Elements.player;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                if (actual instanceof fatal){
                 if(((fatal)actual).isHorizontal()){
                     mover_horizontal(((fatal)actual));
                 }else{
                     mover_vertical(((fatal)actual));
                 }   
                }
                    if (actual instanceof player){
                        verificar_no_fatal_choque((player)actual);
                        System.out.println(((player) actual).getNivel_aire());
                            if(((player) actual).getNivel_aire() == 0){
                                reset_player((player)actual);
                            }

                    }
                actual.actualizarArea();
            }
            esperar(25);
            repaint();
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
                if(jugador.getArea().intersects(this.miMapa.getMisElementos().get(i).getArea())){
                respuesta = true;
                System.out.println("choque ");
                }
            }
            i++;
        }
        return respuesta;
    }
public boolean verificar_no_fatal_choque(player jugador){
    boolean respuesta = false;
        //System.out.println(jugador.getArea());
        int i=0;
        while (i<this.miMapa.getMisElementos().size() && !respuesta) {
            if(this.miMapa.getMisElementos().get(i) instanceof no_fatal){
                if(jugador.getArea().intersects(this.miMapa.getMisElementos().get(i).getArea())){
                respuesta = true;
                int daño = ((no_fatal)this.miMapa.getMisElementos().get(i)).getCantidad_daño();
                jugador.setNivel_aire(jugador.getNivel_aire()-daño);
                }
            }
            i++;
        }
        return respuesta;
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
