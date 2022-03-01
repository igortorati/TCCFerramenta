package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ui.Constants.Pencil;

public class ConfigurationPanel extends JPanel{
	private Parameters parameters;
	
	private JLabel delayLabel;
	private JSlider delaySlider;
	private JLabel cellSizeLabel;
	private JSlider cellSizeSlider;
	private JRadioButton obstacleRadioOption;
	private JRadioButton startRadioOption;
	private JRadioButton destinationRadioOption;
	private JRadioButton eraserRadioOption;
	private ButtonGroup radioButtonGroup;
	private JButton startButton;
	private JButton stopButton;
	private JButton cleanMatrixButton;
	private JButton saveMatrixButton;
	private JButton loadMatrixButton;
	private JButton saveResults;
	
	public ConfigurationPanel(Parameters parameters) {
		super();
		this.parameters = parameters;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(250, 975));
		
		addConfigurationComponents();
		setVisible(true);
	}

	private void addConfigurationComponents() {
		addRadioOptions();
		addStartButton();
		addCleanMatrixButton();
		addCellSizeSlider();
		addDelaySlider();
		addSaveMatrixButton();
		addLoadMatrixButton();
		addSaveResultsButton();
	}
	
	private void addDelaySlider() {
		delaySlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 50);
		delaySlider.setMajorTickSpacing(200);
		delaySlider.setMinorTickSpacing(50);
		delaySlider.setPaintTicks(true);
		delaySlider.setPaintLabels(true);
		delaySlider.setAlignmentX(this.CENTER_ALIGNMENT);
		delaySlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				parameters.setAnimationDelay(delaySlider.getValue() > 0 ? delaySlider.getValue() : 1);
				delayLabel.setText(getDelayLabel());
			}
		});
		
		delayLabel = new JLabel(getDelayLabel());
		delayLabel.setAlignmentX(this.CENTER_ALIGNMENT);
		delayLabel.setBorder(new EmptyBorder(10, 10, 0, 10));
		add(delayLabel);
		add(delaySlider);
	}

	private String getDelayLabel() {
		return "Delay da Animação: " + delaySlider.getValue() + "ms";
	}

	private void addRadioOptions() {
		ActionListener listener = new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if(e.getSource() == obstacleRadioOption) {
	            	parameters.setPencil(Pencil.OBSTACLE);
	            } else if(e.getSource() == startRadioOption) {
	            	parameters.setPencil(Pencil.START);
	            } else if(e.getSource() == destinationRadioOption) {
	            	parameters.setPencil(Pencil.DESTINATION);
	            } else if(e.getSource() == eraserRadioOption) {
	            	parameters.setPencil(Pencil.ERASER);
	            }
	        }
	    };
	    
		radioButtonGroup = new ButtonGroup();
		
		obstacleRadioOption = new JRadioButton();
		obstacleRadioOption.setText("Obstáculo");
		obstacleRadioOption.setSelected(true);
		obstacleRadioOption.setAlignmentX(this.CENTER_ALIGNMENT);
		radioButtonGroup.add(obstacleRadioOption);
		add(obstacleRadioOption);
		obstacleRadioOption.addActionListener(listener);

		eraserRadioOption = new JRadioButton();
		eraserRadioOption.setText("Apagar");
		eraserRadioOption.setAlignmentX(this.CENTER_ALIGNMENT);
		radioButtonGroup.add(eraserRadioOption);
		add(eraserRadioOption);
		eraserRadioOption.addActionListener(listener);
		
		startRadioOption = new JRadioButton();
		startRadioOption.setText("Início");
		startRadioOption.setAlignmentX(this.CENTER_ALIGNMENT);
		radioButtonGroup.add(startRadioOption);
		add(startRadioOption);
		startRadioOption.addActionListener(listener);
		
		destinationRadioOption = new JRadioButton();
		destinationRadioOption.setText("Destino");
		destinationRadioOption.setAlignmentX(this.CENTER_ALIGNMENT);
		destinationRadioOption.setBorder(new EmptyBorder(0, 0, 10, 0));
		radioButtonGroup.add(destinationRadioOption);
		add(destinationRadioOption);
		destinationRadioOption.addActionListener(listener);
	}

	private void addStartButton() {
		add(Box.createRigidArea(new Dimension(getWidth(), 10)));
		startButton = new JButton("Buscar Caminho");
		startButton.setAlignmentX(this.CENTER_ALIGNMENT);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				firePropertyChange(Constants.StartRequested, null, null);
			}
		});
		
		add(startButton);
	}
	
	private void addCellSizeSlider() {
		cellSizeSlider = new JSlider(JSlider.HORIZONTAL, 5, 100, 15);
		cellSizeSlider.setMajorTickSpacing(10);
		cellSizeSlider.setMinorTickSpacing(5);
		cellSizeSlider.setPaintTicks(true);
		cellSizeSlider.setPaintLabels(true);
		cellSizeSlider.setAlignmentX(this.CENTER_ALIGNMENT);
		cellSizeSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				parameters.setCellSize(cellSizeSlider.getValue());
				cellSizeLabel.setText(getCellSizeLabel());
				firePropertyChange(Constants.CellSizeChanged, null, null);
			}
		});
		
		cellSizeLabel = new JLabel(getCellSizeLabel());
		cellSizeLabel.setBorder(new EmptyBorder(10, 10, 0, 10));
		cellSizeLabel.setAlignmentX(this.CENTER_ALIGNMENT);
		add(cellSizeLabel);
		add(cellSizeSlider);
	}
	
	private String getCellSizeLabel() {
		return "Tamanho da Célula: " + cellSizeSlider.getValue() + "px";
	}

	private void addCleanMatrixButton() {
		add(Box.createRigidArea(new Dimension(getWidth(), 10)));
		cleanMatrixButton = new JButton("Limpar Matriz");
		cleanMatrixButton.setAlignmentX(this.CENTER_ALIGNMENT);
		cleanMatrixButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				firePropertyChange(Constants.ClearMatrix, null, null);
			}
		});
		
		add(cleanMatrixButton);
	}
	
	private void addSaveMatrixButton() {
		add(Box.createRigidArea(new Dimension(getWidth(), 10)));
		saveMatrixButton = new JButton("Salvar Matriz");
		saveMatrixButton.setAlignmentX(this.CENTER_ALIGNMENT);
		ConfigurationPanel configPanel = this;
		saveMatrixButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");   
				 
				int userSelection = fileChooser.showSaveDialog(configPanel);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooser.getSelectedFile();
					firePropertyChange(Constants.SaveMatrix, null, fileToSave.getAbsolutePath());
				}
			}
		});
		
		add(saveMatrixButton);
	}
	
	private void addLoadMatrixButton() {
		add(Box.createRigidArea(new Dimension(getWidth(), 10)));
		loadMatrixButton = new JButton("Carregar Matriz");
		loadMatrixButton.setAlignmentX(this.CENTER_ALIGNMENT);
		ConfigurationPanel configPanel = this;
		loadMatrixButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");   
				 
				int userSelection = fileChooser.showSaveDialog(configPanel);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToRead = fileChooser.getSelectedFile();
					firePropertyChange(Constants.LoadMatrix, null, fileToRead.getAbsolutePath());
				}
			}
		});
		
		add(loadMatrixButton);
	}
	
	private void addSaveResultsButton() {
		add(Box.createRigidArea(new Dimension(getWidth(), 10)));
		saveResults = new JButton("Salvar Resultados");
		saveResults.setAlignmentX(this.CENTER_ALIGNMENT);
		saveResults.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				firePropertyChange(Constants.SaveResults, null, null);
			}
		});
		
		add(saveResults);
	}
}
