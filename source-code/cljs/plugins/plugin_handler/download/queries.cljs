
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.download.queries
    (:require [mid-fruits.candy                 :refer [return]]
              [mid-fruits.vector                :as vector]
              [plugins.plugin-handler.body.subs :as body.subs]
              [x.app-core.api                   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-query-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (vector) query
  ;
  ; @usage
  ;  (r download.queries/use-query-prop db :my-plugin)
  ;
  ; @return (vector)
  [db [_ plugin-id query]]
  ; A plugin body komponense számára {:query [...]} tulajdonságként esetlegesen
  ; átadott Pathom lekérés vektort összefűzi a függvény számára query paraméterként
  ; átadott Pathom lekérés vektorral.
  (if-let [query-prop (r body.subs/get-body-prop db plugin-id :query)]
          (vector/concat-items query-prop query)
          (return                         query)))
