cd ..
ant -f build.xml dist
ant -f build.xml reflect-to-spring

cd ../dbflute-jdk15-example
. sync-lib.sh
cd dbflute_exampledb
rm ./log/*.log
. jdbc.sh
. doc.sh
. generate.sh
. sql2entity.sh
cd ..
ant
