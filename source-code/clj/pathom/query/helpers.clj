

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.query.helpers
    (:require [pathom.env.helpers :as env.helpers]
              [mid-fruits.candy   :refer [return]]
              [mid-fruits.reader  :as reader]
              [server-fruits.http :as http]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:params (map)(opt)
  ;    {:query (string or vector)(opt)}
  ;   :transit-params (map)(opt)
  ;    {:query (string or vector)(opt)}}
  ;
  ; @usage
  ;  (pathom/request->query {...})
  ;
  ; @return (vector)
  [request]
  ; Fájlfeltöltéskor a request törzse egy FormData objektum, amiből string típusként
  ; olvasható ki a query értéke ...
  (letfn [(f [query] (cond (vector? query) (return               query)
                           (string? query) (reader/string->mixed query)))]
         ; BUG#4509
         ; Abban az esetben ha a query értéke egy darab kulcsszó egy vektorban, akkor a transit-params
         ; térkép helyett params térképből kiolvasott query hibás lenne, ezért szükséges a query értékét
         ; a transit-params térképből kiolvasni!
         ; Pl.: [:my-resolver]
         ;      =>
         ;      {:transit-params {:query [:my-resolver]}
         ;       :params         {:query :my-resolver}}   <= ebben az esetben a query értéke nem vektor típus
         (if-let [query (http/request->transit-param request :query)]
                 (f query)
                 (if-let [query (http/request->param request :query)]
                         (f query)))))

(defn env->query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)
  ;    {:params (map)
  ;      {:query (vector)(opt)}}}
  ;
  ; @usage
  ;  (pathom/env->query {...})
  ;
  ; @return (vector)
  [env]
  (-> env env.helpers/env->request request->query))
