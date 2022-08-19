
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.events
    (:require [plugins.item-browser.body.subs     :as body.subs]
              [plugins.item-browser.download.subs :as download.subs]
              [x.app-core.api                     :refer [r]]
              [x.app-db.api                       :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ browser-id server-response]]
  ; XXX#3907
  ; Az item-lister pluginnal megegyezően az item-browser plugin is névtér nélkül tárolja a letöltött dokumentumot
  (let [resolver-id (r download.subs/get-resolver-id db browser-id :get-item)
        item-path   (r body.subs/get-body-prop       db browser-id :item-path)
        document    (-> server-response resolver-id db/document->non-namespaced-document)]
       (assoc-in db item-path document)))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ browser-id server-response]]
  (r store-downloaded-item! db browser-id server-response))
