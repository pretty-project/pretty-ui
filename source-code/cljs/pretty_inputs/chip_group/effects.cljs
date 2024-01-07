
(ns pretty-inputs.chip-group.effects
    (:require [pretty-inputs.chip-group.events :as chip-group.events]
              [re-frame.api                      :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.chip-group/delete-chip!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) chip-dex
  (fn [{:keys [db]} [_ group-id group-props chip-dex]]
      {:db (r chip-group.events/delete-chip! db group-id group-props chip-dex)
       :fx [:pretty-inputs.input/mark-input-as-visited! group-id]}))
