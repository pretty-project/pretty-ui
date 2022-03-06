
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sounds.subs
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-user.api :as user]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn enabled-by-user?
  ; @return (boolean)
  [db _]
  (r user/get-user-profile-item db :notifications :notification-sounds.enabled?))
