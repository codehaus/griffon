@artifact.package@
class @artifact.name@ {
    static triggers = {
        cron name: '@artifact.name@', cronExpression: "30 * * * * ?"
    }

    String group = "MyGroup"

    void execute() { println "Job @artifact.name@: ping!" } 
}
