
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.16
; Description:
; Version: v0.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.eql
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#5569 ----------------------------------------------------------------

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
;  Sample: :all-users
;
; @name document-entity
;  Olyan adat, amely egyértelműen azonosít egy dokumentumot.
;  Sample: [:directory/id "my-directory"]
;          [:passenger/passport "KI-1993-6503688-FF"]
;
; @name joined-query-question
;  Egy térképben összekapcsolt entitás és az entitás által azonosított
;  dokumentumból kért adatok felsorolása egy vektorban.
;  Sample: {[:directory/id "my-directory"] [:directory/alias]}
;
; @name query-question
;  Egy query-question kérdés lehet resolver-id azonosító, document-entity entitás
;  vagy joined-query-question térkép.
;  Sample: :all-users
;          [:directory/id "my-directory"]
;          {[:directory/id "my-directory"] [:directory/alias]}
;
; @name query-action
;  Egy mutation függvény neve és a függvény számára átadott paraméterek térképe
;  egy listában.
;  Sample: (media/create-directory! {:source-directory-id \"root\"})
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
;  Sample: [{...} {...}]
;          {:directory/alias "My directory"}
;
; @name query-key
;  A query-response térkép kulcsai a query-key kulcsok. Egy query-key kulcs lehet
;  egy query-question kérdés, vagy egy mutation függvény neve.
;  Sample: :all-users
;          [:directory/id "my-directory"]
;          mutation-f-name



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn entity?
  ; @param (vector) n
  ;
  ; @example
  ;  (eql/entity? [:directory/id "my-directory"])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [n]
  (boolean (and      (vector? n)
                (= 2 (count   n))
                (keyword? (first  n))
                (string?  (second n)))))

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

(defn id->placeholder
  ; @param (keyword or string) id
  ;
  ; @example
  ;  (eql/id->placeholder :my-id)
  ;  =>
  ;  :>/my-id
  ;
  ; @example
  ;  (eql/id->placeholder "my-id")
  ;  =>
  ;  :>/my-id
  ;
  ; @return (keyword)
  [id]
  (cond (string?  id) (keyword (str ">/"       id))
        (keyword? id) (keyword (str ">/" (name id)))))

(defn id->entity
  ; @param (string) id
  ; @param (keyword)(opt) namespace
  ;
  ; @example
  ;  (eql/id->entity "my-directory")
  ;  =>
  ;  [:id "my-directory"]
  ;
  ; @example
  ;  (eql/id->entity "my-directory" :directory)
  ;  =>
  ;  [:directory/id "my-directory"]
  ;
  ; @return (vector)
  ([document-id]           [:id document-id])
  ([document-id namespace] [(keyword (name namespace) "id") document-id]))

(defn entity->id
  ; @param (vector) entity
  ;
  ; @example
  ;  (eql/entity->id [:directory/id "my-directory"])
  ;  =>
  ;  "my-directory"
  ;
  ; @return (string)
  [document-entity]
  (second document-entity))
