
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.events
    (:require [mid-plugins.item-editor.events :as events]
              [x.server-core.api              :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.events
(def store-editor-props! events/store-editor-props!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) editor-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace editor-props]]
  (r store-editor-props! db extension-id item-namespace editor-props))
