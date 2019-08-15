package scripts;

import javax.swing.*;

import static java.awt.EventQueue.invokeLater;
import javax.swing.*;


public class iCUERO extends javax.swing.JFrame {


    public iCUERO() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        System.out.println("Creando la Interfase Inicializando componentes");
        Tex1 = new javax.swing.JLabel();
        LeatherC = new javax.swing.JComboBox();
        Tex2 = new javax.swing.JLabel();
        EnergyC = new javax.swing.JComboBox();
        Tex3 = new javax.swing.JLabel();
        EnergyL = new javax.swing.JSlider();
        AceptarB = new javax.swing.JButton();
        CancelarB = new javax.swing.JButton();

        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Tex1.setText("Leather:");

        LeatherC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Green Dragon", "Blue Dragon", "Red Dragon", "Black Dragon" }));

        Tex2.setText("Energy:");

        EnergyC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Energy Potion", "S. Energy Potion", "Stamine Potion" }));

        Tex3.setText("Use Energy:");

        EnergyL.setMajorTickSpacing(25);
        EnergyL.setMinorTickSpacing(5);
        EnergyL.setPaintLabels(true);
        EnergyL.setPaintTicks(true);

        AceptarB.setText("Aceptar");
        AceptarB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AceptarBActionPerformed(evt);
            }
        });

        CancelarB.setText("Cancelar");
        CancelarB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(Tex2)
                                                        .addComponent(Tex1))
                                                .addGap(29, 29, 29)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(LeatherC, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(EnergyC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(Tex3)
                                                .addGap(18, 18, 18)
                                                .addComponent(EnergyL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(34, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(AceptarB)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CancelarB)
                                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(Tex1)
                                        .addComponent(LeatherC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Tex2)
                                        .addComponent(EnergyC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(EnergyL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Tex3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(AceptarB)
                                        .addComponent(CancelarB))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void AceptarBActionPerformed(java.awt.event.ActionEvent evt) {
        // Aqui va el codigo para el bottom Aceptar
        Leather= LeatherC.getSelectedIndex();
        StaminaLv= EnergyL.getValue();
        Energy=EnergyC.getSelectedIndex();

        System.out.println("Valor de: " + "Cuero es: " + Leather);
        System.out.println("Valor de: " + "StaminaLV es: " + StaminaLv);
        System.out.println("Valor de: " + "Energy es: " + Energy);
         Aceptar=true;
    }
boolean Aceptar=false, Cancelar=false;
    private void CancelarBActionPerformed(java.awt.event.ActionEvent evt) {
        // Aqui va el codigo para el bottom Cancelar
        Cancelar= true;
    }

    // Variables declaration - do not modify
    int Leather, Energy;
    int StaminaLv;
    private javax.swing.JButton AceptarB;
    private javax.swing.JButton CancelarB;
    private javax.swing.JComboBox EnergyC;
    private javax.swing.JSlider EnergyL;
    private javax.swing.JComboBox LeatherC;
    private javax.swing.JLabel Tex1;
    private javax.swing.JLabel Tex2;
    private javax.swing.JLabel Tex3;
    // End of variables declaration
}
