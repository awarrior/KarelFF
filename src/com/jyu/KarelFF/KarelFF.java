package com.jyu.KarelFF;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.jyu.KarelFF.map.Block;
import com.jyu.KarelFF.map.Direction;
import com.jyu.KarelFF.map.Location;
import com.jyu.KarelFF.map.Map;
import com.jyu.KarelFF.map.Wall;
import com.jyu.KarelFF.think.Start;
import com.jyu.KarelFF.tool.BlockPanel;




public class KarelFF {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KarelFF window = new KarelFF();
					window.frmKarelff.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public KarelFF() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		buildFrame();
		changeLAF();
		readInfo.readMap();

		loadGBLayout();
		loadPanelLeft();
		loadPanelRight();
	}

	/**
	 * set frame style
	 */
	private void buildFrame() {
		frmKarelff.getContentPane().setBackground(Color.WHITE);
		frmKarelff.setTitle("KarelFF");
		frmKarelff.setSize(736, 485);
		frmKarelff.setLocationByPlatform(true);
		frmKarelff.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * change the look and feel
	 */
	private void changeLAF() {
		String plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(plaf);
			SwingUtilities.updateComponentTreeUI(frmKarelff);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * the main layout in the frame
	 */
	private void loadGBLayout() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 209, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 447, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		frmKarelff.getContentPane().setLayout(gridBagLayout);
	}

	/**
	 * load left panel
	 */
	private void loadPanelLeft() {
		JPanel panelTool = new JPanel();
		panelTool.setBackground(Color.WHITE);
		panelLeft.setBackground(Color.WHITE);
		panelLeft.add(panelTool);
		loadPanelMap();
		loadPanelGoal();
		setGBLPanelLeft();
		setGBCPanelLeft();
	}

	/**
	 * set map combo items
	 */
	private String[] setComboItems(int num) {
		String[] selectItems = new String[num];
		for (int i = 0; i < selectItems.length; i++) {
			selectItems[i] = Integer.toString(i + 1);
		}
		return selectItems;
	}

	/**
	 * read map param
	 */
	private void readProperty(Map readInfo) {
		loadComboHorizontal(readInfo);
		loadComboVertical(readInfo);
		loadSlider(readInfo);
	}

	/**
	 * load ComboHorizontal
	 */
	private void loadComboHorizontal(final Map readInfo) {
		int numX = readInfo.getDms().getX();
		comboTargetX = new JComboBox();
		comboTargetX.setModel(new DefaultComboBoxModel(setComboItems(numX)));
		int locX = readInfo.getLoc().getBlk().getX() - 1;
		comboTargetX.setSelectedIndex(locX);

		comboHorizontal = new JComboBox();
		comboHorizontal.setEnabled(false);
		comboHorizontal.setModel(new DefaultComboBoxModel(
				setComboItems(DEFAULTITEMS)));
		comboHorizontal.setSelectedIndex(numX - 1);
		comboHorizontal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int select = comboHorizontal.getSelectedIndex() + 1;
				panelBlocks.paintBlockX(select);
				paintWell();
			}
		});
	}

	/**
	 * load ComboVertical
	 */
	private void loadComboVertical(final Map readInfo) {
		int numY = readInfo.getDms().getY();
		comboTargetY = new JComboBox();
		comboTargetY.setModel(new DefaultComboBoxModel(setComboItems(numY)));
		int locY = readInfo.getLoc().getBlk().getY() - 1;
		comboTargetY.setSelectedIndex(locY);

		comboVertical = new JComboBox();
		comboVertical.setEnabled(false);
		comboVertical.setModel(new DefaultComboBoxModel(
				setComboItems(DEFAULTITEMS)));
		comboVertical.setSelectedIndex(numY - 1);
		comboVertical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int select = comboVertical.getSelectedIndex() + 1;
				panelBlocks.paintBlockY(select);
				paintWell();
			}
		});
	}

	/**
	 * load Speed
	 */
	private void loadSlider(final Map readInfo) {
		sliderSpeed = new JSlider();
		double speed = readInfo.getSpeed() * 100;
		sliderSpeed.setValue((int) speed);
		sliderSpeed.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				readInfo.setSpeed(sliderSpeed.getValue());
			}
		});
		sliderSpeed.setMaximum(99);
		sliderSpeed.setBackground(Color.WHITE);
	}

	/**
	 * load the map panel
	 */
	private void loadPanelMap() {
		panelMap.setBorder(new TitledBorder(null, "map", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelMap.setBackground(Color.WHITE);

		JLabel lblHorizontal = new JLabel("Horizontal:");
		JLabel lblVertical = new JLabel("Vertical:");

		readProperty(readInfo);

		comboDetail = new JComboBox();
		comboDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String select = comboDetail.getSelectedItem().toString();
				if (select.equals("Random")) {
					int x = comboHorizontal.getSelectedIndex();
					int y = comboVertical.getSelectedIndex();
					panelBlocks.random(x, y);
					return;
				}
				if (select.equals("Set Walls")) {
					setWallsPower = true;
					setKarelPower = false;
				} else if (select.equals("Set Karel")) {
					setKarelPower = true;
					setWallsPower = false;
				} else {
					setWallsPower = false;
					setKarelPower = false;
				}
			}
		});
		comboDetail.setModel(new DefaultComboBoxModel(new String[] { "",
				"Random", "Set Walls", "Set Karel" }));
		comboDetail.setToolTipText("");
		comboDetail.setEnabled(false);

		final JButton btnNew = new JButton("new");
		final JButton btnLoad = new JButton("load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(readInfo
						.getDirection());
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setDialogTitle("load map");
				int ret = fileChooser.showOpenDialog(null);
				if (ret == JFileChooser.APPROVE_OPTION) {
					readInfo.setFilename(fileChooser.getSelectedFile()
							.getName());
					readInfo.setDirection(fileChooser.getSelectedFile()
							.getParent());
					readInfo.getSomeWalls().clear();
					readInfo.readMap();
					// read property
					int x = readInfo.getDms().getX();
					int y = readInfo.getDms().getY();
					comboHorizontal.setSelectedIndex(x - 1);
					comboVertical.setSelectedIndex(y - 1);
					comboTargetX.setModel(new DefaultComboBoxModel(
							setComboItems(x)));
					comboTargetY.setModel(new DefaultComboBoxModel(
							setComboItems(y)));
					int locX = readInfo.getLoc().getBlk().getX() - 1;
					comboTargetX.setSelectedIndex(locX);
					int locY = readInfo.getLoc().getBlk().getY() - 1;
					comboTargetY.setSelectedIndex(locY);
					sliderSpeed.setValue((int) (readInfo.getSpeed() * 100));
					initX = comboTargetX.getSelectedIndex() + 1;
					initY = comboTargetY.getSelectedIndex() + 1;
					
					panelBlocks.setLoc(readInfo.getLoc());
					paintMap();
				}
			}
		});
		final JButton btnSave = new JButton("save");
		btnSave.setEnabled(false);

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(readInfo
						.getDirection());
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setDialogTitle("save map");
				int ret = fileChooser.showSaveDialog(null);
				if (ret == JFileChooser.APPROVE_OPTION) {
					String name = fileChooser.getSelectedFile().getName();
					String dir = fileChooser.getSelectedFile().getParent();

					File newFile = new File(dir + "/" + name);
					try {
						BufferedWriter bw = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(
										newFile)));
						bw.write("Dimension: (x, y)\n");
						bw.write("Wall: (x, y) east\n");
						bw.write("Karel: (x, y) east\n");
						bw.write("BeeperBag: INFINITE\n");
						bw.write("Speed: 0.");
						bw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					readInfo.setFilename(name);
					readInfo.setDirection(dir);

					readInfo.setDimension(
							comboHorizontal.getSelectedIndex() + 1,
							comboVertical.getSelectedIndex() + 1);
					readInfo.setWalls(panelBlocks.getaWallList());
					readInfo.setLocation(panelBlocks.getLoc());
					readInfo.setSpeed(sliderSpeed.getValue());

					comboHorizontal.setEnabled(false);
					comboVertical.setEnabled(false);
					comboDetail.setEnabled(false);
					btnNew.setEnabled(true);
					btnLoad.setEnabled(true);
					btnSave.setEnabled(false);
					comboTargetX.setEnabled(true);
					comboTargetY.setEnabled(true);
					sliderSpeed.setEnabled(true);
					btnThink.setEnabled(true);
					btnRun.setEnabled(false);
					btnReset.setEnabled(true);

					comboDetail.setSelectedIndex(0);

					saveMap();

					initX = comboTargetX.getSelectedIndex() + 1;
					initY = comboTargetY.getSelectedIndex() + 1;
				}
			}
		});
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboHorizontal.setEnabled(true);
				comboVertical.setEnabled(true);
				comboDetail.setEnabled(true);
				btnNew.setEnabled(false);
				btnLoad.setEnabled(false);
				btnSave.setEnabled(true);
				comboTargetX.setEnabled(false);
				comboTargetY.setEnabled(false);
				sliderSpeed.setEnabled(false);
				btnThink.setEnabled(false);
				btnRun.setEnabled(false);
				btnReset.setEnabled(false);

				panelBlocks.delWalls();
				readInfo.setLoc(new Location(new Block(1, 1), Direction.EAST));
				readInfo.getSomeWalls().clear();
				comboTargetX.setSelectedIndex(0);
				comboTargetY.setSelectedIndex(0);
				
				panelBlocks.setLoc(new Location(new Block(1, 1), Direction.EAST));
				paintMap();
			}
		});

		JLabel lblDetail = new JLabel("Detail:");

		GroupLayout gl_panelMap = new GroupLayout(panelMap);
		gl_panelMap
				.setHorizontalGroup(gl_panelMap
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panelMap
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_panelMap
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																gl_panelMap
																		.createSequentialGroup()
																		.addGroup(
																				gl_panelMap
																						.createParallelGroup(
																								Alignment.TRAILING)
																						.addComponent(
																								lblDetail)
																						.addComponent(
																								lblVertical)
																						.addComponent(
																								lblHorizontal))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_panelMap
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								comboVertical,
																								0,
																								107,
																								Short.MAX_VALUE)
																						.addComponent(
																								comboHorizontal,
																								0,
																								107,
																								Short.MAX_VALUE)
																						.addComponent(
																								comboDetail,
																								0,
																								107,
																								Short.MAX_VALUE)))
														.addGroup(
																gl_panelMap
																		.createSequentialGroup()
																		.addComponent(
																				btnNew)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				btnLoad)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				btnSave)))
										.addContainerGap()));
		gl_panelMap
				.setVerticalGroup(gl_panelMap
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panelMap
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_panelMap
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblHorizontal)
														.addComponent(
																comboHorizontal,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_panelMap
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																comboVertical,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblVertical))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_panelMap
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																comboDetail,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(lblDetail))
										.addPreferredGap(
												ComponentPlacement.RELATED, 8,
												Short.MAX_VALUE)
										.addGroup(
												gl_panelMap
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(btnSave)
														.addComponent(btnLoad)
														.addComponent(btnNew))
										.addContainerGap()));
		panelMap.setLayout(gl_panelMap);
	}

	/**
	 * load the goal panel
	 */
	private void loadPanelGoal() {
		panelGoal.setBorder(new TitledBorder(null, "goal",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelGoal.setBackground(Color.WHITE);

		JLabel lblTargetx = new JLabel("TargetX:");
		JLabel lblTargety = new JLabel("TargetY:");

		GroupLayout gl_panelGoal = new GroupLayout(panelGoal);
		gl_panelGoal
				.setHorizontalGroup(gl_panelGoal
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panelGoal
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_panelGoal
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_panelGoal
																		.createSequentialGroup()
																		.addComponent(
																				lblTargetx)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				comboTargetX,
																				0,
																				125,
																				Short.MAX_VALUE))
														.addGroup(
																gl_panelGoal
																		.createSequentialGroup()
																		.addComponent(
																				lblTargety)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				comboTargetY,
																				0,
																				125,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		gl_panelGoal
				.setVerticalGroup(gl_panelGoal
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panelGoal
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_panelGoal
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblTargetx)
														.addComponent(
																comboTargetX,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_panelGoal
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblTargety)
														.addComponent(
																comboTargetY,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap(21, Short.MAX_VALUE)));
		panelGoal.setLayout(gl_panelGoal);
	}

	/**
	 * set panel left GridBagLayout
	 */
	private void setGBLPanelLeft() {
		btnThink = new JButton("think");
		btnThink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 进度条
				// JProgress jp = new JProgress();
				// Thread td = new Thread(jp);
				// td.start();
				
				int targX = comboTargetX.getSelectedIndex() + 1;
				int targY = comboTargetY.getSelectedIndex() + 1;
				// 原地判断
				if (initX == targX && initY == targY) {
					JOptionPane.showMessageDialog(null, "The same place！",
							"KarelFF", JOptionPane.NO_OPTION);
					return;
				}

				char dir = ' ';
				switch (panelBlocks.getLoc().getDrt()) {
				case NORTH:
					dir = 'N';
					break;
				case EAST:
					dir = 'E';
					break;
				case SOUTH:
					dir = 'S';
					break;
				case WEST:
					dir = 'W';
					break;
				default:
					break;
				}

				String time = new Start().run(readInfo.getDirection(),
						readInfo.getFilename(), initX, initY, dir, targX, targY);

//				System.out.println(">>>> dir file");
//				System.out.println(readInfo.getDirection()+ " " + readInfo.getFilename());
//				System.out.println(">>>> initial state");
//				System.out.printf("(%s, %s) %s\n", initX, initY, dir);
//				System.out.println(">>>> target");
//				System.out.printf("(%s, %s)\n\n", targX, targY);

				// 设置进度条
				// if(jp.getI() < 96)
				// jp.setI(96);
				// // 关闭进度条
				// jp.setFlag(false);

				// 判断行走方法是否为空
				try {
					String filename = readInfo.getFilename() + "_problem_way";
					File map = new File(readInfo.getDirection(), filename);
					BufferedReader br;
					br = new BufferedReader(new FileReader(map));
					String fir = br.readLine();
					br.close();
					if (fir == null) {
						JOptionPane.showMessageDialog(null, "I can't reach！",
								"KarelFF", JOptionPane.NO_OPTION);
						return;
					}
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				JOptionPane.showMessageDialog(null, "Finished thinking！\n"
						+ time, "KarelFF", JOptionPane.NO_OPTION);

				btnRun.setEnabled(true);
			}
		});
		btnRun = new JButton("run");
		btnRun.setEnabled(false);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				btnRun.setEnabled(false);
