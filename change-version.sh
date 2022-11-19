#!/bin/bash
currentVersion=`awk 'BEGIN{verisonMatch=0;} {if(match($1, /<version>(.*)<\/version>/) && versionMatch == 0){print substr($1,10,RLENGTH-19); versionMatch++;}}' pom.xml`;
echo "Current Version: $currentVersion";
echo "Please enter new Version: ";
read newVersion;
while true; do
    read -p "Do you wish to change version to $newVersion? [Y/N]" yn
    case $yn in
        [Yy]* )  for f in `find . -name pom.xml`; do echo "Updating version to $f"; sed -i '' "1,/RE/s/$currentVersion/$newVersion/" $f; done; break;;
        [Nn]* ) exit;;
        * ) echo "Please answer yes or no.";;
    esac
done

