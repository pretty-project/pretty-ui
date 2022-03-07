
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.events
    (:require [mid-plugins.item-browser.events :as events]
              [x.server-core.api               :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.events
(def store-browser-props! events/store-browser-props!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (r store-browser-props! db extension-id item-namespace browser-props))
