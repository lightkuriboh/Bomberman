package uet.oop.bomberman.sound;
import java.io.File;
import javax.sound.sampled.*;

public class SoundPlayer {

    private static final String bgm = "res/musics/bg.wav";
    private static final String cheer = "res/musics/cheer.wav";
    private static final String bomb = "res/musics/bomb.wav";
    private static final String die = "res/musics/die.wav";

    private SoundPlayer () {

    }

    private static Clip bombClip;
    private static Clip dieClip;
    private static Clip cheerClip;

    private static File bombFile;
    private static File cheerFile;
    private static File dieFile;

    public static void initSoundData() throws Exception {

        bombFile = new File(bomb);
        dieFile = new File(die);
        cheerFile = new File(cheer);

    }

    public static void playBgMusic() {
        try {
            File file = new File(bgm);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(100);
            clip.start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void makeSound(Clip clip, File file) throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

    public static void playBombSound() {
        try {
            makeSound(bombClip, bombFile);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void playDieSound() {
        try {
            makeSound(dieClip, dieFile);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void playCheerSound() {
        try {
            makeSound(cheerClip, cheerFile);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
