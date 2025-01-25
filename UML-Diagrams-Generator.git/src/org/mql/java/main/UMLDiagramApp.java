package org.mql.java.main;

import org.mql.java.models.Project;
import org.mql.java.services.ProjectExplorer;
import org.mql.java.ui.DiagramPanel;

import javax.swing.*;

public class UMLDiagramApp extends JFrame {
    private Project project;

    public UMLDiagramApp(String projectPath) {
        setTitle("UML Diagram ");
         setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ProjectExplorer projectExplorer = new ProjectExplorer(projectPath);
        project = projectExplorer.getProject();

        DiagramPanel diagramPanel = new DiagramPanel(project);
        add(new JScrollPane(diagramPanel));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UMLDiagramApp frame = new UMLDiagramApp("src"); 
            frame.setVisible(true);
        });
    }
}
