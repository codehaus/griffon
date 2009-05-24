/*
 * @( # ) SplashScreen . java 1.5 20-Jan-04
 * 
 * http://www.randelshofer.ch/oop/javasplash/Java%20Splash%20Screen.pdf
 *
 * Copyright (C) 2004, Roy Ratcliffe, Lancaster , United Kingdom.
 * All rights reserved .
 *
 * This software is provided ‘‘as is’’ without warranty of any kind ,
 * either expressed or implied . Use at your own risk. Permission to
 * use or copy this software is hereby granted without fee provided
 * you always retain this copy right notice .
 */

import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * SplashScreen is a general??purpose splash screen for application
 * start??up.  Usage is straight forward: simply construct a
 * SplashScreen at the start of main ( ) and call its splash ()
 * method. Proceed with start??up as normal.Use showStatus ( String ) f
 * or reporting progress during start??up.  Finally , at the
 * end of main() call SplashScreen’s dispose() method.By default
 * , the splash loads image splash.g i f but you can change this if
 * necessary.
 * 
 * <h3>Example 1</h3>
 * 
 * <pre>
 * class splasher1 {
 * public static void main ( String[ ] args ) {
 * SplashScreen splashScreen = new SplashScreen ( ) ;
 * splashScreen.splash ( ) ;
 * for ( int i = 10 ; i &gt; 0 ; i++) {
 * splashScreen.showStatus ( Integer.toString ( i ) + ”...” ) ;
 * try {
 * Thead.sleep (1000) ;
 * g
 * catch ( Inter ruptedEx cept ion i e ) f g
 * g
 * ; // frame.show() &lt;???? here
 * splashScreen.dispose ( ) ;
 * ; // frame.show() &lt;???? or here
 * g
 * g
 * </ pre>
 * 
 * <h3>Example 2</h3>
 * 
 * <pre>
 * class splasher2 {
 * public static void main ( String[ ] args ) {
 * SplashScreen splashScreen = new SplashScreen ( ) ;
 * 
 * splashScreen.splash ( ) ;
 * t r y {
 * Thread.sleep ( 5 0 0 ) ;
 * g
 * catch ( Inter ruptedEx cept ion i e ) f g
 * splashScreen.splashFor ( 1 0 0 0 ) ; // discretion
 * splashScreen.dispose ( ) ;
 * g
 * g
 * </ pre>
 * 
 * Note f o l l owi n g comments quoted from design documentation by R.R.
 * <blockquote> This example adds splashFor ( 1 0 0 0 ).I f the splash screen i
 * s al ready displayed , i t wai ts f o r at most 1000 mi l l i seconds before
 * r e t u r n i n g.Note t h i s means 1000 mi l l i seconds of t o t a l
 * splash ! So , i { al ready displayed f o r 1000 mi l l i seconds or more ,
 * the delay i s 0. In other words , i t avoids f l i c k e r i n g the splash
 * on then o f f i f the splash t iming coincides wi th a p p l i c a t i o n s
 * t a r t??up t iming.This i s a compromise between user feedback and c r u f
 * t.I f the splash takes longer than s t a r t??up , i t does not appear at a l
 * l.I f al ready di splay f o r a f r a c t i o n of the given t ime , the
 * delay i s <em>only</em> the remainder. </ blockquote>
 * 
 * <h3>Example 3</h3>
 * 
 * <pre>
 * class splasher3 {
 * p u b l i c s t a t i c void main ( St r i n g [ ] args ) {
 * SplashScreen splashScreen = new SplashScreen ( ) ;
 * splashScreen.splash ( ) ;
 * t r y {
 * Thread.sleep ( 1 0 0 ) ;
 * g
 * catch ( Inter ruptedEx cept ion i e ) f g
 * splashScreen.wai tForSplash ( ) ; // c r u f t zone?
 * splashScreen.splashFor (1000) ;
 * splashScreen.dispose ( ) ;
 * g
 * g
 * </ pre>
 * 
 * This example adds wai tForSplash.I t wai ts f o r the splash screen even
 * though the a p p l i c a t i o n loads f a s t e r.Not recommended as some
 * users consider t h i s bad p r a c t i c e.
 * 
 * <h3>Example 4</h3>
 * 
 * <pre>
 * class splasher4 {
 * p u b l i c s t a t i c void main ( St r i n g [ ] args ) {
 * SplashScreen.instance ( ).splash ( ) ;
 * SplashScreen.instance ( ).delayForSplash ( ) ;
 * t r y {
 * Thread.sleep ( 1 0 0 ) ;
 * 
 * g
 * catch ( Inter ruptedEx cept ion i e ) f g
 * SplashScreen.instance ( ).splashFor (1000) ;
 * SplashScreen.instance ( ).dispose ( ) ;
 * g
 * g
 * </ pre>
 * 
 * This example demonstrates two new features of version 1.5 SplashScreen.F i r
 * s t l y , the Singleton pat tern.Class??scoped method instance ( ) accesses
 * the s i n g l e SplashScreen instance.You can t h e r e f or e access t h i s
 * instance from anywhere.
 * <p>
 * Secondly , method delayForSplash ( ) appears j u s t a f t e r splash ( ).
 * This <em>possibly</em> delays the main thread , allowing the splash
 * screen to load and display.Tests on some uniprocessor platforms show poor
 * multi??threading performance.See Appendix F of design documentation by
 * R.R. The new method bases the extent of delay i { any on number of avail
 * a b l e computing resources.
 * 
 * <h3>Modelling</h3>
 * In U.M. L. modelling terms , SplashScreen fulfils the following
 * requirement depicted as a Use Case.
 * <p>
 * <img src=”UseCaseDiagram1.g i f ”>
 * <p>
 * The sketch below outlines the user interface design.
 * <p>
 * <img src=”hci.gif”>
 * <p>
 * To meet this requirement , the implementation uses the following
 * class design.
 * <p>
 * <img src=” ClassDiagram2.gif”>
 * <p>
 * Or in full detail as follows.
 * <p>
 * <img src=”ClassDiagram2a.gif”>
 * <p>
 * 
 * @todo Add method or methods for adjusting background colours.
 * @author Roy Ratcliffe
 * @version 1.5
 */
public class SplashScreen implements ImageObserver {

	// Design decision.
	// Choose delegation over inheritance. SplashScreen is not a
	// Frame or a Window , or an Image for that matter ; it is a
	// concept.Frame and Image are its components.
	//
	// Conceptually , the splash screen is an image with text
	// underneath : an image and a label in Java terms.The Frame

	// is a somewhat more abstract software??engineering
	// entity.
	//
	// Instantiate the label now and give default
	// contents.Use
	// method showStatus ( s : String ) to override the default. You
	// can
	// call this before splash ( ).Design feature.
	private Image image;
	//private Label label = new Label("Loading...", Label.CENTER);
	private JLabel label = new JLabel("<html><br><span style=\"font-size:large;\">Loading...</span><br><br></html>", SwingConstants.CENTER);

	private Frame frame;
	private long splashTime = 0;

	/**
	 * Constructs SplashScreen using a given filename for the splash image.
	 * 
	 * @param filename name of an image file
	 */
	public SplashScreen(String filename) {
		setImage(filename);
	}

	/**
	 * Constructs SplashScreen using a given URL f o r the splash image.
	 * 
	 * @param url the URL of an image
	 */
	public SplashScreen(URL url) {
		setImage(url);
	}

	/**
	 * Constructs SplashScreen using filename ” splash.gif ” for the
	 * image unless you change the default using setImage or call
	 * splash with an argument specifying a different image.
	 */
	public SplashScreen() {
	}
	
	
	private static SplashScreen instance;
	public static SplashScreen getInstance() {
		if (instance == null) {
			instance = new SplashScreen();
			URL url = SplashScreen.class.getResource("splash.png");
			instance.setImage(url);
		}
		return instance;
	}

	/**
	 * Uses the given filename for the splash image.This method calls 
	 * Toolkit.getImage which resolves mu l t i p l e requests f o r the
	 * same filename to the same Image , u n l i k e createImage which creates
	 * a non??shared instance of Image.In other words , getImage caches Images ,
	 * createImage does not.Use <code>splash ( createImage (</ code >... i f you
	 * want Image privacy.
	 * 
	 * @param filename name of an image file
	 */
	public void setImage(String filename) {
		image = Toolkit.getDefaultToolkit().getImage(filename);
	}

	/**
	 * Uses the given URL f o r the splash image.
	 * 
	 * @param u
	 *            r l the URL of an image
	 */
	public void setImage(URL url) {
		image = Toolkit.getDefaultToolkit().getImage(url);

	}

	/**
	 * St a r t s the asynchronous splash screen using the given filename f o r
	 * the image.
	 * 
	 * @param filename
	 *            name of an image f i l e
	 */
	public void splash(String filename) {
		splash(Toolkit.getDefaultToolkit().getImage(filename));
	}

	/**
	 * St a r t s the asynchronous splash screen using the given URL f o r the
	 * image.
	 * 
	 * @param u
	 *            r l the URL of an image
	 */
	public void splash(URL url) {
		splash(Toolkit.getDefaultToolkit().getImage(url));
	}

	/**
	 * St a r t s the asynchronous splash screen using the prev ious l y s p e c
	 * i f i e d image , or using filename ” splash.g i f ” by d e f a u l t i {
	 * no image yet s p e c i f i e d.
	 */
	public void splash() {
		if (image != null)
			splash(image);
		else
			splash(Toolkit.getDefaultToolkit().getImage("splash.gif")); // async
	}

	/**
	 * <em>Splash</em> the screen ! Or , i n other words , make a splash !
	 * 
	 * Ac t u a l l y , t h i s method merely s t a r t s the process o{
	 * splashing.The splash screen w i l l appear sometime l a t e r when the
	 * splash image i s ready f o r di splay.
	 * 
	 * Note t h a t t h i s splash screen implementat ion uses f u l l y
	 * asynchronous image loading.The splash ( ) method i t s e l f returns to
	 * the c a l l e r as soon as pos s ible.The screen appears l a t e r as
	 * soon as image loading completes.
	 * 
	 * @pre Do not double??splash ! I t creates waves ! That i s , do not invoke
	 *      splash ( ) twice , not wi thout c a l l i n g dispose ( ) i
	 *      n??between.
	 * 
	 * @param img
	 *            the image used f o r splashing
	 */
	public void splash(Image img) {
		image = img;
		frame = new Frame();
		frame.setUndecorated(true);

		if (!Toolkit.getDefaultToolkit().prepareImage(image, -1, -1, this))
			return;
		// Ar r i v i n g here means the image i s al ready f u l l y loaded.

		// The splash screen can proceed wi thout delay.
		splashScreen();
	}

	/**
	 * Runs dur ing image loading.Informs t h i s ImageObserver about progress.
	 * This method does <b>not</b> run i n the main thread , i.e.the thread
	 * which c a l l s splash ( ) and dispose ( ).That i s why methods
	 * splashScreen ( ) and dispose ( ) are <code>synchronized</code>.
	 */
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// debug...
		// System. e r r.p r i n t l n ( ” img= ” + img +
		// ” , i n f o f l a g s = ” + i n f o f l a g s +
		// ” , x= ” + x +
		// ” , y= ” + y +
		// ” , width= ” + width +
		// ” , height= ” + height ) ;

		// Return f a l s e i f i n f o f l a g s i n d i c a t e t h a t the
		// image i s
		// completely loaded ; t rue otherwise.
		boolean allbits = infoflags == ImageObserver.ALLBITS;
		if (allbits)
			splashScreen();
		return !allbits;
	}

	/**
	 * Runs when the splash image i s f u l l y loaded , a l l i t s b i t s and
	 * pieces.
	 * 
	 * @todo Animated splash screens ! I s there a requi rement ? I { so ,
	 *       implement animat ion suppor t.
	 */
	private synchronized void splashScreen() {
		// Which thread runs t h i s method ? One of two : e i t h e r the
		// main thread i f the image has al ready loaded , or Java ’ s
		// image loader thread.
		if (frame == null)
			return;
		final int width = image.getWidth(null);
		final int height = image.getHeight(null);

		// Why use a Canvas ? I t al lows packing.This way , AWT’ s
		// normal packing mechanism handles the s i z i n g and layout.
		Canvas canvas = new Canvas() {
			// Fix thanks to Werner Randelshofer as f o l l ows.
			// Canvas class ’ update ( g ) method f i r s t c lear s then
			// invokes p ai n t ( g ).I t s superclass , Component , only
			// c a l l s p ai n t ( g ).Just pa i nt , not c le a r ! Clear ing
			// f i r s t unhappi ly creates f l i c k e r.The f o l l owi n g
			// over r ide r e v e r t s to the super??superclass behaviour
			// of Component.

			public void update(Graphics g) {
				paint(g);
			}

			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, this);
			}

			// Fixed f o r Mac OS X , also thanks to Werner.
			// Werner ’ s kind t e s t i n g on Apple ’ s JVM reveals an
			// impor tant and subt le d i f f e r e n c e versus Sun ’ s Java f
			// o r
			// Linux.Under Linux , Component.setSize ( ) a l t e r s the
			// Canvas ‘ ‘ prefer red size ’ ’ and t h e r e f o r e a f f e c t
			// s
			// layout.Under Mac OS X , i t does not.Ac t u a l l y ,
			// reviewing Component.java source reveals t h a t
			// prefer redSi ze ( ) dependency i s complex , depending on
			// prefSi ze a t t r i b u t e i f set and Component i sVa l i d ,
			// or
			// otherwise depends on Component peer , or final l y the
			// ‘ ‘ minimum s ize ’ ’ i f no peer yet.Werner ’ s s o l u t i o n
			// seems advisable t h e r e f o r e : over r ide
			// getPrefer redSize ( ) method.
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};

		frame.add(canvas, BorderLayout.CENTER);
		frame.add(label, BorderLayout.SOUTH);
		frame.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		frame.setLocation((screenSize.width - frameSize.width) >> 1, // / 2
				(screenSize.height - frameSize.height) >> 1); // / 2
		frame.show();
		splashTime = System.currentTimeMillis();
	}

	/**
	 * Changes the s tatus message j u s t below the image.Use i t to di splay s
	 * t a r t??up progress.
	 */
	public void showStatus(String s) {
		label.setText(s);
	}

	/**
	 * Waits f o r the splash screen to load , returns when the splash s t a r t
	 * s.The wai t i s i n d e f i n i t e i f necessary.The operat ion returns
	 * immediately i f the splash image has al ready loaded.
	 * <p>
	 * Please note f o l l owi n g discussion taken from design documentation by
	 * R.R. <blockquote>
	 * 
	 * As a guide , invoke t h i s method at the <em>end</em> of s t a r t??up ,
	 * not the beginning.Wai t ing f o r the image to load does not make the
	 * image load f a s t e r , neces sar i l y.Image loading i s an ‘ i np u t
	 * bound ’ process , reading from f i l e s y s t em or network.Remaining s
	 * t a r t??up steps are t y p i c a l l y ‘ compute bound ’ and l i k e l y
	 * compute resource i s a v a i l a b l e f o r consumption.Most l i k e l y
	 * , s t a r t??up mixes i np u t and compute resource demands , and pos s
	 * ibl y even output.
	 * <p>
	 * This g u i d e l i n e appl ies to uniprocessor as wel l as mul t iproces
	 * sor plat forms.Wai t ing only wastes any a v a i l a b l e compute
	 * cycles.I f you need to delay unnecessar i ly , do t h i s at the end when
	 * there i s nothing l e f t to do.Even so , t h i s p r a c t i c e can be
	 * viewed as user i n t e r f a c e c r u f t ! Please use wi th care. </
	 * blockquote>
	 */
	public void waitForSplash() {
		MediaTracker mt = new MediaTracker(frame);
		mt.addImage(image, 0);
		try {
			mt.waitForID(0);
		} catch (InterruptedException ie) {
		}
		// as ser t splashTime !=0
	}

	/**
	 * Waits f o r the splash screen to load f o r a l imi t e d amount o{ t
	 * ime.Method returns when the splash has loaded , or when the given t ime l
	 * imi t expi res.
	 * 
	 * @param ms
	 *            mi l l i seconds to wai t f o r
	 */
	public void waitForSplash(long ms) {
		MediaTracker mt = new MediaTracker(frame);
		mt.addImage(image, 0);
		try {
			mt.waitForID(0, ms);
		} catch (InterruptedException ie) {
		}
		// as ser t splashTime !=0
	}

	/**
	 * Optimise splash latenc y by delaying the c a l l i n g thread according
	 * to number of processors a v a i l a b l e.Mul t iproces sor plat forms s
	 * u c c e s s f u l l y load the splash image i n p a r a l l e l wi th low
	 * overhead.Uniprocessors s t ruggle however ! This method o f f e r s
	 * compromise.I t delays <em>i n d e f i n i t e l y </em> wi th one
	 * processor , same as wai tForSplash ( ) ; however , i t returns
	 * immediately wi th four our more processors thereby maximising p a r a l l
	 * e l execut ion ; or wai ts f o r 500 mi l l i seconds at most wi th dual
	 * processors.Cal l delayForSplash ( ) i n place o{ wai tForSplash ( ).
	 */
	public void delayForSplash() {

		int cpus = Runtime.getRuntime().availableProcessors();
		switch (cpus) {
		case 0: // pathology !
		case 1:
			waitForSplash();
			break;
		case 2:
		case 3: // ?
			waitForSplash(1000 / cpus);
		}
	}

	/**
	 * Splashes the screen f o r at l e a s t the given number o{ mi l l i
	 * seconds i f , and only i f , the splash screen has al ready loaded.I f
	 * not al ready splashed , the method returns immediately.Invoke t h i s
	 * method before disposing i f you want to for ce a minimum splash per iod.
	 * <p>
	 * Why i s t h i s method <code>synchronized</code>? In order to avoid a
	 * race c o n d i t i o n.I t accesses the splashTime a t t r i b u t e
	 * which updates i n another thread.
	 * 
	 * @param ms
	 *            mi l l i seconds of minimum splash
	 * @pre The argument i s greater than zero. You al ready c a l l e d splash.
	 */
	public synchronized void splashFor(int ms) {
		if (splashTime == 0)
			return;
		long splashDuration = System.currentTimeMillis() - splashTime;
		// What t ime does System. c u r r e n tTimeMi l l i s measure ? Real
		// t ime , process t ime , or perhaps thread t ime ? I f r e a l t ime ,
		// the f o l l owi n g sleep durat ion i n a c c u r a t e l y
		// represents the
		// remaining delay.This process could swi tch out at any
		// p oi n t i n??between sampling the t ime and sleeping , and
		// thereby add to the e x i s t i n g delay ! Ignored f o r now.
		if (splashDuration < ms)
			try {
				Thread.sleep(ms - splashDuration);
			} catch (InterruptedException ie) {
			}
	}

	/**
	 * Closes the splash screen i f open , or abandons splash screen i { not al
	 * ready open.Re l a t i v e l y long image loading delays the opening.Cal l
	 * t h i s method at the end of program s t a r t??up , i.e.t y p i c a l l
	 * y at the end of main ( ).
	 * <p>
	 * Implementat ion note. I f you dispose too f a s t , t h i s method could
	 * coinc ide wi th splashScreen ( ).We cannot now preempt the other thread.I
	 * t needs s ynchroni sat ion.This s i t u a t i o n and i t s requi rement
	 * proves i n e v i t a b l e when two threads access the same t h in g.For
	 * 
	 * t h i s reason , methods dispose ( ) and splashScreen ( ) share the
	 * <code>synchronized</code> a t t r i b u t e.
	 * 
	 * @pre Assumes previous invocat ion of splash ( ).Do not dispose before the
	 *      splash !
	 */
	public synchronized void dispose() {
		// Of course , i t i s conceivable though u n l i k e l y t h a t the
		// frame may not r e a l l y e x i s t before disposing.For example ,
		// i f the splash phase of i n t i a l i s a t i o n runs very q u i c k
		// l y.
		// Two splash cycles i n a row? Remove the label ready f o r
		// the next i t e r a t i o n.Not a requi rement but p a t h o l o g i c
		// a l l y safe.
		frame.remove(label);
		frame.dispose();
		frame = null;
		splashTime = 0;
	}

	private static SplashScreen singleton = null;

	/**
	 * Ensures the class has only one instance and gives a global p oi n t of
	 * access to i t.The Singleton pat tern avoids using global v a r i a b l e
	 * s.Instead , the class i t s e l f references the s i n g l e instance
	 * using a class??scoped v a r i a b l e , <em>s t a t i c </em> i n Java
	 * terms.
	 * <p>
	 * The implementat ion a c t u a l l y mixes s i n g l e t o n and non??s i
	 * n g l e t o n pat terns.( Tempting to c a l l i t Mu l t i t o n but t h
	 * a t r e f e r s to a v a r i a t i o n of Singleton where the instance
	 * has <em>many</em> mu l t i p l i c i t y instead of u n i t y.) Co r r e
	 * c t l y applying the Singleton pat tern requi res c los ing access to
	 * cons t ruc tor methods.However , SplashScreen r e t a i n s p u b l i c
	 * cons t ruc tor s , so compromises the pat tern.You can f o l l ow
	 * Singleton usage or not at your own d i s c r e t i o n.
	 * 
	 * @return s i n g l e t o n SplashScreen instance
	 */
	// See Double??checked loc k ing and the Singleton pat tern
	// h t t p : //www??106.ibm. com/ developerworks / java / l i b r a r y /
	// j??dc l.html?dwzone=java
	public static synchronized SplashScreen instance() {
		if (null == singleton)
			singleton = new SplashScreen();
		return singleton;
	}

}
