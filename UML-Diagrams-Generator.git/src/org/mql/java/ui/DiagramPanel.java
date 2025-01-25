package org.mql.java.ui;

import org.mql.java.models.*;
import org.mql.java.models.Package;
import org.mql.java.services.RelationExplorer;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiagramPanel extends JPanel {
    private Project project;
    private Map<String, Point> nodePositions = new HashMap<>();

    public DiagramPanel(Project project) {
        this.project = project;
        setPreferredSize(new Dimension(2000, 2000)); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawPackages(g2d);
        drawRelations(g2d);
        drawNodes(g2d);
    }

    private void drawPackages(Graphics2D g2d) {
        int x = 100, y = 100;
        int verticalSpacing = 200;
        int horizontalSpacing = 300;
        int classVerticalSpacing = 20;

        for (Package pkg : project.getPackages()) {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(x, y, 150, 50);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, 150, 50);
            g2d.drawString(pkg.getName(), x + 10, y + 20);

            int classY = y + 100;
            for (Classe clazz : pkg.getClasses()) {
                nodePositions.put(clazz.getName(), new Point(x + 10, classY));
                classY += getClassHeight(clazz) + classVerticalSpacing;
            }
            for (Interface inter : pkg.getInterfaces()) {
                nodePositions.put(inter.getName(), new Point(x + 10, classY));
                classY += getClassHeight(inter) + classVerticalSpacing;
            }
            for (Enumeration enu : pkg.getEnumerations()) {
                nodePositions.put(enu.getName(), new Point(x + 10, classY));
                classY += getClassHeight(enu) + classVerticalSpacing;
            }
            for (Annotation ann : pkg.getAnnotations()) {
                nodePositions.put(ann.getName(), new Point(x + 10, classY));
                classY += getClassHeight(ann) + classVerticalSpacing;
            }

            y += verticalSpacing;
            x += horizontalSpacing;
        }
    }

    private int getClassHeight(Classe clazz) {
        int height = 50 + 15 * (clazz.getFields().size() + clazz.getMethods().size());
        return Math.max(height, 100);
    }

    private int getClassHeight(Interface inter) {
        int height = 50 + 15 * inter.getMethods().size();
        return Math.max(height, 100);
    }

    private int getClassHeight(Enumeration enu) {
        return 50 + 15 * enu.getConstants().size();
    }

    private int getClassHeight(Annotation ann) {
        return 50 + 15 * ann.getAttributes().size();
    }

    private void drawNodes(Graphics2D g2d) {
        for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
            String name = entry.getKey();
            Point position = entry.getValue();

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(position.x, position.y, 250, getClassHeightFromPosition(name));
            g2d.setColor(Color.BLACK);
            g2d.drawRect(position.x, position.y, 250, getClassHeightFromPosition(name));

            g2d.drawString(name, position.x + 10, position.y + 20);
            drawFieldsAndMethods(g2d, name, position.x, position.y + 30);
        }
    }

    private int getClassHeightFromPosition(String className) {
        for (Package pkg : project.getPackages()) {
            for (Classe clazz : pkg.getClasses()) {
                if (clazz.getName().equals(className)) {
                    return getClassHeight(clazz);
                }
            }
            for (Interface inter : pkg.getInterfaces()) {
                if (inter.getName().equals(className)) {
                    return getClassHeight(inter);
                }
            }
            for (Enumeration enu : pkg.getEnumerations()) {
                if (enu.getName().equals(className)) {
                    return getClassHeight(enu);
                }
            }
            for (Annotation ann : pkg.getAnnotations()) {
                if (ann.getName().equals(className)) {
                    return getClassHeight(ann);
                }
            }
        }
        return 100;
    }

    private void drawFieldsAndMethods(Graphics2D g2d, String className, int x, int y) {
        for (Package pkg : project.getPackages()) {
            for (Classe clazz : pkg.getClasses()) {
                if (clazz.getName().equals(className)) {
                    int fieldY = y;
                    for (String field : clazz.getFields()) {
                        g2d.drawString(field, x + 10, fieldY);
                        fieldY += 15;
                    }

                    int methodY = fieldY;
                    for (MethodInfos method : clazz.getMethods()) {
                        String methodSignature = method.getReturnType() + " " + method.getName() + "(" + method.getParameters() + ")";
                        g2d.drawString(methodSignature, x + 10, methodY);
                        methodY += 15;
                    }
                }
            }
            for (Interface inter : pkg.getInterfaces()) {
                if (inter.getName().equals(className)) {
                    int methodY = y;
                    for (MethodInfos method : inter.getMethods()) {
                        String methodSignature = method.getReturnType() + " " + method.getName() + "(" + method.getParameters() + ")";
                        g2d.drawString(methodSignature, x + 10, methodY);
                        methodY += 15;
                    }
                }
            }
            for (Enumeration enu : pkg.getEnumerations()) {
                if (enu.getName().equals(className)) {
                    int constantY = y;
                    for (String constant : enu.getConstants()) {
                        g2d.drawString(constant, x + 10, constantY);
                        constantY += 15;
                    }
                }
            }
            for (Annotation ann : pkg.getAnnotations()) {
                if (ann.getName().equals(className)) {
                    int fieldY = y;
                    for (String attribute : ann.getAttributes()) {
                        g2d.drawString(attribute, x + 10, fieldY);
                        fieldY += 15;
                    }
                }
            }
        }
    }

    private void drawRelations(Graphics2D g2d) {
        for (Package pkg : project.getPackages()) {
            RelationExplorer relationExplorer = new RelationExplorer(pkg.getName());
            List<Relation> relations = relationExplorer.genererRelations(project);

            for (Classe cls : pkg.getClasses()) {
                drawClasseRelations(g2d, cls, relations);
            }
            for (Interface inter : pkg.getInterfaces()) {
                drawInterfaceRelations(g2d, inter, relations);
            }
            for (Annotation ann : pkg.getAnnotations()) {
                drawAnnotationRelations(g2d, ann, relations);
            }
            for (Enumeration enu : pkg.getEnumerations()) {
                drawEnumerationRelations(g2d, enu, relations);
            }
        }
    }

    private void drawClasseRelations(Graphics2D g2d, Classe cls, List<Relation> relations) {
        relations.stream()
            .filter(relation -> relation.getSource().equals(cls.getName()) || relation.getDestination().equals(cls.getName()))
            .forEach(relation -> drawRelation(g2d, relation));
    }

    private void drawInterfaceRelations(Graphics2D g2d, Interface inter, List<Relation> relations) {
        relations.stream()
            .filter(relation -> relation.getSource().equals(inter.getName()) || relation.getDestination().equals(inter.getName()))
            .forEach(relation -> drawRelation(g2d, relation));
    }

    private void drawAnnotationRelations(Graphics2D g2d, Annotation ann, List<Relation> relations) {
        relations.stream()
            .filter(relation -> relation.getSource().equals(ann.getName()) || relation.getDestination().equals(ann.getName()))
            .forEach(relation -> drawRelation(g2d, relation));
    }

    private void drawEnumerationRelations(Graphics2D g2d, Enumeration enu, List<Relation> relations) {
        relations.stream()
            .filter(relation -> relation.getSource().equals(enu.getName()) || relation.getDestination().equals(enu.getName()))
            .forEach(relation -> drawRelation(g2d, relation));
    }

    private void drawRelation(Graphics2D g2d, Relation relation) {
        // Retrieve the start and end points of the relation based on the source and destination
        Point start = nodePositions.get(relation.getSource());
        Point end = nodePositions.get(relation.getDestination());

        g2d.drawLine(start.x, start.y, end.x, end.y);
        drawArrow(g2d, relation, start, end); // Pass relation to drawArrow
    }

    private void drawArrow(Graphics2D g2d, Relation relation, Point start, Point end) {
        int arrowSize = 10; // Reduced the size of the arrow
        double angle = Math.atan2(end.y - start.y, end.x - start.x);

        int x1 = end.x - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = end.y - (int) (arrowSize * Math.sin(angle - Math.PI / 6));

        int x2 = end.x - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = end.y - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        Polygon arrowHead = new Polygon(
            new int[]{end.x, x1, x2},
            new int[]{end.y, y1, y2},
            3
        );

        g2d.fillPolygon(arrowHead);
    }

   



}
