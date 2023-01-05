
# js literal

###### BUG#0017

https://cljs.github.io/api/syntax/js-literal?fbclid=IwAR1y9-Ju6vPzEHqZ2lTl_YWROk84wZZWzwH2EHQBcWGcwHxJlfYFaIDRp7U

Data in the form of a map {} or vector [] must follow the #js tag, which will be
converted at compile-time to a JavaScript object or array, respectively.

A js literal használata megoldaná, hogy ne kelljen adatokat konvertálni oda-vissza
EDN térkép és JS objektum típus között!
