
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.eql
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name query (query-request)
;  Az egyes query (query-request) lekérések egy vagy több query-question kérdés
;  vagy query-action kérés felsorolásai egy vektorban.
;  A query vektor elemei lehetnek: resolver-id, document-entity, joined-query-question
;  query-action, ...
;
; @param (vector) query
;  [(keyword) resolver-id
;   (vector) document-entity
;   (map) joined-query-question
;   (?) query-action]
;
; @name resolver-id
;  Egy paramétert nem fogadó resolver azonosítója.
;  Pl.: :all-users
;
; @name document-link
;  Egy dokumentumban egy vagy több másik dokumentumot úgy érdemes tárolni (hozzácsatolni),
;  hogy a csatolt dokumentumoknak csak olyan kivonatát táruljuk ami nem tartalmaz
;  változó adatot.
;  Ilyen nem változó adat a csatolt dokumentumok azonosítója, aminek segítségével
;  egy csatolt dokumentumokat egyértelműen azonosíthatjuk és elérhetjük az
;  eredeti tárolási helyükön.
;
; Pl.: [{:account/id "0ce14671-e916-43ab-b057-0939329d4c1b"
;        :games [{:game/id "9cea3696-56ca-4be5-a5f2-e7477d9f43fb"}
;                {...}]}]
;
; @name document-entity
;  Olyan adat, amely egyértelműen azonosít egy dokumentumot.
;  Pl.: [:directory/id "my-directory"]
;       [:passenger/passport "KI-1993-6503688-FF"]
;
; @name joined-query-question
;  Egy térképben összekapcsolt entitás és az entitás által azonosított
;  dokumentumból kért adatok felsorolása egy vektorban.
;  Pl.: {[:directory/id "my-directory"] [:directory/alias]}
;
; @name query-question
;  Egy query-question kérdés lehet resolver-id azonosító, entity vagy joined-query-question térkép.
;  Pl.: :all-users
;       [:directory/id "my-directory"]
;       {[:directory/id "my-directory"] [:directory/alias]}
;
; @name query-action
;  Egy mutation függvény neve és a függvény számára átadott paraméterek térképe
;  egy listában.
;  Pl.: (media/create-directory! {:source-directory-id \"root\"})
;
; @name query-response
;  A szerver válasza a query vektorban felsorlt kérdésekre és kérésekre.
;  A válasz típusa térkép, amiben a kérdések és kérések a kulcsok és az azokra
;  adott válaszok az értékek.
;  {:all-users                     [{...} {...}]
;   [:directory/id "my-directory"] {:directory/alias "My directory"}
;   media/create-directory!        "Directory created"}
;
; @name query-answer
;  A query-response térkép értékei a query-answer válaszok.
;  Pl.: [{...} {...}]
;       {:directory/alias "My directory"}
;
; @name query-key
;  A query-response térkép kulcsai a query-key kulcsok. Egy query-key kulcs lehet
;  egy query-question kérdés, vagy egy mutation függvény neve.
;  Pl.: :all-users
;       [:directory/id "my-directory"]
;       mutation-f-name



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document-link?
  ; @param (*) n
  ;
  ; @example
  ;  (eql/document-link? {:directory/id "my-directory"})
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (and      (map?  n)
       (= 1 (count n))
       (-> n keys first keyword?)
       (-> n vals first string?)))

(defn id->document-link
  ; @param (string) id
  ; @param (keyword)(opt) namespace
  ;
  ; @example
  ;  (eql/id->document-link "my-directory")
  ;  =>
  ;  {:id "my-directory"}
  ;
  ; @example
  ;  (eql/id->document-link "my-directory" :directory)
  ;  =>
  ;  {:directory/id "my-directory"}
  ;
  ; @return (map)
  ([id]           {:id id})
  ([id namespace] {(keyword (name namespace) "id") id}))

(defn document-link->id
  ; @param (map) document-link
  ;
  ; @example
  ;  (eql/document-link->id {:directory/id "my-directory"})
  ;  =>
  ;  "my-directory"
  ;
  ; @return (string)
  [document-link]
  (-> document-link vals first))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn document-entity?
  ; @param (*) n
  ;
  ; @example
  ;  (eql/document-entity? [:directory/id "my-directory"])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (boolean (and      (vector? n)
                (= 2 (count   n))
                (keyword? (first  n))
                (string?  (second n)))))

(defn id->document-entity
  ; @param (string) id
  ; @param (keyword)(opt) namespace
  ;
  ; @example
  ;  (eql/id->document-entity "my-directory")
  ;  =>
  ;  [:id "my-directory"]
  ;
  ; @example
  ;  (eql/id->document-entity "my-directory" :directory)
  ;  =>
  ;  [:directory/id "my-directory"]
  ;
  ; @return (vector)
  ([id]           [:id id])
  ([id namespace] [(keyword (name namespace) "id") id]))

(defn document-entity->id
  ; @param (vector) document-entity
  ;
  ; @example
  ;  (eql/document-entity->id [:directory/id "my-directory"])
  ;  =>
  ;  "my-directory"
  ;
  ; @return (string)
  [document-entity]
  (second document-entity))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn id->placeholder
  ; @param (string) id
  ;
  ; @example
  ;  (eql/id->placeholder "my-id")
  ;  =>
  ;  :>/my-id
  ;
  ; @return (keyword)
  [id]
  (keyword (str ">/" (name id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-to-query
  ; @param (nil vector) query
  ; @param (keyword, map, string or vector) query-parts
  ;
  ; @example
  ;  (eql/append-to-query nil :all-users)
  ;  =>
  ;  [:all-users]
  ;
  ; @example
  ;  (eql/append-to-query [] :all-users)
  ;  =>
  ;  [:all-users]
  ;
  ; @example
  ;  (eql/append-to-query [:all-users]
  ;                       [:directory/id :my-directory])
  ;  =>
  ;  [:all-users [:directory/id :my-directory]]
  ;
  ; @return (vector)
  [query & query-parts]
  (cond (vector?  query) (vec (concat query   query-parts))
        (nil?     query) (vec (concat []      query-parts))
        :else            (vec (concat [query] query-parts))))
