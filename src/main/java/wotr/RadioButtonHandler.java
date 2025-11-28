package wotr;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: RadioButtonHandler */
/* compiled from: ChooseFSP */
class RadioButtonHandler implements ActionListener {
    ChooseFSP controls;

    /* renamed from: f */
    Frame f15f;

    RadioButtonHandler(ChooseFSP c) {
        this.controls = c;
    }

    public void actionPerformed(ActionEvent e) {
        if (!e.getActionCommand().equals("Gandalf") && !e.getActionCommand().equals("Gandalf2") && !ChooseFSP.CouncilChosen) {
            ChooseFSP.CouncilChosen = true;
            if (!e.getActionCommand().equals("Boromir2")) {
                ChooseFSP.Boromir.setSelected(true);
            }
            if (!e.getActionCommand().equals("Strider2")) {
                ChooseFSP.Strider.setSelected(true);
            }
            if (!e.getActionCommand().equals("Legolas2")) {
                ChooseFSP.Legolas.setSelected(true);
            }
            if (!e.getActionCommand().equals("Merry2")) {
                ChooseFSP.Merry.setSelected(true);
            }
            if (!e.getActionCommand().equals("Gimli2")) {
                ChooseFSP.Gimli.setSelected(true);
            }
            if (!e.getActionCommand().equals("Pippin2")) {
                ChooseFSP.Pippin.setSelected(true);
            }
        }
    }
}
