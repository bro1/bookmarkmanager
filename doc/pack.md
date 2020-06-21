# Packaging notes

## Linux


```
exec jpackage -n bookmarkmanager \
  -p "../../../reworked-modules/org.apache.commons.text-1.8.jar:$CLASSPATH" \
  -m plaintextbookmarksfx/com.bro1.bookmarks.BookmarksApp \
  --linux-shortcut \
  --app-version 1.0
```

## Windows

