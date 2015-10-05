package com.jyu.KarelFF.think;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MapToProblem {

	// public static void main(String[] args) throws IOException {
	// // Map map=new Map();
	// // map=map.MapToProblem("src/66");
	// // System.out.println("done");
	// // System.out.println("Scheduling");
	//
	// }
	private static String InitStatus = ""; // 机器人初始方向

	// 将地图转换为problem文件
	public String mapToProblem(String filePath, int initX, int initY,
			int targX, int targY) throws IOException {
		// String dir = System.getProperty("user.dir") + "/src/temp";
		// File action = new File(dir, filename);
		// BufferedReader br = new BufferedReader(new FileReader(action));
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath)));

		String problem = filePath.substring(filePath.lastIndexOf('/') + 1); // 文件名
		String define = "(define (problem " + problem
				+ "_problem) (:domain Karel_domain)"; // define属性
		String objects = ""; // problem文件的objects属性
		List<String> Wall = new ArrayList<String>(); // 存储墙
		String at = "(:init\n\t(at Blk"; // Karel的位置
		// List<String> beeper = new ArrayList<String>(); // 存储beeper
		int prows = 0; // 地图的宽度
		int pcols = 0; // 地图的高度 //地图尺寸
		String location; // 每个对象的相对位置
		String allow; // 允许走的方向
		String goal = "(:goal (and\n\t(at Blk"; // 目的地

		// 逐行读取地图文件
		for (String line = br.readLine(); line != null; line = br.readLine()) {

			line = line.replaceAll(" ", ""); // 消除空格
			String type = line.substring(0, line.indexOf(':')); // 配置的类型
			String rows = ""; // 横坐标
			String cols = ""; // 纵坐标
			String noordir = ""; // 数量或者方向

			if (line.indexOf('(') != -1) { // 取出各类属性
				rows = line.substring(line.indexOf('(') + 1, line.indexOf(','));
				cols = line.substring(line.indexOf(',') + 1, line.indexOf(')'));
				noordir = line.substring(line.indexOf(')') + 1);
			}

			// 对4种不同属性进行收集处理
			if (type.equals("Dimension")) {
				prows = Integer.parseInt(rows);
				pcols = Integer.parseInt(cols);
				objects = dealObject(prows, pcols, rows, cols);
			} else if (type.equals("Wall")) {
				Wall = dealWall(Wall, rows, cols, noordir);
			} else if (type.equals("Beeper")) {
			} else if (type.equals("Karel")) {
				String tempa = addzero(initY);
				String tempb = addzero(initX);
				at = at + tempb + tempa + ")";
				// System.out.println(noordir);
				InitStatus = noordir;
				InitStatus = InitStatus.toUpperCase();
			}
		}
		br.close();
		// 读地图文件结束

		// 写入problem文件
		File problemfile = new File(filePath + "_problem");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(problemfile)));

		location = dealLocation(prows, pcols, Wall);
		allow = dealAlow(Wall, prows, pcols);
		allow += ")\n";
		// 目标传参
		String goalrows = "";
		String goalcols = "";
		goalrows = addzero(targX);
		goalcols = addzero(targY);
		goal += goalrows + goalcols + ")\n\t)\n))";

		bw.write(define + "\n");
		bw.write(objects + "\n");
		bw.write(at + "\n");
		bw.write(location + "\n");
		bw.write(allow + "\n");
		bw.write(goal);

		bw.close();

		return problemfile.toString();
	}

	// 生成所有object属性
	public String dealObject(int prows, int pcols, String rows, String cols) {

		String object = "(:objects\n\t", tempa = "", tempb = "";
		for (int i = 1; i < prows + 1; i++) {
			for (int j = 1; j < pcols + 1; j++) {
				tempa = addzero(i);
				tempb = addzero(j);
				object = object + " Blk" + tempa + tempb;
			}
		}
		object = object + " " + "- object)";
		return object;
	}

	// 收集所有Wall属性，生成所有不能走的方法(黑名单)。

	public List<String> dealWall(List<String> wall, String rows, String cols,
			String noordir) {

		String tempa = addzero(Integer.parseInt(rows));
		String tempa1 = addzero(Integer.parseInt(rows) - 1);
		String tempb = addzero(Integer.parseInt(cols));
		String tempb1 = addzero(Integer.parseInt(cols) - 1);

		if (noordir.equals("west")) {
			String wall1 = "\t(allowwest Blk" + tempa + tempb + ")\n";
			String wall2 = "\t(alloweast Blk" + tempa1 + tempb + ")\n";
			wall.add(wall1);
			wall.add(wall2);
		} else {
			String wall1 = "\t(allowsouth Blk" + tempa + tempb + ")\n";
			String wall2 = "\t(allownorth Blk" + tempa + tempb1 + ")\n";
			wall.add(wall1);
			wall.add(wall2);
		}
		return wall;
	}

	// 处理每个对象的相对位置
	public String dealLocation(int prows, int pcols, List<String> Wall) {
		String location = "";
		String locations = "";
		String locationn = "";
		String locatione = "";
		String locationw = "";
		for (int i = 1; i < prows + 1; i++) {
			for (int j = 1; j < pcols + 1; j++) {
				String tempa = addzero(i);
				String tempb = addzero(j);
				String tempa1 = addzero(i - 1);
				String tempb1 = addzero(j - 1);
				String tempa2 = addzero(i + 1);
				String tempb2 = addzero(j + 1);
				if (i != 1) {
					String temp = "\t(west Blk" + tempa1 + tempb + " Blk"
							+ tempa + tempb + ")\n";
					locationw += temp;
				}
				if (i != prows) {
					String temp = "\t(east Blk" + tempa2 + tempb + " Blk"
							+ tempa + tempb + ")\n";
					locatione += temp;
				}
				if (j != 1) {
					String temp = "\t(south Blk" + tempa + tempb1 + " Blk"
							+ tempa + tempb + ")\n";
					locations += temp;
				}
				if (j != pcols) {
					String temp = "\t(north Blk" + tempa + tempb2 + " Blk"
							+ tempa + tempb + ")\n";
					locationn += temp;
				}
			}
		}
		location = locatione + locationn + locations + locationw;

		return location;
	}

	// 在小于10的数字前面加0
	public String addzero(int num) {
		String temp = "";
		if (num < 10) {
			temp = "0" + num;
		} else
			temp = "" + num;
		return temp;
	}

	// 处理allow属性
	public String dealAlow(List<String> wall, int prows, int pcols) {
		String allow = "";
		String allows = "";
		String allown = "";
		String allowe = "";
		String alloww = "";
		for (int i = 1; i < prows + 1; i++) {
			for (int j = 1; j < pcols + 1; j++) {
				String tempa = addzero(i);
				String tempb = addzero(j);
				if (/*j != 1*/i != 1) {
					String temps = "\t(allowwest Blk" + tempa + tempb + ")\n";
					if (alow(wall, temps))
						allows += temps;
				}
				if (/*j != pcols*/i != prows) {
					String tempn = "\t(alloweast Blk" + tempa + tempb + ")\n";
					if (alow(wall, tempn))
						allown += tempn;
				}
				if (/*i != prows*/j != pcols) {
					String tempe = "\t(allownorth Blk" + tempa + tempb + ")\n";
					if (alow(wall, tempe))
						allowe += tempe;
				}
				if (/*i != 1*/j != 1) {
					String tempw = "\t(allowsouth Blk" + tempa + tempb + ")\n";
					if (alow(wall, tempw))
						alloww += tempw;
				}
			}
		}
		allow = allowe + allown + allows + alloww;
		return allow;
	}

	// 检查一个allow属性是否合法，合法返回1.
	public boolean alow(List<String> wall, String str) {
		boolean flag = true;
		for (int i = 0; i < wall.size(); i++) {
			if (str.equals(wall.get(i))) {
				flag = false;
			}
		}
		return flag;
	}

	// 返回机器人的初始方向
	public static char getInitStatus() {
		return InitStatus.charAt(0);
	}
}
