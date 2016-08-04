/**
 * 
 */
package utiliter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * @author Tiago A. Teixeira {@link tiago.a.teixeira@acm.org}
 *
 */
public class Mover extends MouseAdapter {

	private Dimension snapSize = new Dimension(1, 1);
	private Component source;
	private Point pressed;
	private boolean potentialDrag;
	private static Mover instance;
	private Point locationBeforeMove;

	private Mover() {
	}

	public void deregisterComponent(Component... components) {
		for (Component component : components) {
			component.removeMouseListener(this);
		}
	}

	public void registerComponent(Component... components) {
		for (Component component : components) {
			component.addMouseListener(this);
		}
	}

	public Dimension getSnapSize() {
		return snapSize;
	}

	public void setSnapSize(Dimension snapSize) {
		if (snapSize.width < 1 || snapSize.height < 1) {
			throw new IllegalArgumentException("snapSize deve ser maior que 0");
		}
		this.snapSize = snapSize;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}
		if (e.isConsumed()) {
			return;
		}
		setupForDragging(e);
	}

	private void setupForDragging(MouseEvent e) {
		source = e.getComponent();
		source.addMouseMotionListener(this);
		potentialDrag = true;
		pressed = e.getLocationOnScreen();
		locationBeforeMove = source.getLocation();
	}

	/**
	 * Move the component to its new location. The dragged Point must be in the
	 * destination coordinates.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}
		if (e.isConsumed()) {
			return;
		}
		Point dragged = e.getLocationOnScreen();
		int dragX = getDragDistance(dragged.x, pressed.x, snapSize.width);
		int dragY = getDragDistance(dragged.y, pressed.y, snapSize.height);
		Component source = e.getComponent();
		Point location = locationBeforeMove;
		if (location == null) {
			return;
		}
		int locationX = location.x + dragX;
		int locationY = location.y + dragY;
		while (locationX < 0) {
			locationX += snapSize.width;
		}
		while (locationY < 0) {
			locationY += snapSize.height;
		}
		Dimension d = getBoundingSize(source);
		while (locationX + source.getSize().width > d.width) {
			locationX -= snapSize.width;
		}
		while (locationY + source.getSize().height > d.height) {
			locationY -= snapSize.height;
		}
		source.setLocation(locationX, locationY);
	}

	private int getDragDistance(int larger, int smaller, int snapSize) {
		int halfway = snapSize / 2;
		int drag = larger - smaller;
		drag += (drag < 0) ? -halfway : halfway;
		drag = (drag / snapSize) * snapSize;
		return drag;
	}

	private Dimension getBoundingSize(Component source) {
		if (source instanceof Window) {
			GraphicsEnvironment env = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			Rectangle bounds = env.getMaximumWindowBounds();
			return new Dimension(bounds.width, bounds.height);
		} else {
			return source.getParent().getSize();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}
		if (!potentialDrag) {
			return;
		}
		source.removeMouseMotionListener(this);
		potentialDrag = false;
	}

	public static Mover getInstance() {
		if (instance == null) {
			instance = new Mover();
		}
		return instance;
	}
}
