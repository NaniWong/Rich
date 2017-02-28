import javax.microedition.lcdui.*;
/**
  * 系统菜单。
  * @author 嘟嘟熊
  * @version 1.0
  */

public class SystemList
    extends List
    implements CommandListener {

  private KMRichMan richManObject;
  private PlayCanvas playCanvas;
  private String musicOn = "音乐[开]";
  private String musicOff = "音乐[关]";
  private String music = "音乐[";
  private String lightOn = "背景灯[开]";
  private String lightOff = "背景灯[关]";
  private String gameSpeed = "游戏速度[";
  /**
 *	构造一个对象
 */


  public SystemList(KMRichMan kmrichman,PlayCanvas playCanvas) {
    super("选项菜单", 3);
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
    append("返回游戏", null);
    append("退出游戏", null);
    setCommandListener(this);
  }
  /**
 *	处理按键
 */

  public void commandAction(Command command, Displayable displayable) {
    switch (getSelectedIndex()) {
      case 0: // 背景光开关
        if (this.playCanvas.isIsLightOn()) {
          this.set(0,this.lightOff, null);
          this.playCanvas.setIsLightOn(false);
        }
        else {
          this.set(0,this.lightOn, null);
          this.playCanvas.setIsLightOn(true);
        }
        break;

      case 1: // 音乐开关
        if (this.playCanvas.isIsMusicOn()) {
          this.set(1,this.musicOff, null);
          this.playCanvas.setIsMusicOn(false);
        }
        else {
          this.set(1,this.musicOn, null);
          this.playCanvas.setIsMusicOn(true);
        }
        break;
      case 2: // 音乐选择
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

      case 3: //游戏速度
        if (this.playCanvas.getGameSpeed() < 3) {
          this.playCanvas.setGameSpeed(this.playCanvas.getGameSpeed() + 1);
          this.set(3,this.gameSpeed + this.playCanvas.getGameSpeed() +"]", null);
        }
      else {
        this.playCanvas.setGameSpeed(1);
        this.set(3,this.gameSpeed + this.playCanvas.getGameSpeed() +"]", null);
      }
      break;

      case 4: //返回
        richManObject.setDisplayToPlayCanvas1();
        break;

      case 5: // 退出
        this.playCanvas.autoSaveGame();
        KMRichMan.exitGame();
        break;
    }
  }
}
