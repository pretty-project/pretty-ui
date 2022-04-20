
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.events
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn change-view!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (keyword) view-id
  ;
  ; @return (map)
  [db [_ editor-id view-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items editor-id :view-id] view-id))
