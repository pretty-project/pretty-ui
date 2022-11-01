
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.download.events
    (:require [engines.item-browser.body.subs     :as body.subs]
              [engines.item-browser.download.subs :as download.subs]
              [mid-fruits.map                     :as map]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-received-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ browser-id server-response]]
  ; XXX#3907
  ; A többi pluginnal megegyezően az item-browser engine is névtér nélkül
  ; tárolja a letöltött dokumentumot.
  (let [received-item (r download.subs/get-resolver-answer db browser-id :get-item server-response)
        item-path     (r body.subs/get-body-prop           db browser-id :item-path)]
       (assoc-in db item-path (map/remove-namespace received-item))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ browser-id server-response]]
  (r store-received-item! db browser-id server-response))
