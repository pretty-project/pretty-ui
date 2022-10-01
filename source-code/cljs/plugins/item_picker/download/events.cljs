
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.download.events
    (:require [mid-fruits.map                         :as map]
              [plugins.item-picker.body.subs          :as body.subs]
              [plugins.item-picker.core.events        :as core.events]
              [plugins.item-picker.download.subs      :as download.subs]
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
  ; @param (keyword) picker-id
  ;
  ; @return (map)
  [db [_ picker-id]]
  ; XXX#5051
  (let [current-item-id (r get-current-item-id db picker-id)]
       (as-> db % (r core.events/reset-downloads! % picker-id)
                  (r core.events/set-meta-item!   % picker-id :requested-item current-item-id))))

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ picker-id server-response]]
  ; XXX#3907
  ; A többi pluginnal megegyezően az item-picker plugin is névtér nélkül
  ; tárolja a letöltött dokumentumot.
  (let [resolver-id (r download.subs/get-resolver-id db picker-id :get-item)
        item-path   (r body.subs/get-body-prop       db picker-id :item-path)
        document    (-> server-response resolver-id map/remove-namespace)]
       (assoc-in db item-path document)))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ picker-id server-response]]
  (as-> db % (r data-received          % picker-id)
             (r store-downloaded-item! % picker-id server-response)))
