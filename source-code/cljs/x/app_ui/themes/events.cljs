
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.themes.events
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-user.api :as user]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-selected-theme!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) theme-id
  ;
  ; @return (map)
  [db [_ theme-id]]
  (r user/set-user-settings-item! db :selected-theme theme-id))
