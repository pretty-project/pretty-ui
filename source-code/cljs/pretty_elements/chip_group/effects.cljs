
(ns pretty-elements.chip-group.effects
    (:require [pretty-elements.chip-group.events :as chip-group.events]
              [re-frame.api               :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.chip-group/delete-chip!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) chip-dex
  (fn [{:keys [db]} [_ group-id group-props chip-dex]]
      {:db (r chip-group.events/delete-chip! db group-id group-props chip-dex)
       :fx [:pretty-elements.input/mark-input-as-visited! group-id]}))