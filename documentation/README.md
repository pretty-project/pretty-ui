
# How to compile

```
clj -X:dev
```

```
clj -X:prod
java -jar mono-app.jar
```



# How to deploy

```
```



# A mid- prefixum

Nem lehetséges ugyanazzal az elnevezéssel létrehozni egy névteret a clj/cljc vagy cljs/cljc oldalon
ÉS meghívni az azonos elnevezésű névteret, ezért szükséges a cljc névterekben a mid prefixum használata
a modulok neveiben.
Pl.: my-namespace.clj nem hívhatja meg a my-namespace.cljc névteret mert a fordító nem tudja megkülönböztetni
     a kettőt egymástól ezért "self-referential dependency" hibának minősülne.
     my-namespace.clj és mid.my-namespace.cljc formula szerint kell őket elnevezni!



# Nagybetűs konstans nevek

A nagybetűkkel írt nevű konstansok elérhetők maradnak azokban a scope terekben, ahol megegyező
nevű paraméter van használatban.

```
(def MY-VALUE "abc")

(defn my-function
  [{:keys [my-value]}]
  (or my-value MY-VALUE)))
```
