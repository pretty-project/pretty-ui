
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.themes.subs
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-user.api :as user]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-theme
  ; @return (keyword)
  [db _]
  (r user/get-user-settings-item db :selected-theme))