//				frmKarelff.setResizable(false);
				int time = 1000 - (int) (readInfo.getSpeed() * 1000);
				panelBlocks.runKarel(time, readInfo.getDirection(),
						readInfo.getFilename() + "_problem_way");
				initX = comboTargetX.getSelectedIndex() + 1;
				initY = comboTargetY.getSelectedIndex() + 1;
			}
		});

		btnReset = new JButton("reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				btnRun.setEnabled(true);
				Timer t = panelBlocks.getaTimer();
				if (t != null) {
					t.cancel();
				}
				panelBlocks.setLoc(readInfo.getLoc());
				paintMap();

				initX = readInfo.getLoc().getBlk().getX();
				initY = readInfo.getLoc().getBlk().getY();
			}
		});

		GroupLayout gl_panelLeft = new GroupLayout(panelLeft);
		gl_panelLeft
				.setHorizontalGroup(gl_panelLeft
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panelLeft
										.createSequentialGroup()
										.addGap(32)
										.addGroup(
												gl_panelLeft
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																sliderSpeed,
																GroupLayout.DEFAULT_SIZE,
																209,
																Short.MAX_VALUE)
														.addComponent(
																panelMap,
																Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																panelGoal,
																Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(
																Alignment.TRAILING,
																gl_panelLeft
																		.createSequentialGroup()
																		.addComponent(
																				btnThink)
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addComponent(
																				btnRun)
																		.addPreferredGap(
																				ComponentPlacement.RELATED,
																				22,
																				Short.MAX_VALUE)
																		.addComponent(
																				btnReset)))
										.addContainerGap()));
		gl_panelLeft.setVerticalGroup(gl_panelLeft.createParallelGroup(
				Alignment.LEADING)
				.addGroup(
						gl_panelLeft
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(panelMap,
										GroupLayout.PREFERRED_SIZE, 173,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panelGoal,
										GroupLayout.PREFERRED_SIZE, 101,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(sliderSpeed,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(
										gl_panelLeft
												.createParallelGroup(
														Alignment.BASELINE)
												.addComponent(btnRun)
												.addComponent(btnThink)
												.addComponent(btnReset))
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		panelLeft.setLayout(gl_panelLeft);
	}

	/**
	 * set left panel GridBagConstraint
	 */
	private void setGBCPanelLeft() {
		GridBagConstraints gbc_panelLeft = new GridBagConstraints();
		gbc_panelLeft.insets = new Insets(0, 0, 0, 5);
		gbc_panelLeft.anchor = GridBagConstraints.WEST;
		gbc_panelLeft.gridx = 0;
		gbc_panelLeft.gridy = 0;
		frmKarelff.getContentPane().add(panelLeft, gbc_panelLeft);
	}

	/**
	 * load right panel
	 */
	private void loadPanelRight() {
		panelRight.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				paintWell();
			}
		});
		panelRight.setBackground(Color.WHITE);
		GridBagConstraints gbc_panelRight = new GridBagConstraints();
		gbc_panelRight.fill = GridBagConstraints.BOTH;
		gbc_panelRight.gridx = 1;
		gbc_panelRight.gridy = 0;
		frmKarelff.getContentPane().add(panelRight, gbc_panelRight);
		panelBlocks.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				panelBlocks.clearTempWall();
				panelBlocks.setInKarel(false);
				panelBlocks.repaint();
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if (setWallsPower) {
					panelBlocks.markWall();
				} else if (setKarelPower) {
					panelBlocks.markKarel();
				}
			}
		});
		panelBlocks.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (setWallsPower) {
					panelBlocks.overMap(e.getPoint());
				} else if (setKarelPower) {
					panelBlocks.locateKarel(e.getPoint());
				}
			}
		});

		panelBlocks.setBackground(Color.WHITE);
		GroupLayout gl_panelRight = new GroupLayout(panelRight);
		gl_panelRight.setHorizontalGroup(gl_panelRight.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panelRight
						.createSequentialGroup()
						.addGap(27)
						.addComponent(panelBlocks, GroupLayout.DEFAULT_SIZE,
								437, Short.MAX_VALUE).addContainerGap()));
		gl_panelRight.setVerticalGroup(gl_panelRight.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panelRight
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(panelBlocks, GroupLayout.DEFAULT_SIZE,
								437, Short.MAX_VALUE).addContainerGap()));
		panelBlocks.setLayout(new BorderLayout(0, 0));

		paintMap();

		panelRight.setLayout(gl_panelRight);

		initX = comboTargetX.getSelectedIndex() + 1;
		initY = comboTargetY.getSelectedIndex() + 1;
	}

	/**
	 * paint map
	 */
	private void paintMap() {
		paintBlocks();
		paintWalls();
		locateKarel();
	}

	/**
	 * paint blocks
	 */
	private void paintBlocks() {
		int numX = comboHorizontal.getSelectedIndex() + 1;
		int numY = comboVertical.getSelectedIndex() + 1;
		panelBlocks.paintBlock(numX, numY);
	}

	/**
	 * paint walls
	 */
	@SuppressWarnings("unchecked")
	private void paintWalls() {
		ArrayList<Wall> walls = (ArrayList<Wall>) readInfo.getSomeWalls()
				.clone();
		panelBlocks.delWalls();
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			panelBlocks.paintWall(w.getBlk().getX(), w.getBlk().getY(),
					w.getDrt());
		}
	}

	/**
	 * locate Karel
	 */
	private void locateKarel() {
//		Location loc = readInfo.getLoc();
		Location loc = panelBlocks.getLoc();
		if (loc == null)
			loc = readInfo.getLoc();
		panelBlocks.locateKarel(loc.getBlk().getX(), loc.getBlk().getY(),
				loc.getDrt());
	}

	/**
	 * save new map
	 */
	private void saveMap() {
		int horizontal = comboHorizontal.getSelectedIndex() + 1;
		int vertical = comboVertical.getSelectedIndex() + 1;
		readInfo.setDimension(horizontal, vertical);
		readInfo.setWalls(panelBlocks.getaWallList());
		readInfo.setLocation(panelBlocks.getLoc());

		comboTargetX.setModel(new DefaultComboBoxModel(
				setComboItems(horizontal)));
		int locX = readInfo.getLoc().getBlk().getX() - 1;
		comboTargetX.setSelectedIndex(locX);

		comboTargetY
				.setModel(new DefaultComboBoxModel(setComboItems(vertical)));
		int locY = readInfo.getLoc().getBlk().getY() - 1;
		comboTargetY.setSelectedIndex(locY);
	}

	/**
	 * repaint when change size
	 */
	private void paintWell() {
		// 求panelBlocks最大边长
		int height = panelRight.getHeight() - 21;
		int width = panelRight.getWidth() - 38;
		int numX = panelBlocks.getNumX();
		int numY = panelBlocks.getNumY();
		int max = (numX * height > numY * width ? width : height);
		panelBlocks.setMaxlen(max);
		// 设置起点
		panelBlocks.setHeightLen(height);
		panelBlocks.setWidthLen(width);

		paintMap();
	}

	// frame and panel
	private JFrame frmKarelff = new JFrame();
	private JPanel panelLeft = new JPanel();
	private JPanel panelGoal = new JPanel();
	private JPanel panelMap = new JPanel();
	private JPanel panelRight = new JPanel();
	private BlockPanel panelBlocks = new BlockPanel();

	// combo box
	private final int DEFAULTITEMS = 20;
	private JComboBox comboHorizontal;
	private JComboBox comboVertical;
	private JComboBox comboDetail;
	private JComboBox comboTargetX;
	private JComboBox comboTargetY;

	// button
	private JButton btnThink;
	private JButton btnRun;
	private JButton btnReset;

	// slider
	private JSlider sliderSpeed;

	// map
	private Map readInfo = new Map();

	// power
	private boolean setWallsPower;
	private boolean setKarelPower;

	// Karel initial pos
	private int initX = 1;
	private int initY = 1;
}
