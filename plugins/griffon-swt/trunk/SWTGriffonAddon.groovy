import griffon.swt.ApplicationFactory
import griffon.util.UIThreadHelper

class SWTGriffonAddon {
    def factories = [
        application: new ApplicationFactory()
    ]

    def methods = [
        execAsync: UIThreadHelper.instance.&executeAsync,
        execSync: UIThreadHelper.instance.&executeSync,
        execOutside: UIThreadHelper.instance.&executeOutside
    ]
}
