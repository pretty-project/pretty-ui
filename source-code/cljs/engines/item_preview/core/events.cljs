
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.core.events
    (:require [engines.engine-handler.core.events :as core.events]
              [engines.item-preview.body.subs     :as body.subs]
              [map.api                            :refer [dissoc-in]]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-item!  core.events/remove-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)
(def set-engine-error!  core.events/set-engine-error!)
(def set-item-id!       core.events/set-item-id!)
(def clear-item-id!     core.events/clear-item-id!)
(def update-item-id!    core.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  (let [item-path (r body.subs/get-body-prop db preview-id :item-path)]
       (-> db (dissoc-in [:engines :engine-handler/meta-items preview-id :data-received?])
              (dissoc-in item-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-preview!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  (r update-item-id! db preview-id))

(defn reload-preview!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  ; XXX#1400 (source-code/cljs/engines/item_browser/core/events.cljs)
  (as-> db % (r remove-meta-item! % preview-id :engine-error)
             (r clear-item-id!    % preview-id)
             (r update-item-id!   % preview-id)
             (r reset-downloads!  % preview-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-preview/set-engine-error! set-engine-error!)
