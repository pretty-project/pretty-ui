
# app-, mid- és server- prefixumok
Nem lehetséges ugyanazzal az elnevezéssel létrehozni egy névteret a clj/cljc vagy cljs/cljc oldalon
ÉS meghívni az azonos elnevezésű névteret, ezért szükséges a prefixumok alkalmazása a modulok neveiben.
Pl.:
my-namespace.clj nem hívhatja meg a my-namespace.cljc névteret mert a fordító tudja megkülönböztetni
a kettőt egymástól ezért "self-referential dependency" hibának minősül.



# Nagybetűs konstans nevek
A nagybetűkkel írt nevű konstansok elérhetők maradnak azokban a scope terekben, ahol megegyező
nevű paraméter van használatban.

```
(def MY-VALUE "abc")
(defn my-function
  [{:keys [my-value]}]
  (or my-value MY-VALUE)))
```
