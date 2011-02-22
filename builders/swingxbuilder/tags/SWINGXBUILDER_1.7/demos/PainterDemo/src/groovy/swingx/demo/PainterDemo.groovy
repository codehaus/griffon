/*
 * PainterDemo.groovy
 * Created May 24, 2007
 *
 */
package groovy.swingx.demo

import groovy.swing.SwingXBuilder
import java.awt.Color
import java.awt.Font
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.SwingConstants
import javax.swing.WindowConstants
import net.miginfocom.swing.MigLayout
import org.jdesktop.swingx.painter.GlossPainter
import org.jdesktop.swingx.painter.MattePainter


//import com.jhlabs.image.*
/*
 *
 * author jwill
 */
def swing = new SwingXBuilder()

// Text strings
def purchaseText = "Tried Catalog? Liked it? Now that you're convinced "+
    "that living without Catalog would be horrible, purchase it for "+
    "yourself and use it forever!"
def downloadText = "If you have Mac OS X 10.3 or later, you can grab a "+
    "copy of Catalog right now and take it for a spin. At 816KB it's not "+
    "only the best way to index your discs, but the most lightweight way."
def screenshotsText = "View a high quality, detailed screenshot of "+
    "Catalog. Once you start drooling, click the button above and download."
def coolProduct  = "Introducing: Our Cool Product"
def coolProduct2 = "Having a bit of trouble shuffling through a ton of "+
    "disks, trying to find the one that had that one document from way "+
    "back when? Catalog allows you to find the file you're looking for "+
    "without wearing out your disc drive (or your arm). Simply pop in a "+
    "disk, drop it onto Catalog, and watch as it is indexed \u2013 "+
    "virtually representing every file and tons of information about it. "+
    "Then, when you want to find the file, search for it in Catalog and "+
    "you find out exactly where it is, even if the disc isn't in your drive."
def dude  = "But Dude! The Disc Isn't In The Drive!"
def dude2 ="Catalog isn't supposed to feel like an extension of your disc"+
    "browsing needs \u2013 rather it seamlessly blends offline disc "+
    "browsing into your work environment. Browsing in Catalog feels just "+
    "like browsing in the Finder \u2013 your files are right there in "+
    "front of you. Once you find the file you want, just look to see where "+
    "it is, pop in that disk, and open it up. Whether you are cataloging "+
    "your backup, your music collection, family recipes, or your super "+
    "secret world domination plans, Catalog will work exactly how it should."

def compoundPaint = swing.compoundPainter() {
        mattePainter(fillPaint:new Color(51,51,51))
        pinstripePainter(paint:new Color(1.0f,1.0f,1.0f,0.17f),
            spacing:5.0f)
        glossPainter(paint:new Color(1.0f,1.0f,1.0f,0.2f),
            position:GlossPainter.GlossPosition.TOP)
    }

