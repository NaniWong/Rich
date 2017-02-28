import javax.microedition.lcdui.*;
/**
  * ϵͳ�˵���
  * @author ����
  * @version 1.0
  */

public class SystemList
    extends List
    implements CommandListener {

  private KMRichMan richManObject;
  private PlayCanvas playCanvas;
  private String musicOn = "����[��]";
  private String musicOff = "����[��]";
  private String music = "����[";
  private String lightOn = "������[��]";
  private String lightOff = "������[��]";
  private String gameSpeed = "��Ϸ�ٶ�[";
  /**
 *	����һ������
 */


  public SystemList(KMRichMan kmrichman,PlayCanvas playCanvas) {
    super("ѡ��˵�", 3);
    this.richManObject = kmrichman;
    this.playCanvas = playCanvas;

    if (this.playCanvas.isIsLightOn()) {
      append(this.lightOn, null);
    }
    else {
      append(this.lightOff, null);
    }
    if (this.playCanvas.isIsMusicOn()) {
      append(this.musicOn, null);
    }
    else {
      append(this.musicOff, null);
    }
    append(this.music + this.playCanvas.getMusic() +"]", null);
    append(this.gameSpeed + this.playCanvas.getGameSpeed() +"]", null);
    append("������Ϸ", null);
    append("�˳���Ϸ", null);
    setCommandListener(this);
  }
  /**
 *	������
 */

  public void commandAction(Command command, Displayable displayable) {
    switch (getSelectedIndex()) {
      case 0: // �����⿪��
        if (this.playCanvas.isIsLightOn()) {
          this.set(0,this.lightOff, null);
          this.playCanvas.setIsLightOn(false);
        }
        else {
          this.set(0,this.lightOn, null);
          this.playCanvas.setIsLightOn(true);
        }
        break;

      case 1: // ���ֿ���
        if (this.playCanvas.isIsMusicOn()) {
          this.set(1,this.musicOff, null);
          this.playCanvas.setIsMusicOn(false);
        }
        else {
          this.set(1,this.musicOn, null);
          this.playCanvas.setIsMusicOn(true);
        }
        break;
      case 2: // ����ѡ��
        if (this.playCanvas.getMusic() < 7) {
          this.playCanvas.setIsMusicOn(false);
          this.playCanvas.setMusic(this.playCanvas.getMusic() + 1);
          this.playCanvas.setIsMusicOn(true);
          this.set(1,this.musicOn, null);
          this.set(2,this.music + this.playCanvas.getMusic() +"]", null);
        }
        else {
          this.playCanvas.setIsMusicOn(false);
          this.playCanvas.setMusic(1);
          this.playCanvas.setIsMusicOn(true);
          this.set(1,this.musicOn, null);
          this.set(2,this.music + this.playCanvas.getMusic() +"]", null);
        }
        break;

      case 3: //��Ϸ�ٶ�
        if (this.playCanvas.getGameSpeed() < 3) {
          this.playCanvas.setGameSpeed(this.playCanvas.getGameSpeed() + 1);
          this.set(3,this.gameSpeed + this.playCanvas.getGameSpeed() +"]", null);
        }
      else {
        this.playCanvas.setGameSpeed(1);
        this.set(3,this.gameSpeed + this.playCanvas.getGameSpeed() +"]", null);
      }
      break;

      case 4: //����
        richManObject.setDisplayToPlayCanvas1();
        break;

      case 5: // �˳�
        this.playCanvas.autoSaveGame();
        KMRichMan.exitGame();
        break;
    }
  }
}
