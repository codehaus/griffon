import griffon.util.UIThreadHelper

class JFaceGriffonAddon {
    def factories = [:]

    def methods = [
        execAsync: UIThreadHelper.instance.&executeAsync,
        execSync: UIThreadHelper.instance.&executeSync,
        execOutside: UIThreadHelper.instance.&executeOutside
    ]
}