swing.frame(title:"Groovy SwingXBuilder Painter Demo",
    background:Color.WHITE, resizable:false,
    layout:new MigLayout("insets 0 0 0 0"),
    defaultCloseOperation:WindowConstants.DISPOSE_ON_CLOSE,
    show:true,
    pack:true,
) {
    panel( backgroundPainter:compoundPaint, background:new Color(51,51,51), layout:new MigLayout(), constraints:"north, w 522") {
        label(name:'shield',icon:new ImageIcon(ImageIO.read(new File("shield.png"))))
        label(font:new Font("Tahoma", 1,18), foreground:Color.WHITE, text:"UTAH BOY SOFTWARE", constraints:"x 80, y 20")
        label(font:new Font("Tahoma",0,14), foreground:Color.WHITE, text:"Better tools.", constraints:"x 80, y 50")
    }
    panel(backgroundPainter:new MattePainter(new Color(51,51,51)), layout:new MigLayout(), border:BorderFactory.createLineBorder(new Color(102,102,102)), constraints:"newline, north") {
        label(font:new Font("Lucinda Grande",0,9), text:"PRODUCTS", foreground:Color.WHITE, horizontalAlignment:SwingConstants.CENTER, constraints:"gapleft 9")
        label(font:new Font("Lucinda Grande",0,9), text:"STORE", foreground:Color.WHITE, horizontalAlignment:SwingConstants.CENTER,constraints: "gapleft 23")
        label(font:new Font("Lucinda Grande",0,9), text:"ABOUT", foreground:Color.WHITE, horizontalAlignment:SwingConstants.CENTER,constraints: "gapleft 29")
        label(font:new Font("Lucinda Grande",0,9), text:"NEWS", foreground:Color.WHITE, horizontalAlignment:SwingConstants.CENTER,constraints: "gapleft 33")
        label(font:new Font("Lucinda Grande",0,9), text:"SUPPORT", foreground:Color.WHITE, horizontalAlignment:SwingConstants.CENTER,constraints: "gapleft 28")
        label(font:new Font("Lucinda Grande",0,9), text:"GOODIES", foreground:Color.WHITE, horizontalAlignment:SwingConstants.CENTER,constraints: "gapleft 27")
    }
    label(icon:new ImageIcon(ImageIO.read(new File("title.png"))), constraints:"north")
    separator(constraints:"w 2, h 500, gap 0", background:new Color(99,130,191), orientation:SwingConstants.VERTICAL)
    panel(layout:new MigLayout("insets 0 0 0 0")) {
        // Purchase Panel
        panel(backgroundPainter:new MattePainter(new Color(240,239,239)), layout:new MigLayout(), constraints:"gap 0, wrap") {
            label(font:new Font("Tahoma",1,11), foreground:new Color(51,102,255), text:"Purchase", constraints:"wrap")
            textArea(columns:20, constraints:"align left", font:new Font("Tahoma",0,11), lineWrap:true, rows:5, text:purchaseText, wrapStyleWord:true, border:null,
                disabledTextColor:new Color(0,0,0), opaque:false, enabled:false, selectedTextColor:Color.WHITE, selectionColor:Color.BLACK)
        }

        // Download panel
        panel(backgroundPainter:new MattePainter(Color.WHITE), layout:new MigLayout(), constraints:"gap 0, wrap") {
            label(font:new Font("Tahoma",1,11), foreground:new Color(51,102,255), text:"Download", constraints:"wrap")
            textArea(columns:20, constraints:"align left", font:new Font("Tahoma",0,11), lineWrap:true, rows:5, text:downloadText, wrapStyleWord:true, border:null,
                disabledTextColor:Color.BLACK, opaque:false, enabled:false, selectedTextColor:Color.WHITE, selectionColor:Color.BLACK)
        }

        // Screenshots panel
        panel(backgroundPainter:new MattePainter(new Color(240,239,239)), layout:new MigLayout(), constraints:"gap 0,wrap") {
            label(font:new Font("Tahoma",1,11), foreground:new Color(51,102,255), text:"Screenshots", constraints:"wrap")
            textArea(columns:20, constraints:"align left", font:new Font("Tahoma",0,11), lineWrap:true, rows:5, text:screenshotsText, wrapStyleWord:true, border:null,
                disabledTextColor:Color.BLACK, opaque:false, enabled:false, selectedTextColor:Color.WHITE, selectionColor:Color.BLACK)
        }
   }

   // western panel - cool product and dude
   panel(backgroundPainter:new MattePainter(Color.WHITE), layout:new MigLayout(), constraints:"west") {
        label(font:new Font("Tahoma",1,11), text:coolProduct)
        textArea(columns:28, constraints:"newline, gaptop 10, gapbottom 10", font:new Font("Tahoma",0,11), lineWrap:true, rows:5, text:coolProduct2, wrapStyleWord:true, disabledTextColor:Color.BLACK,
            enabled:false, selectedTextColor:Color.BLACK, border:null, selectionColor:Color.WHITE)
        label(font:new Font("Tahoma",1,11), text:dude, constraints:"newline")
        textArea(columns:28, constraints:"newline",font:new Font("Tahoma",0,11), lineWrap:true, rows:5, text:dude2, wrapStyleWord:true, disabledTextColor:Color.BLACK,
            enabled:false, selectedTextColor:Color.BLACK, border:null, selectionColor:Color.WHITE)
    }
}

// don't know why gant is killing it soo quick, let us look at it for 10 seconds...
sleep 10000