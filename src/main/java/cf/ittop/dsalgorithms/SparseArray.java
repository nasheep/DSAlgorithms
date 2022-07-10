package cf.ittop.dsalgorithms;

import org.junit.Test;

import java.io.*;

/**
 * 稀疏数组
 */
public class SparseArray {

    /**
     * 二维数组转稀疏数组
     */
    @Test
    public void two2SparseArray() {
        // 1. 创建原始二维数组（空棋盘）
        int[][] twoArray = new int[10][10];
        // 2. 添加数据（下棋，1：白棋 2：黑棋）
        twoArray[1][2] = 1;
        twoArray[2][3] = 2;
        twoArray[8][6] = 1;
        // 3. 遍历原始二维数组获取数据个数（棋子个数）
        int sum = 0;
        for (int[] rows : twoArray) {
            for (int data : rows) {
                if (data != 0) {
                    sum++;
                }
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }
        System.out.println("当前棋子个数：" + sum);

        // 4. 创建稀疏数组
        int[][] sparseArray = new int[sum + 1][3];
        // 5. 给稀疏数组第一行赋值（行数、列数、非零数据个数）
        sparseArray[0][0] = twoArray.length;
        sparseArray[0][1] = twoArray[1].length;
        sparseArray[0][2] = sum;

        // 6. 给稀疏数组其他行赋值（非零数据所在的行、列、值）
        int count = 0; // 记录稀疏数组的行数
        for (int i = 0; i < twoArray.length; i++) {
            for (int j = 0; j < twoArray[i].length; j++) {
                if (twoArray[i][j] != 0) {
                    count++;
                    sparseArray[count][0] = i;
                    sparseArray[count][1] = j;
                    sparseArray[count][2] = twoArray[i][j];
                }
            }
        }
        // 7. 打印稀疏数组
        for (int i = 0; i < sparseArray.length; i++) {
            System.out.printf("%d\t%d\t%d\t", sparseArray[i][0], sparseArray[i][1], sparseArray[i][2]);
            System.out.println();
        }
        // 8. 将稀疏数组写入文件
        writeToFile(sparseArray);
    }

    /**
     * 稀疏数组转二维数组
     */
    @Test
    public void sparse2TwoArray() {
        // 1. 从文件读取稀疏数组
        int[][] sparseArray;
        try {
            sparseArray = readFromFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        // 2. 打印读取的稀疏数组
        System.out.println("读取到的稀疏数组：");
        for (int[] rows : sparseArray) {
            System.out.printf("%d\t%d\t%d\t",rows[0],rows[1],rows[2]);
            System.out.println();
        }

        // 3. 创建二维数组
        int[][] twoArray = new int[sparseArray[0][0]][sparseArray[0][1]];
        for (int i = 1; i < sparseArray.length; i++) {
            twoArray[sparseArray[i][0]][sparseArray[i][1]] = sparseArray[i][2];
        }
        // 4. 打印二维数组
        System.out.println("转换后的二维数组：");
        for (int[] rows : twoArray) {
            for (int data : rows) {
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }
    }

    /**
     * 将稀疏数组写入文件
     *
     * @param sparseArray
     */
    public void writeToFile(int[][] sparseArray) {
        // 文件路径
        File file = new File("file");
        // 判断路径是否存在
        if (!file.exists()) {
            file.mkdir();
        }
        // 创建缓冲流
        BufferedWriter bufferedWriter = null;
        try {
            // Windows平台使用\作为路径分隔符，在Java字符串中需要用\\表示一个\；Linux平台使用/作为路径分隔符
            bufferedWriter = new BufferedWriter(new FileWriter(file + "\\sparseArray.txt"));
            // 使用缓冲流写入数据
            for (int[] rows : sparseArray) {
                bufferedWriter.write(rows[0] + " " + rows[1] + " " + rows[2]);
                // 换行（Windows：\r\n、Linux：/n、Mac：/r）
                // bufferedWriter.write("\r\n");
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从文件读取稀疏数组
     * @throws IOException
     */
    public int[][] readFromFile() {
        // 文件路径
        File file = new File("file\\sparseArray.txt");
        if (!file.exists()) {
            throw new RuntimeException("File is not found.");
        }

        // 创建稀疏数组
        int[][] sparseArray = null;
        // 创建缓冲流
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            // 统计行数
            int count = 0;
            // 读取的每一行字符
            String lineStr = null;
            // 标记读取的是不是第一行
            boolean first = true;
            while ((lineStr = bufferedReader.readLine()) != null) {
                // 分割每行的字符
                String[] strArray = lineStr.split(" ");
                if (first) {
                    // 从第一行中获取行和列的大小
                    sparseArray = new int[Integer.parseInt(strArray[2]) + 1][3];
                    first = false;
                }
                sparseArray[count][0] = Integer.parseInt(strArray[0]);
                sparseArray[count][1] = Integer.parseInt(strArray[1]);
                sparseArray[count][2] = Integer.parseInt(strArray[2]);
                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sparseArray;
    }
}
