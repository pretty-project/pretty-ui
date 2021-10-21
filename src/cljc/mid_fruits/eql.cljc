
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.16
; Description:
; Version: v0.3.4
; Compatibility: x4.3.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.eql
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [mid-fruits.mixed   :as mixed]
              [mid-fruits.string  :as string]))



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
;  egy ?-ban.
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



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def QUERY-OPENING "[")

; @constant (string)
(def QUERY-CLOSING "]")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-to-query
  ; @param (nil, string or vector) query
  ; @param (keyword, map, string or vector) query-question, query-action
  ;
  ; @example
  ;  (eql/append-to-query nil :all-users)
  ;  =>
  ;  "[:all-users]"
  ;
  ; @example
  ;  (eql/append-to-query [] :all-users)
  ;  =>
  ;  "[:all-users]"
  ;
  ; @example
  ;  (eql/append-to-query "[]" :all-users)
  ;  =>
  ;  "[:all-users]"
  ;
  ; @example
  ;  (eql/append-to-query "[]" ":all-users")
  ;  =>
  ;  "[:all-users]"
  ;
  ; @example
  ;  (eql/append-to-query [:all-users]
  ;                        [:directory/id :my-directory])
  ;  =>
  ;  "[:all-users [:directory/id :my-directory]]"
  ;
  ; @return (string)
  [query & query-questions]
  (let [query        (or query [])
        query        (mixed/mixed->string query)
        opened-query (string/before-last-occurence query QUERY-CLOSING)]
       (str (reduce (fn [result query-question]
                        (if (string/ends-with? result QUERY-OPENING)
                            (str result     query-question)
                            (str result " " query-question)))
                    (param opened-query)
                    (param query-questions))
            (param QUERY-CLOSING))))

(defn concat-queries
  ; @param (nil, string or vector) base-query
  ; @param (nil, string or vector) additional-query
  ;
  ; @example
  ;  (eql/concat-queries nil nil)
  ;  =>
  ;  "[]"
  ;
  ; @example
  ;  (eql/concat-queries [] [])
  ;  =>
  ;  "[]"
  ;
  ; @example
  ;  (eql/concat-queries [:all-users] [:all-games])
  ;  =>
  ;  "[:all-users :all-games]"
  ;
  ; @example
  ;  (eql/concat-queries [] "[:all-games]")
  ;  =>
  ;  "[:all-games]"
  ;
  ; @return (string)
  [base-query additional-query]
  (let [base-query              (or base-query       [])
        additional-query        (or additional-query [])
        base-query              (mixed/mixed->string base-query)
        additional-query        (mixed/mixed->string additional-query)
        opened-base-query       (string/before-last-occurence base-query QUERY-CLOSING)
        opened-additional-query (string/between-occurences additional-query QUERY-OPENING QUERY-CLOSING)]
       (str opened-base-query opened-additional-query QUERY-CLOSING)))

(defn query-action
  ; @param (string) mutation-f-name
  ; @param (map or string)(opt) mutation-props
  ;
  ; @example
  ;  (eql/query-action "media/recalculate-storage-usage!")
  ;  =>
  ;  "(media/recalculate-storage-usage!)"
  ;
  ; @example
  ;  (eql/query-action "media/create-directory!" {:source-directory-id "root"})
  ;  =>
  ;  "(media/create-directory! {:source-directory-id \"root\"})"
  ;
  ; @example
  ;  (eql/query-action "media/create-directory!" "{:source-directory-id "root"}")
  ;  =>
  ;  "(media/create-directory! {:source-directory-id \"root\"})"
  ;
  ; @return (string)
  [mutation-f-name & [mutation-props]]
  (if (or (map/nonempty?    mutation-props)
          (string/nonempty? mutation-props))
      (str "(" mutation-f-name " " mutation-props ")")
      (str "(" mutation-f-name ")")))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (keyword/join ">/" id))
