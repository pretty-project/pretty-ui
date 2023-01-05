
### A Java nyelvben az egy elemű string típusa CHAR, nem pedig STRING!

BUG#9080
Karakterek (egy elemű string típusok) összehasonlításakor (str) függvény használatával
szükséges string típusra alakítani a karaktert!

### BUG#0012

Closure compilation failed with 2 errors
--- node_modules/@ckeditor/ckeditor5-build-classic/build/ckeditor.js:7
Cannot convert ECMASCRIPT_2018 feature "RegExp named groups" to targeted output language.
--- node_modules/@ckeditor/ckeditor5-build-classic/build/ckeditor.js:7
Cannot convert ECMASCRIPT_2018 feature "RegExp unicode property escape" to targeted output language.
