
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.events
    (:require [plugins.plugin-handler.header.events :as header.events]
              [x.app-core.api                       :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.header.events
(def store-header-props!  header.events/store-header-props!)
(def remove-header-props! header.events/remove-header-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ editor-id header-props]]
  (r store-header-props! db editor-id header-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r remove-header-props! db editor-id))
