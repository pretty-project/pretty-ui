
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.events
    (:require [mid-plugins.item-lister.events :as events]
              [x.server-core.api              :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.events
(def store-lister-props! events/store-lister-props!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) lister-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace lister-props]]
  (r store-lister-props! db extension-id item-namespace lister-props))
