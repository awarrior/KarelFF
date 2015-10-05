/**
 * �����ɵ����߷����ĸ�ʽ������Ļ���������Getmethod���getway�������޸ġ�
 */

package com.jyu.KarelFF.think;

import java.io.IOException;

import com.jyu.KarelFF.tool.Directory;


public class Start {
	public String run(String direction, String filename, int initX,
			int initY, char dir, int targX, int targY) {
		try {
			// �����ļ���PDDL�ļ�·��
			String filePath = direction + "/" + filename;
			String problemname = new MapToProblem().mapToProblem(filePath,
					initX, initY, targX, targY);
			String pddl = Directory.getDefConf() + Directory.getDefPDDL();
			// ��ȡ��������������ʱ��
			int args[] = {initX, initY, targX, targY};
			String[] strs = Getmethod.getmethod(pddl, problemname, dir, args);
//			problemname = strs[0];
			return strs[1] + "\n" + strs[2];
			
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "";
		}
	}

}