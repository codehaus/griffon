
//TODO config slurping and writing
new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'groovy.swing.JideBuilder'.view = '*'
""")
