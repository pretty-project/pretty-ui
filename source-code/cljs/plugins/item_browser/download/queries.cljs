
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.queries
    (:require [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.download.subs :as download.subs]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ browser-id]]
  (let [current-item-id (r core.subs/get-current-item-id db browser-id)]
       (r core.subs/use-query-params db browser-id {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (vector)
  [db [_ browser-id]]
  (let [resolver-id    (r download.subs/get-resolver-id   db browser-id :get-item)
        resolver-props (r get-request-item-resolver-props db browser-id)]
       ; XXX#9981
       ; A download.subs/use-query-prop függvényt az item-browser komponensben
       ; nem szükséges alkalmazni, mivel az engine alapját adó item-lister plugin
       ; az elemek első letöltésekor alkalmazza a függvényt.
       [`(~resolver-id ~resolver-props)]))
