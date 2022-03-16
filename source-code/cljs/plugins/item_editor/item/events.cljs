
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.item.events
    (:require [mid-fruits.map                 :refer [dissoc-in]]
              [plugins.item-editor.mount.subs :as mount.subs]
              [x.app-core.api                 :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [item-path (r mount.subs/get-body-prop db extension-id item-namespace :item-path)]
       (dissoc-in db item-path)))
