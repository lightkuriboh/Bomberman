package signal;

import java.io.FileReader;
import java.io.Serializable;
import java.util.Scanner;

public class GameStart implements Serializable {
     private int _width,_height;
     private char[][] _map;
     public GameStart(String mapPath) {
         loadLevel(mapPath);
     }


    private void loadLevel(String path) {
        // TODO: đọc dữ liệu từ tệp cấu hình /levels/Level{level}.txt
        // TODO: cập nhật các giá trị đọc được vào _width, _height, _level, _map
        try {
            FileReader fileReader = new FileReader(path);
            Scanner scanner = new Scanner(fileReader);

            _height = scanner.nextInt();
            _width = scanner.nextInt();
            System.out.println(_height);
            _map = new char[_height][_width];

            scanner.nextLine();
            for (int i = 0; i < _height; i++) {
                String line = scanner.nextLine();
//				System.out.println(line);
                for (int j = 0; j < _width; j++) {
                    _map[i][j] = line.charAt(j);
                }
            }
            scanner.close();
            fileReader.close();



        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
    }

    public int get_width() {
        return _width;
    }

    public int get_height() {
        return _height;
    }

    public char[][] get_map() {
        return _map;
    }
}
