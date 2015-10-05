package com.jyu.KarelFF.think;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jyu.KarelFF.algorithm.Algorithm;
import com.jyu.KarelFF.tool.Directory;

public class Getmethod {
	private Algorithm iface;

	public Algorithm getIface() {
		return iface;
	}

	public void setIface(Algorithm iface) {
		this.iface = iface;
	}

	public static String[] getmethod(String pddl, String problemfile,
			char InitStatus, int[] argsLoc) throws IOException,
			InterruptedException {
		// 当前方向
		char direction = InitStatus;
		// 下一步要走的方向
		char nextdirection;
		// 程序调用输出结果
		String sOut = "";
		// 读取临时文件每一行内容
		String sLine = "";

		// -- 调用javaff.jar -- new
		// 记录旧输出地点
		PrintStream old = System.out;
		// 重定向到临时文件
		try {
			System.setOut(new PrintStream(Directory.getDefConf()
					+ Directory.getDefTMP()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 目标参数，用于迷宫算法
		String[] locs = new String[argsLoc.length];
		for (int i = 0; i < argsLoc.length; i++) {
			locs[i] = Integer.toString(argsLoc[i]);
		}
		// 调用算法
		String[] args = new String[] { pddl, problemfile, locs[0], locs[1],
				InitStatus + "", locs[2], locs[3] };
		ApplicationContext apc = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		Getmethod gm = (Getmethod) apc.getBean("load");
		gm.getIface().load(args);
		gm = null;
		// Algorithm iface = new JavaFFAlgorithm();
		// iface.load(args);

		// 重定向到旧地点
		System.setOut(old);

		/*
		 * // -- 调用javaff -- old // sErr程序调用错误信息 // String sErr, String command
		 * = "cd bin && java -Xmx512m javaff.JavaFF " + pddl + problemfile +
		 * " > ..\\tmp"; Process p = Runtime.getRuntime().exec("cmd /c " +
		 * command); // String[] cmd = { "/bin/sh", "-c", comand }; // linux //
		 * Process p = Runtime.getRuntime().exec(cmd);
		 * System.out.println(">>>> command\n" + command);
		 * 
		 * p.waitFor(); // p.destroy();
		 * 
		 * // 读取错误信息和输出结果 // InputStreamReader e = new
		 * InputStreamReader(p.getErrorStream()); // InputStreamReader r = new
		 * InputStreamReader(p.getInputStream()); // // LineNumberReader eLine =
		 * new LineNumberReader (e); // sErr=""; // // while ((sLine =
		 * eLine.readLine ()) != null) // sErr += sLine + '\n'; // //
		 * LineNumberReader rLine = new LineNumberReader (r); // sOut=""; String
		 * dir = System.getProperty("user.dir"); String filename = "tmp";
		 */
		// 临时文件对象
		File tmp = new File(Directory.getDefConf(), Directory.getDefTMP());
		BufferedReader br = new BufferedReader(new FileReader(tmp));

		// 保存时间
		String instant = "";
		String plan = "";

		while ((sLine = br.readLine()) != null) {
			System.out.println(/* "sLIne:" + */sLine);
			if (sLine.indexOf('(') != -1) {
				String temp = sLine.substring(sLine.indexOf('e') + 1,
						sLine.indexOf('e') + 2) + '\n';
				// System.out.println(temp);
				nextdirection = temp.charAt(0);
				// nextdirection=sLine.charAt(11);
				sOut += getway(direction, nextdirection);
				direction = nextdirection;
			}

			if (sLine.contains("Instantiation Time"))
				instant = sLine;
			else if (sLine.contains("Planning Time"))
				plan = sLine;
		}
		// System.out.println(sOut);
		String wayfile = writeWay(problemfile, sOut);

		// if(sErr!="")
		// System.out.println("error:\n"+sErr);
		br.close();

		/*
		 * // 删除临时文件tmp command = "del tmp"; p =
		 * Runtime.getRuntime().exec("cmd /c " + command); p.waitFor();
		 */

		// System.out.println("output:\n"+sOut);

		// 汇总字串
		instant = instant.replaceAll("\t", " ");
		plan = plan.replaceAll("\t", " ");
		String[] strs = new String[] { wayfile, instant, plan };
		return strs;
	}

	// 根据当前方向和下一步方向得到移动方法
	public static String getway(char tdir, char tnextdir) {
		int dir = dirToNum(tdir);
		int nextdir = dirToNum(tnextdir);

		String moveWay = "";
		switch (nextdir - dir) {
		case -3:
			moveWay = "turnRight\n" + "move\n";
			break;
		case -2:
			moveWay = "turnRight\n" + "turnRight\n" + "move\n";
			break;
		case -1:
			moveWay = "turnLeft\n" + "move\n";
			break;
		case 0:
			moveWay = "move\n";
			break;
		case 1:
			moveWay = "turnRight\n" + "move\n";
			break;
		case 2:
			moveWay = "turnRight\n" + "turnRight\n" + "move\n";
			break;
		case 3:
			moveWay = "turnLeft\n" + "move\n";
			break;

		default:
			break;
		}

		return moveWay;
	}

	// 将方向装换成数字
	public static int dirToNum(char dir) {
		// N:1 E:2 S:3 W:4
		int d = 0;
		switch (dir) {
		case 'N':
			d = 1;
			break;
		case 'E':
			d = 2;
			break;
		case 'S':
			d = 3;
			break;
		case 'W':
			d = 4;
			break;
		}
		return d;
	}

	// 写入移动方法
	public static String writeWay(String filePath, String way)
			throws IOException {
		File wayfile = new File(filePath + "_way");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(wayfile)));
		bw.write(way);
		bw.close();
		return wayfile.toString();
	}
}
