
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.download.events
    (:require [mid-fruits.map                         :as map]
              [plugins.item-preview.body.subs         :as body.subs]
              [plugins.item-preview.core.events       :as core.events]
              [plugins.item-preview.core.subs         :as core.subs]
              [plugins.item-preview.download.subs     :as download.subs]
              [plugins.plugin-handler.download.events :as download.events]
              [re-frame.api                           :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.events
(def data-received download.events/data-received)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  (r core.events/reset-downloads! db preview-id))

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ preview-id server-response]]
  ; XXX#3907
  ; A többi pluginnal megegyezően az item-preview plugin is névtér nélkül
  ; tárolja a letöltött dokumentumot.
  (let [resolver-id (r download.subs/get-resolver-id db preview-id :get-item)
        item-path   (r body.subs/get-body-prop       db preview-id :item-path)
        document    (-> server-response resolver-id map/remove-namespace)]
       (assoc-in db item-path document)))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ preview-id server-response]]
  (as-> db % (r data-received          % preview-id)
             (r store-downloaded-item! % preview-id server-response)))