
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.core.events
    (:require [engines.engine-handler.core.events :as core.events]
              [engines.item-viewer.body.subs      :as body.subs]
              [engines.item-viewer.core.subs      :as core.subs]
              [map.api                            :refer [dissoc-in]]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-item!  core.events/remove-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-engine-error!  core.events/set-engine-error!)
(def set-item-id!       core.events/set-item-id!)
(def clear-item-id!     core.events/clear-item-id!)
(def update-item-id!    core.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  (let [item-path (r body.subs/get-body-prop db viewer-id :item-path)]
       (-> db (dissoc-in [:engines :engine-handler/meta-items viewer-id :data-received?])
              (dissoc-in item-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  (r update-item-id! db viewer-id))

(defn reload-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  ; XXX#1400 (source-code/cljs/engines/item_browser/core/events.cljs)
  (as-> db % (r remove-meta-item! % viewer-id :engine-error)
             (r clear-item-id!    % viewer-id)
             (r update-item-id!   % viewer-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-viewer/set-engine-error! set-engine-error!)
