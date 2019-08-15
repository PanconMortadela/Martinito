package scripts;

import javax.swing.*;



public class Ini{
    private JPanel Pluto;
    private JFrame Puto;
    private JComboBox Skin;
    private JComboBox potion;
    private JSlider ene;
    private JButton Aceptar;
    private JButton Cancelar;
    private JLabel tex1;
    private JLabel tex2;
    private JLabel tex3;

    public void Ini() {
         Puto.add(Pluto);
         Pluto.add(Skin);
         Pluto.add(potion);
         Pluto.add(ene);
         Pluto.add(Aceptar);
         Pluto.add(Cancelar);
         Pluto.add(tex1);
         Pluto.add(tex2);
         Pluto.add(tex3);
   }


   /*
    public String getSkin(){
        return Skin.getSelectedItem().toString();
    }
    public String getPotion(){
        return potion.getSelectedItem().toString();
    }
    public int getene(){
        return ene.getValue();
    }
*/
}
