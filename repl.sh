sed -i 's/-classpath/-p/g' target/appassembler/bin/ClipCatch.sh
sed -i 's/\(com\.bro1\.bookmarks\.BookmarksApp\)/-m plaintextbookmarksfx\/\1/g' target/appassembler/bin/ClipCatch.sh
